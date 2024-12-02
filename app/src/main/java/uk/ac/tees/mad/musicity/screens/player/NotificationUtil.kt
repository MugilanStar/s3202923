package uk.ac.tees.mad.musicity.screens.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import uk.ac.tees.mad.musicity.R

object NotificationUtil {
    private const val CHANNEL_ID = "music_player_channel"
    private const val NOTIFICATION_ID = 1

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Music Playback",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notification channel for music playback"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showTrackNotification(context: Context, trackTitle: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentTitle("Now Playing")
            .setContentText(trackTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.EXTRA_MEDIA_SESSION)
            .setSilent(true)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

}
