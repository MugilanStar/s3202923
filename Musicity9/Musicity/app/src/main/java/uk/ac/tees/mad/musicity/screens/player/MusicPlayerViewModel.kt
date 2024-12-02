package uk.ac.tees.mad.musicity.screens.player

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.musicity.data.repository.MusicRepository
import uk.ac.tees.mad.musicity.models.Data
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _currentTrack = MutableStateFlow<Data?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    init {
        loadInitialTrack()
    }

    private fun loadInitialTrack(musicData: Data) {
        viewModelScope.launch {
            _currentTrack.value = musicData
            currentTrack.value?.let { playMusic(it) }
        }
    }

    fun playMusic(track: Data? = currentTrack.value) {
        track?.let {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(it.preview)
                prepare()
                start()
            }
            _isPlaying.value = true
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
        _isPlaying.value = false
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
