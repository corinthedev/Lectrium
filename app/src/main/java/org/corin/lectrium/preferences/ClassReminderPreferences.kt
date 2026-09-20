// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.corin.lectrium.model.NotificationType
import org.corin.lectrium.model.ReminderTiming
import org.corin.lectrium.model.TimeUnit

private val Context.dataStore by preferencesDataStore(name = "class_reminder_timing_setting")

class ClassReminderData(private val context: Context) {
    private object PreferenceKeys {
        val CLASS_REMINDER_TIMING = stringPreferencesKey("class_reminder_timing")
        val CLASS_REMINDER_CUSTOM_VALUE = intPreferencesKey("class_reminder_custom_value")
        val CLASS_REMINDER_CUSTOM_UNIT = stringPreferencesKey("class_reminder_custom_unit")
        val MASTER_NOTIFICATIONS_ENABLED = booleanPreferencesKey("master_notifications_enabled")
        val ENABLED_NOTIFICATION_TYPES = stringSetPreferencesKey("enabled_notification_types")
    }

    val masterNotificationsEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.MASTER_NOTIFICATIONS_ENABLED] ?: true
        }

    val enabledNotificationTypesFlow: Flow<Set<NotificationType>> = context.dataStore.data
        .map { preferences ->
            val defaultSet = NotificationType.ALL.map { it.id }.toSet()
            val savedSet = preferences[PreferenceKeys.ENABLED_NOTIFICATION_TYPES] ?: defaultSet
            savedSet.mapNotNull { NotificationType.fromId(it) }.toSet()
        }

    val classReminderTimingFlow: Flow<ReminderTiming> = context.dataStore.data
        .map { preferences ->
            val savedLabel = preferences[PreferenceKeys.CLASS_REMINDER_TIMING] ?: "Off"
            val customValue = preferences[PreferenceKeys.CLASS_REMINDER_CUSTOM_VALUE] ?: 2
            val customUnitStr = preferences[PreferenceKeys.CLASS_REMINDER_CUSTOM_UNIT] ?: TimeUnit.MINUTES.name
            val customUnit = try {
                TimeUnit.valueOf(customUnitStr)
            } catch (_: Exception) {
                TimeUnit.MINUTES
            }

            ReminderTiming.fromLabel(
                label = savedLabel,
                customValue = customValue,
                customUnit = customUnit
            )
        }

    suspend fun saveMasterNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.MASTER_NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun saveEnabledNotificationTypes(types: Set<NotificationType>) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.ENABLED_NOTIFICATION_TYPES] = types.map { it.id }.toSet()
        }
    }

    suspend fun saveClassReminderTiming(timing: ReminderTiming) {
        context.dataStore.edit { preferences ->
            when (timing) {
                is ReminderTiming.Custom -> {
                    preferences[PreferenceKeys.CLASS_REMINDER_TIMING] = "Custom"
                    preferences[PreferenceKeys.CLASS_REMINDER_CUSTOM_VALUE] = timing.customReminder.value
                    preferences[PreferenceKeys.CLASS_REMINDER_CUSTOM_UNIT] = timing.customReminder.unit.name
                }
                else -> {
                    preferences[PreferenceKeys.CLASS_REMINDER_TIMING] = timing.label
                }
            }
        }
    }
}
