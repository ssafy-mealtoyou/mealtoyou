package com.example.mealtoyou

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mealtoyou.handler.SupplementEventHandler

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // API 호출 로직

        val extraInformation = intent.getStringExtra("extraInformation")
        if (extraInformation != null) {
            SupplementEventHandler().sendSupplementTaken(extraInformation)
        }
        // 알림 끄기 로직
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (extraInformation != null) {
            notificationManager.cancel(extraInformation.toInt())
        }
    }
}