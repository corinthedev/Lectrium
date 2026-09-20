// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.corin.lectrium.preferences.ClassReminderData
import org.corin.lectrium.model.NotificationType
import org.corin.lectrium.model.ReminderTiming

class ClassReminderViewModel(private val data: ClassReminderData): ViewModel() {
    val masterNotificationsEnabled: StateFlow<Boolean> = data.masterNotificationsEnabledFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val enabledNotificationTypes: StateFlow<Set<NotificationType>> = data.enabledNotificationTypesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationType.ALL
        )

    val classReminderTiming: StateFlow<ReminderTiming> = data.classReminderTimingFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReminderTiming.Off
        )

    fun onMasterNotificationsToggled(enabled: Boolean) {
        viewModelScope.launch {
            data.saveMasterNotificationsEnabled(enabled)
        }
    }

    fun onNotificationTypeToggled(type: NotificationType, enabled: Boolean) {
        viewModelScope.launch {
            val current = enabledNotificationTypes.value.toMutableSet()
            if (enabled) {
                current.add(type)
            } else {
                current.remove(type)
            }
            data.saveEnabledNotificationTypes(current)
        }
    }

    fun onReminderTimingSelected(newTiming: ReminderTiming) {
        viewModelScope.launch {
            data.saveClassReminderTiming(newTiming)
        }
    }
}
