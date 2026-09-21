// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.corin.lectrium.model.Assignment
import org.corin.lectrium.model.AssignmentStatus
import org.corin.lectrium.notifications.NotificationScheduler
import org.corin.lectrium.repository.AcademicRepository
import java.util.UUID

class AssignmentsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AcademicRepository(application)
    private val notificationScheduler = NotificationScheduler(application)

    val assignmentsState: StateFlow<List<Assignment>> = repository.assignmentsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addAssignment(assignment: Assignment, courseCode: String) {
        viewModelScope.launch {
            val assignmentId =
                if (assignment.id.isBlank()) UUID.randomUUID().toString() else assignment.id
            val newAssignment = assignment.copy(id = assignmentId)
            repository.saveAssignment(newAssignment)
            notificationScheduler.scheduleAssignmentReminder(newAssignment, courseCode)
        }
    }

    fun toggleAssignmentStatus(assignment: Assignment) {
        viewModelScope.launch {
            val newStatus = when (assignment.status) {
                AssignmentStatus.COMPLETED -> AssignmentStatus.PENDING
                else -> AssignmentStatus.COMPLETED
            }
            repository.saveAssignment(assignment.copy(status = newStatus))
        }
    }

    fun updateAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.saveAssignment(assignment)
        }
    }

    fun deleteAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.deleteAssignment(assignment.id)
        }
    }
}
