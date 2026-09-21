// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import org.corin.lectrium.LectriumApp
import org.corin.lectrium.R

class AcademicReminderReceiver : BroadcastReceiver() {

    @Suppress("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Lectrium Reminder"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "You have an upcoming academic task."
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID) ?: CHANNEL_CLASS
        val itemId = intent.getStringExtra(EXTRA_ITEM_ID) ?: "0"

        createNotificationChannels(context)

        val openAppIntent = Intent(context, LectriumApp::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            itemId.hashCode(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notifications_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            notificationManager.notify(itemId.hashCode(), builder.build())
        } catch (_: SecurityException) {
            // Suppress if notification permission is revoked
        }
    }

    private fun createNotificationChannels(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channels = listOf(
            NotificationChannel(
                CHANNEL_CLASS,
                "Class Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for upcoming class sessions"
            },
            NotificationChannel(
                CHANNEL_ASSIGNMENTS,
                "Assignment Deadlines",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for assignment due dates"
            },
            NotificationChannel(
                CHANNEL_EXAMS,
                "Exam Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for upcoming exams"
            },
            NotificationChannel(
                CHANNEL_PROJECTS,
                "Project Deadlines",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for project milestone deadlines"
            }
        )

        channels.forEach { notificationManager.createNotificationChannel(it) }
    }

    companion object {
        const val ACTION_CLASS_REMINDER = "org.corin.lectrium.ACTION_CLASS_REMINDER"
        const val ACTION_ASSIGNMENT_REMINDER = "org.corin.lectrium.ACTION_ASSIGNMENT_REMINDER"
        const val ACTION_EXAM_REMINDER = "org.corin.lectrium.ACTION_EXAM_REMINDER"
        const val ACTION_PROJECT_REMINDER = "org.corin.lectrium.ACTION_PROJECT_REMINDER"

        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
        const val EXTRA_ITEM_ID = "extra_item_id"

        const val CHANNEL_CLASS = "channel_class_reminders"
        const val CHANNEL_ASSIGNMENTS = "channel_assignment_reminders"
        const val CHANNEL_EXAMS = "channel_exam_reminders"
        const val CHANNEL_PROJECTS = "channel_project_reminders"
    }
}
