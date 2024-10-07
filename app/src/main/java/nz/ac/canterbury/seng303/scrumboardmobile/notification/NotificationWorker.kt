package nz.ac.canterbury.seng303.scrumboardmobile.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import nz.ac.canterbury.seng303.scrumboardmobile.R

class NotificationWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    override fun doWork(): Result {
        val title = inputData.getString("title") ?: applicationContext.getString(R.string.notification_title)
        val message = inputData.getString("message") ?: applicationContext.getString(R.string.notification_message)

        showNotification(title, message)
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            "default_channel_id",
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, "default_channel_id")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        notificationManager.notify(1, notification)
    }
}