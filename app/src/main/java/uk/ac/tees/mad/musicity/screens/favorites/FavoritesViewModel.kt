package uk.ac.tees.mad.musicity.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.ac.tees.mad.musicity.data.database.FavoriteTrackDao
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteTrackDao: FavoriteTrackDao
) : ViewModel() {


    val favoriteTracks =
        favoriteTrackDao.getAllFavoriteTracks()
            .flowOn(Dispatchers.IO)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                emptyList()
            )


    fun removeTrackFromFavorites(trackId: Long) {
        viewModelScope.launch {
            favoriteTrackDao.removeFavoriteTrack(trackId)
        }
    }
}
