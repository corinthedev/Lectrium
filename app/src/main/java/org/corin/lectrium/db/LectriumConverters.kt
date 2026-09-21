// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.db

import androidx.room.TypeConverter
import org.corin.lectrium.model.AssignmentStatus
import org.corin.lectrium.model.CourseClassification
import org.corin.lectrium.model.CourseModality
import org.corin.lectrium.model.PriorityLevel
import java.time.DayOfWeek

class LectriumConverters {
    @TypeConverter
    fun fromDayOfWeek(day: DayOfWeek?): String? = day?.name

    @TypeConverter
    fun toDayOfWeek(value: String?): DayOfWeek? = value?.let {
        try {
            DayOfWeek.valueOf(it)
        } catch (_: Exception) {
            DayOfWeek.MONDAY
        }
    }

    @TypeConverter
    fun fromModality(modality: CourseModality?): String? = modality?.name

    @TypeConverter
    fun toModality(value: String?): CourseModality? = value?.let {
        try {
            CourseModality.valueOf(it)
        } catch (_: Exception) {
            CourseModality.FACE_TO_FACE
        }
    }

    @TypeConverter
    fun fromClassification(classification: CourseClassification?): String? = classification?.name

    @TypeConverter
    fun toClassification(value: String?): CourseClassification? = value?.let {
        try {
            CourseClassification.valueOf(it)
        } catch (_: Exception) {
            CourseClassification.LECTURE
        }
    }

    @TypeConverter
    fun fromAssignmentStatus(status: AssignmentStatus?): String? = status?.name

    @TypeConverter
    fun toAssignmentStatus(value: String?): AssignmentStatus? = value?.let {
        try {
            AssignmentStatus.valueOf(it)
        } catch (_: Exception) {
            AssignmentStatus.PENDING
        }
    }

    @TypeConverter
    fun fromPriorityLevel(priority: PriorityLevel?): String? = priority?.name

    @TypeConverter
    fun toPriorityLevel(value: String?): PriorityLevel? = value?.let {
        try {
            PriorityLevel.valueOf(it)
        } catch (_: Exception) {
            PriorityLevel.MEDIUM
        }
    }
}
