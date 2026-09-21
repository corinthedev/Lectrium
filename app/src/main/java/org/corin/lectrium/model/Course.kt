// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CourseModality(val label: String) {
    FACE_TO_FACE("Face-to-Face"),
    ASYNCHRONOUS("Asynchronous"),
    HYBRID("Hybrid")
}

enum class CourseClassification(val label: String) {
    LECTURE("Lecture"),
    LABORATORY("Laboratory")
}

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey
    val id: String,
    val code: String,
    val title: String,
    val section: String = "",
    val units: Int = 3,
    val faculty: String = "",
    val room: String = "",
    val colorHex: String = "38BDF8",
    val modality: CourseModality = CourseModality.FACE_TO_FACE,
    val classification: CourseClassification = CourseClassification.LECTURE
)
