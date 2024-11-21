package uk.ac.tees.mad.musicity.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import uk.ac.tees.mad.musicity.models.MusicResponse

interface MusicService {

    @Headers(
        "x-rapidapi-key: 86150f6146mshdc2e64b37b566d8p1fd284jsnde5423212894",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com"
    )
    @GET("search")
    fun getAllTopMusic(
        @Query("q") query: String
    ): Response<MusicResponse>
}