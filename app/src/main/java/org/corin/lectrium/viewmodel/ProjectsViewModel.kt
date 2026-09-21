// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.corin.lectrium.model.Project
import org.corin.lectrium.notifications.NotificationScheduler
import org.corin.lectrium.repository.AcademicRepository
import java.util.UUID

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AcademicRepository(application)
    private val notificationScheduler = NotificationScheduler(application)

    val projectsState: StateFlow<List<Project>> = repository.projectsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addProject(project: Project, courseCode: String) {
        viewModelScope.launch {
            val id = if (project.id.isBlank()) UUID.randomUUID().toString() else project.id
            val newProject = project.copy(id = id)
            repository.saveProject(newProject)
            notificationScheduler.scheduleProjectReminder(newProject, courseCode)
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch {
            repository.saveProject(project)
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            repository.deleteProject(project.id)
        }
    }
}
