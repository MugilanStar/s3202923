package uk.ac.tees.mad.musicity.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val artist: Artist,
    val id: Long,
    val md5_image: String,
    val preview: String,
    val title: String
) : Parcelable