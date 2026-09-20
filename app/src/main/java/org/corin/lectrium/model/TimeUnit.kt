// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

enum class TimeUnit(val unitLabel: String, val minuteMulti: Int) {
    MINUTES("Minutes", 1),
    HOURS("Hours", 60),
    DAYS("Days", 1440);

    fun toMinutes(value: Int): Int = value * minuteMulti
}