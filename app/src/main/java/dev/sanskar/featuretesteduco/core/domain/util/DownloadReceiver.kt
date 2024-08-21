package dev.sanskar.featuretesteduco.core.domain.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.DownloadManager
import android.app.NotificationManager
import androidx.core.app.NotificationCompat

class DownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (id != -1L) {
            println("${id}")
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification = NotificationCompat.Builder(context, "DOWNLOAD_CHANNEL")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Download completeDD")
                .setContentText("resource.jpg has been downloaded")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(id.toInt(), notification)
        }
    }
}
