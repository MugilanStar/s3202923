package uk.ac.tees.mad.musicity.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.ac.tees.mad.musicity.models.FavoriteTrack

@Database(entities = [FavoriteTrack::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteTrackDao(): FavoriteTrackDao
}
