// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(
    tableName = "class_schedules",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("courseId")]
)
data class ClassSchedule(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val dayOfWeek: DayOfWeek,
    val startTime: String, // e.g. "08:30" (24h format)
    val endTime: String,   // e.g. "10:00"
    val roomOverride: String = "",
    val modalityOverride: CourseModality? = null
)
