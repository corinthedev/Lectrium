// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

import androidx.annotation.DrawableRes
import org.corin.lectrium.R

enum class ScreenDestinations(
    val route: String,
    val routeLabel: String,
    val routeDesc: String,
    @get:DrawableRes val routeIcon: Int
) {
    HOME("home", "Home", "Home Dashboard", R.drawable.home_icon),
    SCHEDULES("schedules", "Schedules", "Class Schedules", R.drawable.dock_icon),
    TASKS("tasks", "Tasks", "Assignments & Projects", R.drawable.bar_chart_icon),
    EXAMS("exams", "Exams", "Exams & Quizzes", R.drawable.alarm_icon),
    ATTENDANCE("attendance", "Attendance", "Attendance Record", R.drawable.attendance_icon),
    SETTINGS("settings", "Settings", "App Preferences", R.drawable.settings_icon)
}
