package uk.ac.tees.mad.musicity.screens.login

import android.app.Activity
import android.content.Intent
import android.credentials.GetCredentialException
import android.credentials.GetCredentialRequest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch
import uk.ac.tees.mad.musicity.R
import uk.ac.tees.mad.musicity.screens.splash.LoaderAnimation

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    val startAddAccountIntentLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            doGoogleSignIn(viewModel, coroutineScope, context, null)
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            LoaderAnimation(
                modifier = Modifier
                    .size(200.dp),
                anim = R.raw.music
            )
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text("Sign in to ", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Musicity",

                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF009688),
                        fontSize = 24.sp,
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Card(
            onClick = {
                doGoogleSignIn(
                    viewModel,
                    coroutineScope,
                    context,
                    startAddAccountIntentLauncher
                )
            }
        ) {
            Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

    }
}