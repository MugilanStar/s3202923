package uk.ac.tees.mad.musicity.data.repository

import uk.ac.tees.mad.musicity.data.api.MusicService
import uk.ac.tees.mad.musicity.models.Data
import uk.ac.tees.mad.musicity.models.MusicResponse
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val apiService: MusicService
) {
    suspend fun getTrendingMusic(): List<Data> {
        return try {
            val response = apiService.getAllTopMusic("trending")
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
}
