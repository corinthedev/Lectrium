// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class AssignmentStatus(val label: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}

enum class PriorityLevel(val label: String) {
    HIGH("High Priority"),
    MEDIUM("Medium Priority"),
    LOW("Low Priority")
}

@Entity(
    tableName = "assignments",
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
data class Assignment(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val title: String,
    val description: String = "",
    val dueTimestamp: Long, // Epoch milliseconds
    val status: AssignmentStatus = AssignmentStatus.PENDING,
    val priority: PriorityLevel = PriorityLevel.MEDIUM
)
