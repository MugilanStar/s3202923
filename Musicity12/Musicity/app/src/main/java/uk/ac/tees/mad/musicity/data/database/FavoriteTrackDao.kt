package uk.ac.tees.mad.musicity.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.musicity.models.FavoriteTrack

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteTrack(track: FavoriteTrack)

    @Query("DELETE FROM favorite_tracks WHERE id = :trackId")
    suspend fun removeFavoriteTrack(trackId: String)

    @Query("SELECT * FROM favorite_tracks")
    fun getAllFavoriteTracks(): Flow<List<FavoriteTrack>>

//    @Query("SELECT * FROM favorite_tracks WHERE id = :trackId")
//    suspend fun getFavoriteTrackById(trackId: String): FavoriteTrack?
}
