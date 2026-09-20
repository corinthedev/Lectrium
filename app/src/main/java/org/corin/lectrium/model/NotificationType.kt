// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

enum class NotificationType(
    val id: String,
    val title: String,
    val description: String
) {
    CLASS_REMINDERS(
        id = "class_reminders",
        title = "Class Reminders",
        description = "Class reminder alerts before schedule"
    ),
    ASSIGNMENT_DEADLINES(
        id = "assignment_deadlines",
        title = "Assignment Deadlines",
        description = "Notifications for upcoming assignment due dates"
    ),
    EXAM_ALERTS(
        id = "exam_alerts",
        title = "Exam Alerts",
        description = "Reminders for scheduled exams"
    ),
    SCHEDULE_OVERRIDES(
        id = "schedule_overrides",
        title = "Schedule Overrides",
        description = "Alerts for makeup classes and room changes"
    );

    companion object {
        val ALL: Set<NotificationType> = entries.toSet()

        fun fromId(id: String): NotificationType? {
            return entries.firstOrNull { it.id == id }
        }
    }
}