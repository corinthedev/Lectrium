// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.corin.lectrium.preferences.ClassReminderData

class ClassReminderReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {
        val ctx = context ?: return
        if (intent == null) return

        val reminderData = ClassReminderData(ctx)
        val masterEnabled = runBlocking { reminderData.masterNotificationsEnabledFlow.first() }
        if (!masterEnabled) {
            Log.d("ClassReminderReceiver", "Master notifications are OFF. Notification suppressed.")
            return
        }

        val courseName = intent.getStringExtra("courseName")
        val courseCode = intent.getStringExtra("courseCode")
        val courseRoom = intent.getStringExtra("courseRoom")

        if (courseName.isNullOrEmpty() || courseCode.isNullOrEmpty() || courseRoom.isNullOrEmpty()) {
            Log.w("ClassReminderReceiver", "Missing required intent extras!")
            return
        }

        val notificationChannelId = "class_reminders"
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            "Class Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Class Reminder Notifications"
        }
        val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val classReminderNotification = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle("$courseName ($courseCode) - Room $courseRoom")
            .setContentText("The class is starting soon! Arrive before the class starts!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(ctx).notify(System.currentTimeMillis().toInt(), classReminderNotification)
    }
}