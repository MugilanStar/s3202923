package uk.ac.tees.mad.musicity.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val firebase = FirebaseAuth.getInstance()
    private val uid = firebase.uid

    init {
        getUserData()
    }

    fun getUserData() = viewModelScope.launch {
        uid?.let {
            firestore.collection("users").document(uid).get().addOnSuccessListener { snapshot ->
                val data = snapshot.data
                _user.value = User(
                    uid = data?.get("uid") as String,
                    name = data["name"] as String,
                    email = data["email"] as String,
                    photoUrl = data["photoUrl"] as String
                )
            }.addOnFailureListener {
                it.printStackTrace()
            }
        }
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            uid?.let {
                firestore.collection("users").document(uid)
                    .update("name", newName).addOnSuccessListener {
                        _user.value = _user.value?.copy(name = newName)
                        getUserData()
                    }
            }
        }
    }

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            uid?.let {
                val storageRef = storage.reference.child("profile_pictures/$uid.jpg")
                storageRef.putFile(uri).await()

                val imageUrl = storageRef.downloadUrl.await().toString()
                firestore.collection("users").document(uid)
                    .update("photoUrl", imageUrl)
                _user.value =
                    _user.value?.copy(photoUrl = imageUrl)
            }
        }
    }

    fun logout() {
        firebase.signOut()
    }
}

data class User(
    val uid: String,
    val name: String,
    val email: String,
    val photoUrl: String
)

