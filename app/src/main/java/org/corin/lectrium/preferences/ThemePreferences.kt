// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.corin.lectrium.model.ColorSchemeMode
import org.corin.lectrium.model.ThemeMode

private val Context.themeDataStore by preferencesDataStore(name = "theme_preferences")

class ThemePreferences(private val context: Context) {
    private object PreferenceKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val COLOR_SCHEME_MODE = stringPreferencesKey("color_scheme_mode")
        val CUSTOM_COLOR_HEX = stringPreferencesKey("custom_color_hex")
    }

    val themeModeFlow: Flow<ThemeMode> = context.themeDataStore.data
        .map { preferences ->
            val value = preferences[PreferenceKeys.THEME_MODE]
            if (value != null) {
                runCatching { ThemeMode.valueOf(value) }.getOrDefault(ThemeMode.SYSTEM)
            } else {
                ThemeMode.SYSTEM
            }
        }

    val colorSchemeModeFlow: Flow<ColorSchemeMode> = context.themeDataStore.data
        .map { preferences ->
            val value = preferences[PreferenceKeys.COLOR_SCHEME_MODE]
            if (value != null) {
                runCatching { ColorSchemeMode.valueOf(value) }.getOrDefault(ColorSchemeMode.DEFAULT)
            } else {
                ColorSchemeMode.DEFAULT
            }
        }

    val customColorHexFlow: Flow<String> = context.themeDataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.CUSTOM_COLOR_HEX] ?: "38BDF8"
        }

    suspend fun saveThemeMode(mode: ThemeMode) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferenceKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun saveColorSchemeMode(mode: ColorSchemeMode) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferenceKeys.COLOR_SCHEME_MODE] = mode.name
        }
    }

    suspend fun saveCustomColorHex(hex: String) {
        context.themeDataStore.edit { preferences ->
            preferences[PreferenceKeys.CUSTOM_COLOR_HEX] = hex
        }
    }
}
