// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import org.corin.lectrium.model.ColorSchemeMode
import org.corin.lectrium.model.ThemeMode

private val SkyBlueDarkColorScheme = darkColorScheme(
    primary = SkyBluePrimaryDark,
    onPrimary = Color(0xFF003554),
    primaryContainer = SkyBluePrimaryContainerDark,
    onPrimaryContainer = SkyBlueOnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = Color(0xFF1E293B),
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = Color(0xFFE2E8F0),
    tertiary = TertiaryDark,
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = Color(0xFFFEF3C7),
    error = ErrorDark,
    onError = Color(0xFF450A0A),
    errorContainer = ErrorContainerDark,
    onErrorContainer = Color(0xFFFEE2E2),
    surface = SurfaceDark,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val SkyBlueLightColorScheme = lightColorScheme(
    primary = SkyBluePrimaryLight,
    onPrimary = Color.White,
    primaryContainer = SkyBluePrimaryContainerLight,
    onPrimaryContainer = SkyBlueOnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = Color(0xFF0F172A),
    tertiary = TertiaryLight,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = Color(0xFF451A03),
    error = ErrorLight,
    onError = Color.White,
    errorContainer = ErrorContainerLight,
    onErrorContainer = Color(0xFF450A0A),
    surface = SurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF475569)
)

fun buildCustomColorScheme(hexString: String, darkTheme: Boolean): ColorScheme {
    val clean = hexString.removePrefix("#")
    val colorInt = runCatching {
        if (clean.length == 6) {
            android.graphics.Color.parseColor("#FF$clean")
        } else if (clean.length == 8) {
            android.graphics.Color.parseColor("#$clean")
        } else {
            android.graphics.Color.parseColor("#FF38BDF8")
        }
    }.getOrDefault(android.graphics.Color.parseColor("#FF38BDF8"))

    val seedColor = Color(colorInt)
    val hsv = FloatArray(3)
    android.graphics.Color.colorToHSV(colorInt, hsv)

    return if (darkTheme) {
        val primaryContainer = Color(
            android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], (hsv[1] * 0.8f).coerceAtMost(1f), 0.35f))
        )
        val onPrimaryContainer = Color(
            android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], 0.15f, 0.95f))
        )
        darkColorScheme(
            primary = seedColor,
            onPrimary = if (seedColor.luminance() > 0.6f) Color.Black else Color.White,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = SecondaryDark,
            onSecondary = Color(0xFF1E293B),
            secondaryContainer = SecondaryContainerDark,
            onSecondaryContainer = Color(0xFFE2E8F0),
            tertiary = TertiaryDark,
            onTertiary = Color(0xFF451A03),
            tertiaryContainer = TertiaryContainerDark,
            onTertiaryContainer = Color(0xFFFEF3C7),
            error = ErrorDark,
            onError = Color(0xFF450A0A),
            errorContainer = ErrorContainerDark,
            onErrorContainer = Color(0xFFFEE2E2),
            surface = SurfaceDark,
            onSurface = Color(0xFFF1F5F9),
            surfaceVariant = SurfaceVariantDark,
            onSurfaceVariant = Color(0xFFCBD5E1)
        )
    } else {
        val primary = Color(
            android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], (hsv[1] * 1.1f).coerceAtMost(1f), (hsv[2] * 0.75f).coerceAtLeast(0.2f)))
        )
        val primaryContainer = Color(
            android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], 0.15f, 0.96f))
        )
        val onPrimaryContainer = Color(
            android.graphics.Color.HSVToColor(floatArrayOf(hsv[0], 0.9f, 0.4f))
        )
        lightColorScheme(
            primary = primary,
            onPrimary = Color.White,
            primaryContainer = primaryContainer,
            onPrimaryContainer = onPrimaryContainer,
            secondary = SecondaryLight,
            onSecondary = Color.White,
            secondaryContainer = SecondaryContainerLight,
            onSecondaryContainer = Color(0xFF0F172A),
            tertiary = TertiaryLight,
            onTertiary = Color.White,
            tertiaryContainer = TertiaryContainerLight,
            onTertiaryContainer = Color(0xFF451A03),
            error = ErrorLight,
            onError = Color.White,
            errorContainer = ErrorContainerLight,
            onErrorContainer = Color(0xFF450A0A),
            surface = SurfaceLight,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = SurfaceVariantLight,
            onSurfaceVariant = Color(0xFF475569)
        )
    }
}

@Composable
fun LectriumTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    colorSchemeMode: ColorSchemeMode = ColorSchemeMode.DEFAULT,
    customColorHex: String = "38BDF8",
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val context = LocalContext.current
    val colorScheme = when {
        colorSchemeMode == ColorSchemeMode.DYNAMIC && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        colorSchemeMode == ColorSchemeMode.CUSTOM -> {
            buildCustomColorScheme(customColorHex, darkTheme)
        }
        else -> {
            if (darkTheme) SkyBlueDarkColorScheme else SkyBlueLightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
