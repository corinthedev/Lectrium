// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

sealed interface ReminderTiming {
    val label: String
    val durationMinutes: Int

    data object Off : ReminderTiming {
        override val label: String = "Off"
        override val durationMinutes: Int = -1
    }

    data class Preset(
        override val label: String,
        override val durationMinutes: Int
    ) : ReminderTiming

    data class Custom(
        val customReminder: CustomReminder
    ) : ReminderTiming {
        override val label: String = "Custom (${customReminder.displayLabel})"
        override val durationMinutes: Int = customReminder.totalMinutes
    }

    data object CustomPlaceholder : ReminderTiming {
        override val label: String = "Custom Duration..."
        override val durationMinutes: Int = 0
    }

    companion object {
        val presets: List<ReminderTiming> = listOf(
            Off,
            Preset("5 Minutes", 5),
            Preset("10 Minutes", 10),
            Preset("15 Minutes", 15),
            Preset("30 Minutes", 30),
            Preset("1 Hour", 60)
        )

        val dialogOptions: List<ReminderTiming> = presets + CustomPlaceholder

        fun fromLabel(
            label: String,
            customValue: Int = 1,
            customUnit: TimeUnit = TimeUnit.MINUTES
        ): ReminderTiming {
            if (label.equals("Off", ignoreCase = true)) return Off
            presets.firstOrNull { it.label.equals(label, ignoreCase = true) }?.let { return it }
            if (label.startsWith("Custom", ignoreCase = true)) {
                return Custom(CustomReminder(customValue, customUnit))
            }
            return Off
        }
    }
}