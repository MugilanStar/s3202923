package uk.ac.tees.mad.musicity.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import uk.ac.tees.mad.musicity.models.Data
import uk.ac.tees.mad.musicity.navigation.BottomNavigationBar
import uk.ac.tees.mad.musicity.navigation.MusicPlayerDestination

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val trendingMusic by viewModel.trendingMusic.collectAsState()
    val searchedMusic by viewModel.searchedMusic.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                Text(
                    text = "Trending Music",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            items(trendingMusic) { musicItem ->
                MusicCard(
                    musicItem = musicItem,
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle
                            ?.set(
                                "track",
                                musicItem
                            )
                        navController.navigate(MusicPlayerDestination.route)
                    }
                )
            }
        }
    }
}

@Composable
fun MusicCard(
    musicItem: Data,
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
        Row(Modifier.fillMaxWidth()) {

            Image(
                painter = rememberAsyncImagePainter("https://e-cdns-images.dzcdn.net/images/cover/${musicItem.md5_image}/500x500.jpg"),
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
                    text = musicItem.artist.name,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

