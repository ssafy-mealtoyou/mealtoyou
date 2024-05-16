package com.example.mealtoyou;

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log;
import androidx.core.app.NotificationCompat


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Check if message contains a notification payload.
        remoteMessage.data.let {
            Log.d("Message Firebase", "Message Notification Body: $it")
            sendNotification(it)
        }
    }

    private fun sendNotification(message: MutableMap<String, String>) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // 버튼 클릭 시 실행할 AnotherActivity를 위한 Intent 생성
        val actionIntent = Intent(this, NotificationReceiver::class.java)
        actionIntent.putExtra("extraInformation", message["extraInformation"])
        val actionPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            actionIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.zzang)
            .setContentTitle(message["title"])
            .setContentText(message["body"])
//            .setAutoCancel(true) //알림 누르면 꺼지게
            .setSound(defaultSoundUri)
            .setContentIntent(mainPendingIntent)

        var notificationId=0;
        if (message.containsKey("extraInformation")) {
            //영양제면 버튼 추가
            notificationBuilder.addAction(R.drawable.zzang, "확인", actionPendingIntent)
            notificationId = message["extraInformation"]?.toInt()!!
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android O(API 26) 이상에서는 알림 채널이 필요
        val channel = NotificationChannel(
            channelId,
            "알림 채널 입니다",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(
            notificationId,
            notificationBuilder.build()
        )
    }
}