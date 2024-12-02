package uk.ac.tees.mad.musicity.data.repository

import android.util.Log
import uk.ac.tees.mad.musicity.data.api.MusicService
import uk.ac.tees.mad.musicity.models.Data
import uk.ac.tees.mad.musicity.models.MusicResponse
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val apiService: MusicService
) {
    private var currentTrackIndex: Int = 0
    private var tracks: List<Data> = emptyList()

    suspend fun getTrendingMusic(): List<Data> {
        return try {
            val response = apiService.getAllTopMusic("top music")

            if (response.isSuccessful) {
                val trackResponse = response.body()?.data ?: emptyList()
                tracks = trackResponse
                trackResponse
            } else {
                emptyList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emptyList()
        }
    }

    suspend fun searchMusic(query: String): List<Data> {
        return try {
            val response = apiService.getAllTopMusic(query)
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emptyList()
        }
    }

    fun getNextTrack(): Data? {
        Log.d("MUSIC", "Tracks: ${tracks.size}")
        if (tracks.isEmpty()) return null
        currentTrackIndex = (currentTrackIndex + 1) % tracks.size
        return tracks[currentTrackIndex]
    }

    fun getPreviousTrack(): Data? {
        if (tracks.isEmpty()) return null
        currentTrackIndex =
            if (currentTrackIndex - 1 < 0) tracks.size - 1 else currentTrackIndex - 1
        return tracks[currentTrackIndex]
    }
}
