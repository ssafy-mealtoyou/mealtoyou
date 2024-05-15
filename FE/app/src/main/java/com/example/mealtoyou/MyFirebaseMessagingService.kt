package com.example.mealtoyou;

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log;
import androidx.core.app.NotificationCompat
import com.example.mealtoyou.handler.FcmEventHandler


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("token is here", token)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("testMessage")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Log.d("Message Firebase", "Message Notification Body: ${it.body}")
//            sendNotification(it.title ?: "제목 없음", it.body ?: "내용 없음")
//        }
        super.onMessageReceived(message)

        val intentA = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntentA = PendingIntent.getActivity(this, 0, intentA, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val intentB = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntentB = PendingIntent.getActivity(this, 0, intentB, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // 서버에서 보낸 데이터 확인
//        val clickAction = message.data["click_action"]
//        val extraInformation = message.data["extra_information"]

        // 사용자 정의 알림 생성
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // 버튼 클릭 시 실행할 AnotherActivity를 위한 Intent 생성
        val actionIntent = Intent(this, MainActivity::class.java)
        val actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.default_notification_channel_id)
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.zzang)
            setContentTitle(message.notification?.title)
            setContentText(message.notification?.body)
            setAutoCancel(true)
            setContentIntent(pendingIntentA)
            addAction(R.drawable.zzang, "액션 A", pendingIntentA) // 버튼 A 추가
            addAction(R.drawable.zzang, "액션 B", pendingIntentB) // 버튼 B 추가
            // 추가적으로 버튼을 여기에 설정할 수 있습니다.
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendNotification(title: String, messageBody: String) {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // 버튼 클릭 시 실행할 AnotherActivity를 위한 Intent 생성
        val actionIntent = Intent(this, MainActivity::class.java)
        val actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.zzang)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(mainPendingIntent)
            // 버튼 추가
            .addAction(R.drawable.zzang, "확인", actionPendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android O(API 26) 이상에서는 알림 채널이 필요
        val channel = NotificationChannel(
            channelId,
            "Channel human readable title",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())
    }
}