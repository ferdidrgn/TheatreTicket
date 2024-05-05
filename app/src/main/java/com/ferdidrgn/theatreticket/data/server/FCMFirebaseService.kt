package com.ferdidrgn.theatreticket.data.server

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ferdidrgn.theatreticket.R
import com.ferdidrgn.theatreticket.presentation.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.w("Token: ", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("msg", "onMessageReceived: " + remoteMessage.data["message"])
        Log.d("msg", "From: ${remoteMessage.from}")
        Log.d("msg", "Message data payload: ${remoteMessage.data}")

        if (remoteMessage.notification != null) {
            Log.d(
                "msg",
                "Message Notification Body: ${remoteMessage.notification!!.body}"
            )
            sendNotification(remoteMessage.notification!!)
        }
    }

    private fun sendNotification(message: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("message", message.body)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "default"
        val channelName = "channelName"
        val channelDescription = "channelDescription"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_alarm_alert_bell_notification)
            .setColor(resources.getColor(R.color.pink))
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                channel.description = channelDescription
                notificationManager.createNotificationChannel(channel)
            }
        } catch (e: Exception) {
            Log.d("msg", "onMessageReceived: ${e.localizedMessage}")
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}