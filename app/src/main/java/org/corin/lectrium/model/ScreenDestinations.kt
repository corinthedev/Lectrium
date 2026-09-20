// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import org.corin.lectrium.R

enum class ScreenDestinations(val route: String, val routeLabel: String, val routeDesc: String, @get:DrawableRes val routeIcon: Int) {
    HOME("home", "Home", "The Application's Home Screen", R.drawable.home_icon),
    ATTENDANCE("attendance", "Attendance", "Check your Attendance Record!", R.drawable.attendance_icon),
    SETTINGS("settings", "Settings", "The App Preferences Screen", R.drawable.settings_icon)
}