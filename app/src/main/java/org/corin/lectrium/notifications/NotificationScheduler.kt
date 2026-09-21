// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import org.corin.lectrium.model.Assignment
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.Exam
import org.corin.lectrium.model.Project
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class NotificationScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleClassReminder(schedule: ClassSchedule, course: Course, offsetMinutes: Int) {
        if (offsetMinutes <= 0) return

        val targetDateTime = calculateNextOccurrence(schedule.dayOfWeek, schedule.startTime)
            .minusMinutes(offsetMinutes.toLong())

        val triggerTimeMs = targetDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (triggerTimeMs <= System.currentTimeMillis()) return

        val intent = Intent(context, AcademicReminderReceiver::class.java).apply {
            action = AcademicReminderReceiver.ACTION_CLASS_REMINDER
            putExtra(AcademicReminderReceiver.EXTRA_TITLE, "Upcoming Class: ${course.code}")
            putExtra(
                AcademicReminderReceiver.EXTRA_MESSAGE,
                "${course.title} starting in $offsetMinutes mins at ${schedule.startTime} (${schedule.roomOverride.ifEmpty { course.room }})"
            )
            putExtra(
                AcademicReminderReceiver.EXTRA_CHANNEL_ID,
                AcademicReminderReceiver.CHANNEL_CLASS
            )
            putExtra(AcademicReminderReceiver.EXTRA_ITEM_ID, schedule.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        scheduleAlarm(triggerTimeMs, pendingIntent)
    }

    fun scheduleAssignmentReminder(assignment: Assignment, courseCode: String) {
        val dueMs = assignment.dueTimestamp
        val triggerTime24h = dueMs - (24 * 60 * 60 * 1000L)
        val triggerTime2h = dueMs - (2 * 60 * 60 * 1000L)

        if (triggerTime24h > System.currentTimeMillis()) {
            val intent = Intent(context, AcademicReminderReceiver::class.java).apply {
                action = AcademicReminderReceiver.ACTION_ASSIGNMENT_REMINDER
                putExtra(
                    AcademicReminderReceiver.EXTRA_TITLE,
                    "Assignment Due Tomorrow: $courseCode"
                )
                putExtra(AcademicReminderReceiver.EXTRA_MESSAGE, assignment.title)
                putExtra(
                    AcademicReminderReceiver.EXTRA_CHANNEL_ID,
                    AcademicReminderReceiver.CHANNEL_ASSIGNMENTS
                )
                putExtra(AcademicReminderReceiver.EXTRA_ITEM_ID, assignment.id + "_24h")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                (assignment.id + "_24h").hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            scheduleAlarm(triggerTime24h, pendingIntent)
        }

        if (triggerTime2h > System.currentTimeMillis()) {
            val intent = Intent(context, AcademicReminderReceiver::class.java).apply {
                action = AcademicReminderReceiver.ACTION_ASSIGNMENT_REMINDER
                putExtra(AcademicReminderReceiver.EXTRA_TITLE, "Assignment Due in 2 Hours!")
                putExtra(
                    AcademicReminderReceiver.EXTRA_MESSAGE,
                    "$courseCode — ${assignment.title}"
                )
                putExtra(
                    AcademicReminderReceiver.EXTRA_CHANNEL_ID,
                    AcademicReminderReceiver.CHANNEL_ASSIGNMENTS
                )
                putExtra(AcademicReminderReceiver.EXTRA_ITEM_ID, assignment.id + "_2h")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                (assignment.id + "_2h").hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            scheduleAlarm(triggerTime2h, pendingIntent)
        }
    }

    fun scheduleExamReminder(exam: Exam, courseCode: String) {
        val examMs = exam.examTimestamp
        val triggerTime3d = examMs - (3 * 24 * 60 * 60 * 1000L)
        val triggerTime1d = examMs - (24 * 60 * 60 * 1000L)

        if (triggerTime3d > System.currentTimeMillis()) {
            val intent = Intent(context, AcademicReminderReceiver::class.java).apply {
                action = AcademicReminderReceiver.ACTION_EXAM_REMINDER
                putExtra(AcademicReminderReceiver.EXTRA_TITLE, "Upcoming Exam in 3 Days!")
                putExtra(AcademicReminderReceiver.EXTRA_MESSAGE, "$courseCode — ${exam.title}")
                putExtra(
                    AcademicReminderReceiver.EXTRA_CHANNEL_ID,
                    AcademicReminderReceiver.CHANNEL_EXAMS
                )
                putExtra(AcademicReminderReceiver.EXTRA_ITEM_ID, exam.id + "_3d")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                (exam.id + "_3d").hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            scheduleAlarm(triggerTime3d, pendingIntent)
        }

        if (triggerTime1d > System.currentTimeMillis()) {
            val intent = Intent(context, AcademicReminderReceiver::class.java).apply {
                action = AcademicReminderReceiver.ACTION_EXAM_REMINDER
                putExtra(AcademicReminderReceiver.EXTRA_TITLE, "Exam Tomorrow!")
                putExtra(
                    AcademicReminderReceiver.EXTRA_MESSAGE,
                    "$courseCode — ${exam.title} at ${exam.room}"
                )
                putExtra(
                    AcademicReminderReceiver.EXTRA_CHANNEL_ID,
                    AcademicReminderReceiver.CHANNEL_EXAMS
                )
                putExtra(AcademicReminderReceiver.EXTRA_ITEM_ID, exam.id + "_1d")
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                (exam.id + "_1d").hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            scheduleAlarm(triggerTime1d, pendingIntent)
        }
    }

    fun scheduleProjectReminder(project: Project, courseCode: String) {
        val deadlineMs = project.deadlineTimestamp
        val triggerTime2d = deadlineMs - (2 * 24 * 60 * 60 * 1000L)

        if (triggerTime2d > System.currentTimeMillis()) {
            val intent = Intent(context, AcademicReminderReceiver::class.java).apply {
                action = AcademicReminderReceiver.ACTION_PROJECT_REMINDER
                putExtra(AcademicReminderReceiver.EXTRA_TITLE, "Project Deadline in 2 Days!")
                putExtra(AcademicReminderReceiver.EXTRA_MESSAGE, "$courseCode — ${project.title}")
                putExtra(
                    AcademicReminderReceiver.EXTRA_CHANNEL_ID,
                    AcademicReminderReceiver.CHANNEL_PROJECTS
                )
                putExtra(AcademicReminderReceiver.EXTRA_ITEM_ID, project.id)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                project.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            scheduleAlarm(triggerTime2d, pendingIntent)
        }
    }

    private fun scheduleAlarm(triggerTimeMs: Long, pendingIntent: PendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMs,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMs,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMs,
                pendingIntent
            )
        }
    }

    private fun calculateNextOccurrence(dayOfWeek: DayOfWeek, startTimeStr: String): LocalDateTime {
        val parts = startTimeStr.split(":").mapNotNull { it.toIntOrNull() }
        val hour = if (parts.size >= 2) parts[0] else 8
        val minute = if (parts.size >= 2) parts[1] else 0

        val today = LocalDate.now()
        var targetDate = today.with(TemporalAdjusters.nextOrSame(dayOfWeek))
        var targetDateTime = LocalDateTime.of(targetDate, LocalTime.of(hour, minute))

        if (targetDateTime.isBefore(LocalDateTime.now())) {
            targetDate = today.plusWeeks(1).with(TemporalAdjusters.nextOrSame(dayOfWeek))
            targetDateTime = LocalDateTime.of(targetDate, LocalTime.of(hour, minute))
        }

        return targetDateTime
    }
}
