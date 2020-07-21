package com.fycus.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fycus.R
import com.fycus.ui.activity.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val gSon = Gson()
            val jsonObject = JSONObject(remoteMessage.data as Map<*, *>)
            val firebasePushResponse: FirebasePushResponse = gSon.fromJson<FirebasePushResponse>(
                jsonObject.toString(),
                FirebasePushResponse::class.java
            )
            // sendNotification(firebasePushResponse);
            sendNotification(firebasePushResponse)
        } else {
            val gSon = Gson()
            val jsonObject = JSONObject(remoteMessage.data as Map<*, *>)
            val firebasePushResponse: FirebasePushResponse = gSon.fromJson<FirebasePushResponse>(
                jsonObject.toString(),
                FirebasePushResponse::class.java
            )
            firebasePushResponse.title =
                remoteMessage.notification!!.title
            getString(R.string.app_name)
            firebasePushResponse.body = remoteMessage.notification!!.body
            Log.e(
                "title1",
                "title" + firebasePushResponse.title.toString() + "description :" + firebasePushResponse.body + remoteMessage.notification
            )
            // sendNotification(firebasePushResponse);
            sendNotification(firebasePushResponse)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    var NOTIFICATION_ID: Int = 234

    private fun sendNotification(messageBody: FirebasePushResponse) {
        val CHANNEL_ID = "my_channel_01"
        val name: CharSequence = "my_channel"
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("type",messageBody.type)
        intent.putExtra("user_id","test")
        intent.putExtra("item_id",messageBody.item_id)
        //val intent11 = Intent()
        ////intent11.putExtra("type",3)
        intent.action = "com.community"
       sendBroadcast(intent)
       val pendingIntent = PendingIntent.getActivity(
            this, 0   , intent,
            0
        )

        // val channelId = getString(CHANNEL_ID)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_logo)
            .setContentTitle(messageBody.title)
            .setContentText(messageBody.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}