// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

data class CustomReminder(
    val value: Int = 1,
    val unit: TimeUnit = TimeUnit.MINUTES
) {
    val totalMinutes: Int
        get() = unit.toMinutes(value)

    val displayLabel: String
        get() {
            val unitStr = if (value == 1) {
                when (unit) {
                    TimeUnit.MINUTES -> "Minute"
                    TimeUnit.HOURS -> "Hour"
                    TimeUnit.DAYS -> "Day"
                }
            } else {
                unit.unitLabel
            }
            return "$value $unitStr"
        }

    companion object {
        const val DEFAULT_MAX_MINUTES: Int = 90 // 1 hour 30 minutes max limit default

        fun isValid(value: Int, unit: TimeUnit, maxMinutes: Int = DEFAULT_MAX_MINUTES): Boolean {
            val total = unit.toMinutes(value)
            return value > 0 && total <= maxMinutes
        }

        fun formatMinutes(minutes: Int): String {
            if (minutes < 60) return "$minutes Minutes"
            val hrs = minutes / 60
            val mins = minutes % 60
            val hrStr = if (hrs == 1) "1 Hour" else "$hrs Hours"
            return if (mins == 0) hrStr else "$hrStr $mins Minutes"
        }
    }
}