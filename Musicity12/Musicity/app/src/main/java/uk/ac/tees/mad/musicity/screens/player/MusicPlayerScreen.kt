package uk.ac.tees.mad.musicity.screens.player

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import uk.ac.tees.mad.musicity.models.Data

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MusicPlayerScreen(
    navController: NavHostController,
    viewModel: MusicPlayerViewModel = hiltViewModel(),
    musicData: Data
) {
    val currentTrack by viewModel.currentTrack.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    val postNotificationPermission =
        rememberPermissionState(permission = "android.permission.POST_NOTIFICATIONS")
    LaunchedEffect(Unit) {
        if (!postNotificationPermission.status.isGranted) {
            postNotificationPermission.launchPermissionRequest()
        }
        viewModel.loadInitialTrack(musicData)
        Log.d("MUSIC", "Music player: $musicData")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Now Playing") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Album Art
                currentTrack?.let { track ->
                    Image(
                        painter = rememberAsyncImagePainter("https://e-cdns-images.dzcdn.net/images/cover/${track.md5_image}/500x500.jpg"),
                        contentDescription = "Album Art",
                        modifier = Modifier
                            .size(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Song Title and Artist
                Text(
                    text = currentTrack?.title ?: "Unknown Title",
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = currentTrack?.artist?.name ?: "Unknown Artist",
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { viewModel.playPreviousTrack() }) {
                        Icon(Icons.Default.SkipPrevious, contentDescription = "Previous")
                    }

                    IconButton(onClick = {
                        if (isPlaying) {
                            viewModel.pauseMusic()
                        } else {
                            viewModel.playMusic()
                        }
                    }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    IconButton(onClick = { viewModel.playNextTrack() }) {
                        Icon(Icons.Default.SkipNext, contentDescription = "Next")
                    }
                }
            }
        }
    }
}
