package uk.ac.tees.mad.musicity.screens.splash

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.musicity.R
import uk.ac.tees.mad.musicity.navigation.HomeDestination
import uk.ac.tees.mad.musicity.navigation.LoginDestination
import uk.ac.tees.mad.musicity.navigation.SplashDestination

@Composable
fun SplashScreen(navController: NavHostController) {
    val splashScreenDuration = 2000L // 2 seconds

    // Animation state
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        // Animate the logo and name fading in
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing)
        )
        // Wait for splash screen duration
        delay(splashScreenDuration)
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("THREAD", Thread.currentThread().name)
            navController.navigate(if (Firebase.auth.currentUser != null) HomeDestination.route else LoginDestination.route) {
                popUpTo(SplashDestination.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            LoaderAnimation(
                modifier = Modifier
                    .size(300.dp)
                    .scale(alphaAnim.value),
                anim = R.raw.music
            )
        }
        Text(
            text = "Musicity",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .alpha(alphaAnim.value)
                .align(Alignment.BottomCenter)
        )

    }
}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int, iterations: Int = 1) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(anim))

    LottieAnimation(
        composition = composition,
        modifier = modifier,
        iterations = iterations
    )
}