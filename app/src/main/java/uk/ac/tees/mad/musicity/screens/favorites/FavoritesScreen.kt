package uk.ac.tees.mad.musicity.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.musicity.models.FavoriteTrack
import uk.ac.tees.mad.musicity.models.toData
import uk.ac.tees.mad.musicity.navigation.BottomNavigationBar
import uk.ac.tees.mad.musicity.navigation.MusicPlayerDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    // Observe the favorite tracks list
    val favoriteTracks by viewModel.favoriteTracks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Tracks") }

            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { padd ->
        if (favoriteTracks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padd),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No favorite tracks found")
            }
        } else {
            // Display the list of favorite tracks
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padd),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoriteTracks) { track ->
                    FavoriteTrackItem(
                        musicItem = track,
                        onRemoveClick = { viewModel.removeTrackFromFavorites(track.id) },
                        onClick = {
                            navController.currentBackStackEntry?.savedStateHandle
                                ?.set(
                                    "track",
                                    track.toData()
                                )
                            navController.navigate(MusicPlayerDestination.route)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteTrackItem(
    musicItem: FavoriteTrack,
    onRemoveClick: () -> Unit,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {

                Image(
                    painter = rememberAsyncImagePainter("https://e-cdns-images.dzcdn.net/images/cover/${musicItem.image}/500x500.jpg"),
                    contentDescription = null,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {

                    Text(
                        text = musicItem.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = musicItem.artist,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = onRemoveClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove from favorites",
                    tint = Color.Red
                )
            }
        }
    }
}
