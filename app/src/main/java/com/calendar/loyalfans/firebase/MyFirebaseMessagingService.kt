package com.calendar.loyalfans.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.calendar.loyalfans.R
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.activities.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val webViewURL = remoteMessage.data["webViewURL"]
            webViewURL?.let { Log.d("WebViewURL", it) }

        } catch (e: Exception) {
        }
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            it.body?.let { it1 ->
                sendNotification(
                    it1,
                    it.sound,
                    it.clickAction
                )
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d("Token---", token)
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(
        messageBody: String,
        sound: String?,
        clickAction: String?,
    ) {
        val channelId = "101"

        val intent = if (clickAction.equals("MainActivity")) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, SplashActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

//        val NOTIFICATION_SOUND =
//            Uri.parse("android.resource://com.gama.whistle/raw/$sound")

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(2)
//            .setSound(NOTIFICATION_SOUND)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val audioAttributes = AudioAttributes.Builder()
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .setUsage(AudioAttributes.USAGE_ALARM)
//                .build()

            val channel = NotificationChannel(
                channelId, sound,
                NotificationManager.IMPORTANCE_HIGH
            )
//            channel.setSound(NOTIFICATION_SOUND, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}