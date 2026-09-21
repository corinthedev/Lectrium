// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.corin.lectrium.model.Exam
import org.corin.lectrium.notifications.NotificationScheduler
import org.corin.lectrium.repository.AcademicRepository
import java.util.UUID

class ExamsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AcademicRepository(application)
    private val notificationScheduler = NotificationScheduler(application)

    val examsState: StateFlow<List<Exam>> = repository.examsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addExam(exam: Exam, courseCode: String) {
        viewModelScope.launch {
            val id = if (exam.id.isBlank()) UUID.randomUUID().toString() else exam.id
            val newExam = exam.copy(id = id)
            repository.saveExam(newExam)
            notificationScheduler.scheduleExamReminder(newExam, courseCode)
        }
    }

    fun updateExam(exam: Exam) {
        viewModelScope.launch {
            repository.saveExam(exam)
        }
    }

    fun deleteExam(exam: Exam) {
        viewModelScope.launch {
            repository.deleteExam(exam.id)
        }
    }
}
