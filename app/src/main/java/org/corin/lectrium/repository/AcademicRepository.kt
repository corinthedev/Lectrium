// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.corin.lectrium.model.Assignment
import org.corin.lectrium.model.AssignmentStatus
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.CourseClassification
import org.corin.lectrium.model.CourseModality
import org.corin.lectrium.model.Exam
import org.corin.lectrium.model.PriorityLevel
import org.corin.lectrium.model.Project
import org.json.JSONArray
import org.json.JSONObject
import java.time.DayOfWeek

private val Context.academicDataStore by preferencesDataStore(name = "academic_repository_store")

class AcademicRepository(private val context: Context) {
    private object Keys {
        val COURSES_JSON = stringPreferencesKey("courses_json")
        val SCHEDULES_JSON = stringPreferencesKey("schedules_json")
        val ASSIGNMENTS_JSON = stringPreferencesKey("assignments_json")
        val PROJECTS_JSON = stringPreferencesKey("projects_json")
        val EXAMS_JSON = stringPreferencesKey("exams_json")
    }

    val coursesFlow: Flow<List<Course>> = context.academicDataStore.data.map { preferences ->
        val json = preferences[Keys.COURSES_JSON] ?: "[]"
        parseCourses(json)
    }

    val schedulesFlow: Flow<List<ClassSchedule>> =
        context.academicDataStore.data.map { preferences ->
            val json = preferences[Keys.SCHEDULES_JSON] ?: "[]"
            parseSchedules(json)
        }

    val assignmentsFlow: Flow<List<Assignment>> =
        context.academicDataStore.data.map { preferences ->
            val json = preferences[Keys.ASSIGNMENTS_JSON] ?: "[]"
            parseAssignments(json)
        }

    val projectsFlow: Flow<List<Project>> = context.academicDataStore.data.map { preferences ->
        val json = preferences[Keys.PROJECTS_JSON] ?: "[]"
        parseProjects(json)
    }

    val examsFlow: Flow<List<Exam>> = context.academicDataStore.data.map { preferences ->
        val json = preferences[Keys.EXAMS_JSON] ?: "[]"
        parseExams(json)
    }

    suspend fun saveCourseWithSchedules(course: Course, schedules: List<ClassSchedule>) {
        context.academicDataStore.edit { preferences ->
            val currentCourses =
                parseCourses(preferences[Keys.COURSES_JSON] ?: "[]").toMutableList()
            currentCourses.removeAll { it.id == course.id }
            currentCourses.add(course)
            preferences[Keys.COURSES_JSON] = coursesToJson(currentCourses)

            val currentSchedules =
                parseSchedules(preferences[Keys.SCHEDULES_JSON] ?: "[]").toMutableList()
            currentSchedules.removeAll { it.courseId == course.id }
            currentSchedules.addAll(schedules)
            preferences[Keys.SCHEDULES_JSON] = schedulesToJson(currentSchedules)
        }
    }

    suspend fun deleteCourse(courseId: String) {
        context.academicDataStore.edit { preferences ->
            val currentCourses =
                parseCourses(preferences[Keys.COURSES_JSON] ?: "[]").toMutableList()
            currentCourses.removeAll { it.id == courseId }
            preferences[Keys.COURSES_JSON] = coursesToJson(currentCourses)

            val currentSchedules =
                parseSchedules(preferences[Keys.SCHEDULES_JSON] ?: "[]").toMutableList()
            currentSchedules.removeAll { it.courseId == courseId }
            preferences[Keys.SCHEDULES_JSON] = schedulesToJson(currentSchedules)
        }
    }

    suspend fun saveAssignment(assignment: Assignment) {
        context.academicDataStore.edit { preferences ->
            val current =
                parseAssignments(preferences[Keys.ASSIGNMENTS_JSON] ?: "[]").toMutableList()
            current.removeAll { it.id == assignment.id }
            current.add(assignment)
            preferences[Keys.ASSIGNMENTS_JSON] = assignmentsToJson(current)
        }
    }

    suspend fun deleteAssignment(assignmentId: String) {
        context.academicDataStore.edit { preferences ->
            val current =
                parseAssignments(preferences[Keys.ASSIGNMENTS_JSON] ?: "[]").toMutableList()
            current.removeAll { it.id == assignmentId }
            preferences[Keys.ASSIGNMENTS_JSON] = assignmentsToJson(current)
        }
    }

    suspend fun saveProject(project: Project) {
        context.academicDataStore.edit { preferences ->
            val current = parseProjects(preferences[Keys.PROJECTS_JSON] ?: "[]").toMutableList()
            current.removeAll { it.id == project.id }
            current.add(project)
            preferences[Keys.PROJECTS_JSON] = projectsToJson(current)
        }
    }

    suspend fun deleteProject(projectId: String) {
        context.academicDataStore.edit { preferences ->
            val current = parseProjects(preferences[Keys.PROJECTS_JSON] ?: "[]").toMutableList()
            current.removeAll { it.id == projectId }
            preferences[Keys.PROJECTS_JSON] = projectsToJson(current)
        }
    }

    suspend fun saveExam(exam: Exam) {
        context.academicDataStore.edit { preferences ->
            val current = parseExams(preferences[Keys.EXAMS_JSON] ?: "[]").toMutableList()
            current.removeAll { it.id == exam.id }
            current.add(exam)
            preferences[Keys.EXAMS_JSON] = examsToJson(current)
        }
    }

    suspend fun deleteExam(examId: String) {
        context.academicDataStore.edit { preferences ->
            val current = parseExams(preferences[Keys.EXAMS_JSON] ?: "[]").toMutableList()
            current.removeAll { it.id == examId }
            preferences[Keys.EXAMS_JSON] = examsToJson(current)
        }
    }

    suspend fun clearAllData() {
        context.academicDataStore.edit { preferences ->
            preferences.clear()
        }
    }

    // JSON Serializers and Deserializers
    private fun parseCourses(jsonStr: String): List<Course> {
        val list = mutableListOf<Course>()
        return try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Course(
                        id = obj.optString("id"),
                        code = obj.optString("code"),
                        title = obj.optString("title"),
                        section = obj.optString("section"),
                        units = obj.optInt("units", 3),
                        faculty = obj.optString("faculty"),
                        room = obj.optString("room"),
                        colorHex = obj.optString("colorHex", "38BDF8"),
                        modality = runCatching { CourseModality.valueOf(obj.optString("modality")) }.getOrDefault(
                            CourseModality.FACE_TO_FACE
                        ),
                        classification = runCatching { CourseClassification.valueOf(obj.optString("classification")) }.getOrDefault(
                            CourseClassification.LECTURE
                        )
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun coursesToJson(courses: List<Course>): String {
        val array = JSONArray()
        courses.forEach { course ->
            val obj = JSONObject().apply {
                put("id", course.id)
                put("code", course.code)
                put("title", course.title)
                put("section", course.section)
                put("units", course.units)
                put("faculty", course.faculty)
                put("room", course.room)
                put("colorHex", course.colorHex)
                put("modality", course.modality.name)
                put("classification", course.classification.name)
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun parseSchedules(jsonStr: String): List<ClassSchedule> {
        val list = mutableListOf<ClassSchedule>()
        return try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val modalityStr = obj.optString("modalityOverride")
                val modality =
                    if (modalityStr.isNotBlank()) runCatching { CourseModality.valueOf(modalityStr) }.getOrNull() else null

                list.add(
                    ClassSchedule(
                        id = obj.optString("id"),
                        courseId = obj.optString("courseId"),
                        dayOfWeek = runCatching { DayOfWeek.valueOf(obj.optString("dayOfWeek")) }.getOrDefault(
                            DayOfWeek.MONDAY
                        ),
                        startTime = obj.optString("startTime"),
                        endTime = obj.optString("endTime"),
                        roomOverride = obj.optString("roomOverride"),
                        modalityOverride = modality
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun schedulesToJson(schedules: List<ClassSchedule>): String {
        val array = JSONArray()
        schedules.forEach { schedule ->
            val obj = JSONObject().apply {
                put("id", schedule.id)
                put("courseId", schedule.courseId)
                put("dayOfWeek", schedule.dayOfWeek.name)
                put("startTime", schedule.startTime)
                put("endTime", schedule.endTime)
                put("roomOverride", schedule.roomOverride)
                put("modalityOverride", schedule.modalityOverride?.name ?: "")
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun parseAssignments(jsonStr: String): List<Assignment> {
        val list = mutableListOf<Assignment>()
        return try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Assignment(
                        id = obj.optString("id"),
                        courseId = obj.optString("courseId"),
                        title = obj.optString("title"),
                        description = obj.optString("description"),
                        dueTimestamp = obj.optLong("dueTimestamp"),
                        status = runCatching { AssignmentStatus.valueOf(obj.optString("status")) }.getOrDefault(
                            AssignmentStatus.PENDING
                        ),
                        priority = runCatching { PriorityLevel.valueOf(obj.optString("priority")) }.getOrDefault(
                            PriorityLevel.MEDIUM
                        )
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun assignmentsToJson(assignments: List<Assignment>): String {
        val array = JSONArray()
        assignments.forEach { a ->
            val obj = JSONObject().apply {
                put("id", a.id)
                put("courseId", a.courseId)
                put("title", a.title)
                put("description", a.description)
                put("dueTimestamp", a.dueTimestamp)
                put("status", a.status.name)
                put("priority", a.priority.name)
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun parseProjects(jsonStr: String): List<Project> {
        val list = mutableListOf<Project>()
        return try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Project(
                        id = obj.optString("id"),
                        courseId = obj.optString("courseId"),
                        title = obj.optString("title"),
                        description = obj.optString("description"),
                        deadlineTimestamp = obj.optLong("deadlineTimestamp"),
                        teamMembers = obj.optString("teamMembers"),
                        isCompleted = obj.optBoolean("isCompleted"),
                        milestonesJson = obj.optString("milestonesJson", "[]")
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun projectsToJson(projects: List<Project>): String {
        val array = JSONArray()
        projects.forEach { p ->
            val obj = JSONObject().apply {
                put("id", p.id)
                put("courseId", p.courseId)
                put("title", p.title)
                put("description", p.description)
                put("deadlineTimestamp", p.deadlineTimestamp)
                put("teamMembers", p.teamMembers)
                put("isCompleted", p.isCompleted)
                put("milestonesJson", p.milestonesJson)
            }
            array.put(obj)
        }
        return array.toString()
    }

    private fun parseExams(jsonStr: String): List<Exam> {
        val list = mutableListOf<Exam>()
        return try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    Exam(
                        id = obj.optString("id"),
                        courseId = obj.optString("courseId"),
                        title = obj.optString("title"),
                        examTimestamp = obj.optLong("examTimestamp"),
                        durationMinutes = obj.optInt("durationMinutes", 90),
                        room = obj.optString("room"),
                        weightPercentage = obj.optDouble("weightPercentage", 0.0),
                        topicsScopeJson = obj.optString("topicsScopeJson", "[]")
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun examsToJson(exams: List<Exam>): String {
        val array = JSONArray()
        exams.forEach { e ->
            val obj = JSONObject().apply {
                put("id", e.id)
                put("courseId", e.courseId)
                put("title", e.title)
                put("examTimestamp", e.examTimestamp)
                put("durationMinutes", e.durationMinutes)
                put("room", e.room)
                put("weightPercentage", e.weightPercentage)
                put("topicsScopeJson", e.topicsScopeJson)
            }
            array.put(obj)
        }
        return array.toString()
    }
}
