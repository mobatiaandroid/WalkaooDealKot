package com.mobatia.vkcsalesapp.fcm

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFCMID
import com.mobatia.vkcsalesapp.ui.activity.SplashActivity
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Created by Bibin Johnson
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    var intent: Intent? = null
    var bitmap: Bitmap? = null
    var mType: String? = null

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            // Log.d(TAG, "Message data payload: " + remoteMessage.getData().toString());
//            String questionTitle = data.get("questionTitle").toString();
            try {
                val json = JSONObject(remoteMessage.data.toString().replace("=".toRegex(), ":"))
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }

        // Check if message contains a notification payload.
        /* if (remoteMessage.getNotification() != null) {
//            sendNotification(remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/
    }

    @TargetApi(26)
    private fun sendNotification(messageBody: String) {
        val Number = Random()
        val Rnumber = Number.nextInt(100)
        // if (mType.equals("Text")) {
        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(messageBody) /*
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap))*/
            // .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
        if (Build.VERSION.SDK_INT >= 23) {
//            notificationBuilder.setSmallIcon(R.drawable.notifyicons);
            notificationBuilder.color = resources.getColor(R.color.vkcred)
            //    notificationBuilder.setSmallIcon(R.drawable.not_large);
            notificationBuilder.setLargeIcon(largeIcon)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_launcher)
            //            notificationBuilder.setSmallIcon(R.drawable.notifyicons);
//            notificationBuilder.setColor(getResources().getColor(R.color.tictapHeader));
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = getString(R.string.app_name) + "_01" // The id of the channel.
            val name: CharSequence =
                getString(R.string.app_name) // The user-visible name of the channel.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationBuilder.setChannelId(mChannel.id)
            mChannel.setShowBadge(true)
            mChannel.canShowBadge()
            mChannel.enableLights(true)
            mChannel.lightColor = resources.getColor(R.color.vkcred)
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(mChannel)
        }
        notificationManager.notify(Rnumber, notificationBuilder.build())
    }

    private fun handleDataMessage(json: JSONObject) {
        Log.e(TAG, "push json: $json")
        try {
            val data = json.getJSONObject("body")
            val message = data.optString("description")
            // String title = data.optString("title");
            // String image = data.optString("image");
            // if (image.length() > 0) {
            //     bitmap = getBitmapfromUrl(image);
            //  }
            sendNotification(message)
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        saveFCMID(applicationContext, s)
    }
    companion object {
        private const val TAG = "MyFirebaseMsgService"

    }
}