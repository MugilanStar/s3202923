package uk.ac.tees.mad.musicity.screens.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _uiState = mutableStateOf(AuthUiState())
    val uiState = _uiState

    private val TAG = "LOGINVIEWMODEL"

    // Function to update UI state
    private fun updateUiState(update: (AuthUiState) -> AuthUiState) {
        _uiState.value = update(_uiState.value)
    }

    // Handle sign-in results from Google Identity Services
    fun onEvent(event: AuthUIEvent) {
        when (event) {
            is AuthUIEvent.HandleSignInResult -> {
                viewModelScope.launch {
                    handleSignIn(event.result)
                }
            }
        }
    }

    // Complete the sign-in function for handling result
    suspend fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val googleIdToken = googleIdTokenCredential.idToken

                        val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                        val user = Firebase.auth.signInWithCredential(authCredential).await().user
                        user?.run {
                            updateUiState {
                                AuthUiState(
                                    alreadySignUp = true,
                                    isLoading = false,
                                    user = user,
                                    isAnonymous = user.isAnonymous,
                                    isAuthenticated = true,
                                    authState = if (user.isAnonymous) AuthState.Authenticated else AuthState.SignedIn
                                )
                            }

                            // Store user info in Firestore
                            storeUserInFirestore(user)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in sign-in: ${e.message}", e)
                    }
                }
            }

            else -> Log.e(TAG, "Unexpected type of credential")
        }
    }

    // Store user details in Firestore after successful sign-in
    private suspend fun storeUserInFirestore(user: FirebaseUser) {
        val firestore = Firebase.firestore
        val userMap = hashMapOf(
            "uid" to user.uid,
            "name" to user.displayName,
            "email" to user.email,
            "photoUrl" to user.photoUrl.toString()
        )
        firestore.collection("users").document(user.uid).set(userMap).await()
    }
}


fun doGoogleSignIn(
    authViewModel: LoginViewModel,
    coroutineScope: CoroutineScope,
    context: Context,
    startAddAccountIntentLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
) {

    val credentialManager = CredentialManager.create(context)

    fun getGoogleIdOption(context: Context): GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("")
        .setAutoSelectEnabled(true)
        .build()

    val googleSignRequest: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(getGoogleIdOption(context))
        .build()

    coroutineScope.launch {
        try {
            val result = credentialManager.getCredential(
                request = googleSignRequest,
                context = context,
            )

            authViewModel.onEvent(AuthUIEvent.HandleSignInResult(result))
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            startAddAccountIntentLauncher?.launch(getAddGoogleAccountIntent())
        } catch (e: GetCredentialException) {
            e.printStackTrace()
        }
    }
}

fun getAddGoogleAccountIntent(): Intent {
    val intent = Intent(Settings.ACTION_ADD_ACCOUNT)
    intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
    return intent
}

data class AuthUiState(
    val alreadySignUp: Boolean = false,
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val isAnonymous: Boolean = true,
    val isAuthenticated: Boolean = false,
    val authState: AuthState = AuthState.Unauthenticated
)

enum class AuthState {
    Unauthenticated,
    Authenticated,
    SignedIn
}

sealed class AuthUIEvent {
    data class HandleSignInResult(val result: GetCredentialResponse) : AuthUIEvent()
}
