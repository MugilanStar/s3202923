package uk.ac.tees.mad.musicity.screens.player

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.musicity.data.repository.MusicRepository
import uk.ac.tees.mad.musicity.models.Data
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val context: Application
) : ViewModel() {

    private val _currentTrack = MutableStateFlow<Data?>(null)
    val currentTrack = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            musicRepository.getTrendingMusic()
        }
    }

    fun loadInitialTrack(musicData: Data) {
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
            showTrackNotification(it.title)
        }
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
        _isPlaying.value = false
        showTrackNotification(currentTrack.value?.title ?: "Unknown track", isPlaying = false)
    }

    fun playNextTrack() {
        viewModelScope.launch {
            mediaPlayer?.stop()
            val nextTrack = musicRepository.getNextTrack()
            Log.d("MUSIC", "Next track: $nextTrack")
            _currentTrack.value = nextTrack
            playMusic(nextTrack)
        }
    }

    fun playPreviousTrack() {
        viewModelScope.launch {
            val previousTrack = musicRepository.getPreviousTrack()
            _currentTrack.value = previousTrack
            playMusic(previousTrack)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun showTrackNotification(trackTitle: String, isPlaying: Boolean = true) {
        val notificationText = if (isPlaying) {
            "$trackTitle is being played now"
        } else {
            "$trackTitle is paused"
        }

        NotificationUtil.createNotificationChannel(context)
        NotificationUtil.showTrackNotification(context, notificationText)
    }
}
