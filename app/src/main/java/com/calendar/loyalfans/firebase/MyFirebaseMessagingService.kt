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
import com.calendar.loyalfans.activities.BaseActivity
import com.calendar.loyalfans.activities.MainActivity
import com.calendar.loyalfans.activities.SplashActivity
import com.calendar.loyalfans.utils.Common
import com.calendar.loyalfans.utils.SPHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Notification Received")
        if (SPHelper(BaseActivity.getActivity()).getLoginData() != null) {
            remoteMessage.notification?.let {
                Log.d(TAG, "Message Notification Body: ${it.body}")
                it.body?.let { it1 ->
                    sendNotification(
                        it.title.toString(),
                        it1,
                        it.clickAction
                    )
                }
            }
        } else {
            Log.d(TAG, "No Login")
        }
    }

    override fun onNewToken(token: String) {
        Log.d("Token---", token)
        Common.updateTokenToServer(token)
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(
        title: String,
        messageBody: String,
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


        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(title)
            .setAutoCancel(true)
            .setPriority(2)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}