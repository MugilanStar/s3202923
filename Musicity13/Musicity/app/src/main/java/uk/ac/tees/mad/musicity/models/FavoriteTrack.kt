package uk.ac.tees.mad.musicity.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrack(
    @PrimaryKey val id: Long,
    val title: String,
    val artist: String,
    val previewUrl: String
)
