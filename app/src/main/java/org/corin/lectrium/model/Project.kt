// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

data class ProjectMilestone(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false
)

@Entity(
    tableName = "projects",
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
data class Project(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val title: String,
    val description: String = "",
    val deadlineTimestamp: Long, // Epoch milliseconds
    val teamMembers: String = "", // Comma-separated or description
    val isCompleted: Boolean = false,
    val milestonesJson: String = "[]" // JSON representation of List<ProjectMilestone>
)
