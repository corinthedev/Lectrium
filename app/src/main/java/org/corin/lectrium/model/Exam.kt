// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exams",
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
data class Exam(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val title: String,
    val examTimestamp: Long, // Epoch milliseconds
    val durationMinutes: Int = 90,
    val room: String = "",
    val weightPercentage: Double = 0.0,
    val topicsScopeJson: String = "[]" // JSON representation of List<String>
)
