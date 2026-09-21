// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import org.corin.lectrium.notifications.NotificationScheduler
import org.corin.lectrium.preferences.ClassReminderData
import org.corin.lectrium.preferences.SchedulePreferences
import org.corin.lectrium.preferences.ScheduleViewMode
import org.corin.lectrium.repository.AcademicRepository
import org.corin.lectrium.screens.CourseAttendanceData
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalTime
import java.util.UUID

data class FreeTimeGap(
    val dayOfWeek: DayOfWeek,
    val startTime: String,
    val endTime: String,
    val durationMinutes: Long,
    val previousCourseCode: String,
    val nextCourseCode: String
)

data class ScheduleConflict(
    val dayOfWeek: DayOfWeek,
    val course1Code: String,
    val course2Code: String,
    val timeSlot: String
)

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AcademicRepository(application)
    private val schedulePreferences = SchedulePreferences(application)
    private val reminderData = ClassReminderData(application)
    private val notificationScheduler = NotificationScheduler(application)

    val coursesState: StateFlow<List<Course>> = repository.coursesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val schedulesState: StateFlow<List<ClassSchedule>> = repository.schedulesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val viewModeState: StateFlow<ScheduleViewMode> = schedulePreferences.scheduleViewModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScheduleViewMode.LIST)

    val showSaturdayState: StateFlow<Boolean> = schedulePreferences.showSaturdayFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val showSundayState: StateFlow<Boolean> = schedulePreferences.showSundayFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val enableGapCheckerState: StateFlow<Boolean> = schedulePreferences.enableGapCheckerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val enableAutoMuteState: StateFlow<Boolean> = schedulePreferences.enableAutoMuteFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val attendanceCoursesState: StateFlow<List<CourseAttendanceData>> =
        combine(coursesState, schedulesState) { courses, _ ->
            courses.map { course ->
                CourseAttendanceData(
                    code = course.code,
                    title = course.title,
                    units = course.units,
                    sessionHours = 1.5f,
                    missedHours = 0f
                )
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val freeGapsState: StateFlow<List<FreeTimeGap>> =
        combine(schedulesState, coursesState) { schedules, courses ->
            calculateFreeGaps(schedules, courses)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val conflictsState: StateFlow<List<ScheduleConflict>> =
        combine(schedulesState, coursesState) { schedules, courses ->
            calculateConflicts(schedules, courses)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setViewMode(mode: ScheduleViewMode) {
        viewModelScope.launch { schedulePreferences.saveScheduleViewMode(mode) }
    }

    fun setShowSaturday(show: Boolean) {
        viewModelScope.launch { schedulePreferences.saveShowSaturday(show) }
    }

    fun setShowSunday(show: Boolean) {
        viewModelScope.launch { schedulePreferences.saveShowSunday(show) }
    }

    fun setEnableGapChecker(enable: Boolean) {
        viewModelScope.launch { schedulePreferences.saveEnableGapChecker(enable) }
    }

    fun setEnableAutoMute(enable: Boolean) {
        viewModelScope.launch { schedulePreferences.saveEnableAutoMute(enable) }
    }

    fun addCourseWithSchedules(course: Course, schedules: List<ClassSchedule>) {
        viewModelScope.launch {
            val courseId = if (course.id.isBlank()) UUID.randomUUID().toString() else course.id
            val updatedCourse = course.copy(id = courseId)

            val updatedSchedules = schedules.map { schedule ->
                schedule.copy(
                    id = if (schedule.id.isBlank()) UUID.randomUUID().toString() else schedule.id,
                    courseId = courseId
                )
            }

            repository.saveCourseWithSchedules(updatedCourse, updatedSchedules)

            // Schedule Notifications
            val timing = reminderData.classReminderTimingFlow.first()
            val offsetMins = timing.durationMinutes
            if (offsetMins > 0) {
                updatedSchedules.forEach { sched ->
                    notificationScheduler.scheduleClassReminder(sched, updatedCourse, offsetMins)
                }
            }
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            repository.deleteCourse(course.id)
        }
    }

    private fun calculateFreeGaps(
        schedules: List<ClassSchedule>,
        courses: List<Course>
    ): List<FreeTimeGap> {
        val courseMap = courses.associateBy { it.id }
        val gaps = mutableListOf<FreeTimeGap>()

        val groupedByDay = schedules.groupBy { it.dayOfWeek }

        for ((day, daySchedules) in groupedByDay) {
            val sorted = daySchedules.mapNotNull { sched ->
                val start = parseTime(sched.startTime)
                val end = parseTime(sched.endTime)
                if (start != null && end != null) Triple(sched, start, end) else null
            }.sortedBy { it.second }

            for (i in 0 until sorted.size - 1) {
                val current = sorted[i]
                val next = sorted[i + 1]

                val currentEnd = current.third
                val nextStart = next.second

                if (nextStart.isAfter(currentEnd)) {
                    val gapMins = Duration.between(currentEnd, nextStart).toMinutes()
                    if (gapMins >= 30) {
                        val c1 = courseMap[current.first.courseId]?.code ?: "Class"
                        val c2 = courseMap[next.first.courseId]?.code ?: "Class"
                        gaps.add(
                            FreeTimeGap(
                                dayOfWeek = day,
                                startTime = current.first.endTime,
                                endTime = next.first.startTime,
                                durationMinutes = gapMins,
                                previousCourseCode = c1,
                                nextCourseCode = c2
                            )
                        )
                    }
                }
            }
        }
        return gaps
    }

    private fun calculateConflicts(
        schedules: List<ClassSchedule>,
        courses: List<Course>
    ): List<ScheduleConflict> {
        val courseMap = courses.associateBy { it.id }
        val conflicts = mutableListOf<ScheduleConflict>()

        val groupedByDay = schedules.groupBy { it.dayOfWeek }

        for ((day, daySchedules) in groupedByDay) {
            for (i in daySchedules.indices) {
                for (j in i + 1 until daySchedules.size) {
                    val s1 = daySchedules[i]
                    val s2 = daySchedules[j]

                    val start1 = parseTime(s1.startTime)
                    val end1 = parseTime(s1.endTime)
                    val start2 = parseTime(s2.startTime)
                    val end2 = parseTime(s2.endTime)

                    if (start1 != null && end1 != null && start2 != null && end2 != null) {
                        if (start1.isBefore(end2) && start2.isBefore(end1)) {
                            val c1 = courseMap[s1.courseId]?.code ?: "Class A"
                            val c2 = courseMap[s2.courseId]?.code ?: "Class B"
                            conflicts.add(
                                ScheduleConflict(
                                    dayOfWeek = day,
                                    course1Code = c1,
                                    course2Code = c2,
                                    timeSlot = "${s1.startTime}-${s1.endTime}"
                                )
                            )
                        }
                    }
                }
            }
        }
        return conflicts
    }

    private fun parseTime(timeStr: String): LocalTime? {
        return try {
            val parts = timeStr.split(":").map { it.toInt() }
            LocalTime.of(parts[0], parts[1])
        } catch (_: Exception) {
            null
        }
    }
}
