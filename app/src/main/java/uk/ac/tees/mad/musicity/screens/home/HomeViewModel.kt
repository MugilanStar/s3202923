package uk.ac.tees.mad.musicity.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.musicity.data.repository.MusicRepository
import uk.ac.tees.mad.musicity.models.Data
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _trendingMusic = MutableStateFlow<List<Data>>(emptyList())
    val trendingMusic = _trendingMusic.asStateFlow()

    private val _searchedMusic = MutableStateFlow<List<Data>>(emptyList())
    val searchedMusic = _searchedMusic.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    init {
        fetchTrendingMusic()
    }

    private fun fetchTrendingMusic() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            _trendingMusic.value = musicRepository.getTrendingMusic()
            _isLoading.emit(false)
        }
    }

     fun fetchSearchMusic(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            _searchedMusic.value = musicRepository.searchMusic(query)
            _isLoading.emit(false)
        }
    }
}
