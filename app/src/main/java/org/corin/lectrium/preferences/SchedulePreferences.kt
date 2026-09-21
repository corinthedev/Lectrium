// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ScheduleViewMode(val label: String) {
    LIST("List"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    CALENDAR_GRID("Calendar")
}

private val Context.scheduleDataStore by preferencesDataStore(name = "schedule_preferences")

class SchedulePreferences(private val context: Context) {
    private object Keys {
        val VIEW_MODE = stringPreferencesKey("schedule_view_mode")
        val SHOW_SATURDAY = booleanPreferencesKey("show_saturday")
        val SHOW_SUNDAY = booleanPreferencesKey("show_sunday")
        val ENABLE_GAP_CHECKER = booleanPreferencesKey("enable_gap_checker")
        val ENABLE_AUTO_MUTE = booleanPreferencesKey("enable_auto_mute")
    }

    val scheduleViewModeFlow: Flow<ScheduleViewMode> =
        context.scheduleDataStore.data.map { preferences ->
            val savedName = preferences[Keys.VIEW_MODE] ?: ScheduleViewMode.LIST.name
            runCatching { ScheduleViewMode.valueOf(savedName) }.getOrDefault(ScheduleViewMode.LIST)
        }

    val showSaturdayFlow: Flow<Boolean> = context.scheduleDataStore.data.map { preferences ->
        preferences[Keys.SHOW_SATURDAY] ?: false
    }

    val showSundayFlow: Flow<Boolean> = context.scheduleDataStore.data.map { preferences ->
        preferences[Keys.SHOW_SUNDAY] ?: false
    }

    val enableGapCheckerFlow: Flow<Boolean> = context.scheduleDataStore.data.map { preferences ->
        preferences[Keys.ENABLE_GAP_CHECKER] ?: true
    }

    val enableAutoMuteFlow: Flow<Boolean> = context.scheduleDataStore.data.map { preferences ->
        preferences[Keys.ENABLE_AUTO_MUTE] ?: true
    }

    suspend fun saveScheduleViewMode(mode: ScheduleViewMode) {
        context.scheduleDataStore.edit { preferences ->
            preferences[Keys.VIEW_MODE] = mode.name
        }
    }

    suspend fun saveShowSaturday(show: Boolean) {
        context.scheduleDataStore.edit { preferences ->
            preferences[Keys.SHOW_SATURDAY] = show
        }
    }

    suspend fun saveShowSunday(show: Boolean) {
        context.scheduleDataStore.edit { preferences ->
            preferences[Keys.SHOW_SUNDAY] = show
        }
    }

    suspend fun saveEnableGapChecker(enable: Boolean) {
        context.scheduleDataStore.edit { preferences ->
            preferences[Keys.ENABLE_GAP_CHECKER] = enable
        }
    }

    suspend fun saveEnableAutoMute(enable: Boolean) {
        context.scheduleDataStore.edit { preferences ->
            preferences[Keys.ENABLE_AUTO_MUTE] = enable
        }
    }
}
