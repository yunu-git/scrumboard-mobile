package nz.ac.canterbury.seng303.scrumboardmobile.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver
import nz.ac.canterbury.seng303.scrumboardmobile.R

const val notificationId = 1
const val channelId = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"


@SuppressLint("RestrictedApi")
class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentText(intent?.getStringExtra(messageExtra))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }
}