package dev.sanskar.featuretesteduco.core.domain.util

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import java.security.AccessController.getContext


class AndroidDownloader(
   val context: Context
): Downloader {
    private val conntext= getContext()

    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    init {
        createNotificationChannel()
//        context.registerReceiver(DownloadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle("Downloading resource file")
            .setDescription("resource.jpg")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "resource.jpg")
        val downloadId = downloadManager.enqueue(request)
        showDownloadingNotification(downloadId)
        return downloadId
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Download Channel"
            val descriptionText = "Notification channel for download manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("DOWNLOAD_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
            println(channel)
        }
    }

    private fun showDownloadingNotification(downloadId: Long) {
        val notification = NotificationCompat.Builder(context , "DOWNLOAD_CHANNEL")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Downloading resource fileSSS")
            .setContentText("Download in progress")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
        notificationManager.notify(downloadId.toInt(), notification)
        println(notification)
    }


}