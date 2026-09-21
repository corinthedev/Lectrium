// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.corin.lectrium.R
import org.corin.lectrium.components.userprofile.UserInitials
import org.corin.lectrium.model.Assignment
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.Exam
import org.corin.lectrium.model.Project
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String = "Student",
    userProfileImage: String? = null,
    courses: List<Course> = emptyList(),
    schedules: List<ClassSchedule> = emptyList(),
    assignments: List<Assignment> = emptyList(),
    exams: List<Exam> = emptyList(),
    projects: List<Project> = emptyList(),
    onSeeAllUpcoming: () -> Unit = {},
    onNavigateToAttendance: () -> Unit = {}
) {
    val today = LocalDate.now()
    val formattedDate = remember(today) {
        today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
    }

    val dynamicGreetings = remember(userName) {
        val hour = LocalTime.now().hour
        val timeOfDayGreeting = when (hour) {
            in 5..11 -> "Good morning"
            in 12..17 -> "Good afternoon"
            else -> "Good evening"
        }
        val nameToUse = userName.ifBlank { "Alex" }.split(" ").firstOrNull() ?: "Alex"
        listOf(
            "$timeOfDayGreeting, $nameToUse",
            "Welcome back, $nameToUse",
            "Ready to excel, $nameToUse",
            "Let's conquer today, $nameToUse",
            "Stay ahead, $nameToUse"
        ).random()
    }

    val todaySchedules = remember(schedules, today) {
        schedules.filter { it.dayOfWeek == today.dayOfWeek }
            .sortedBy { parseTimeMins(it.startTime) }
    }
    val courseMap = remember(courses) { courses.associateBy { it.id } }

    val nextClassTuple = remember(todaySchedules) {
        val nowMins = LocalTime.now().hour * 60 + LocalTime.now().minute
        todaySchedules.firstOrNull { parseTimeMins(it.endTime) > nowMins }
    }

    val upcomingItems = remember(exams, assignments, projects, courseMap) {
        val list = mutableListOf<UpcomingDisplayItem>()
        exams.forEach { e ->
            val courseCode = courseMap[e.courseId]?.code ?: "Exam"
            list.add(
                UpcomingDisplayItem(
                    "${e.title} — $courseCode",
                    formatDueText(e.examTimestamp),
                    e.examTimestamp
                )
            )
        }
        assignments.forEach { a ->
            val courseCode = courseMap[a.courseId]?.code ?: "Assignment"
            list.add(
                UpcomingDisplayItem(
                    "${a.title} — $courseCode",
                    formatDueText(a.dueTimestamp),
                    a.dueTimestamp
                )
            )
        }
        projects.forEach { p ->
            val courseCode = courseMap[p.courseId]?.code ?: "Project"
            list.add(
                UpcomingDisplayItem(
                    "${p.title} — $courseCode",
                    formatDueText(p.deadlineTimestamp),
                    p.deadlineTimestamp
                )
            )
        }
        list.sortedBy { it.timestamp }.take(3)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Avatar/Photo + Dynamic Greeting + Date
            item(key = "header") {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val file = userProfileImage?.let { File(it) }
                    if (file != null && file.exists()) {
                        val bitmap = remember(userProfileImage) {
                            BitmapFactory.decodeFile(file.absolutePath)
                        }
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            UserInitials(
                                username = userName.ifBlank { "Alex" },
                                profileSize = 48.dp
                            )
                        }
                    } else {
                        UserInitials(
                            username = userName.ifBlank { "Alex" },
                            profileSize = 48.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = dynamicGreetings,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Next Class Hero Card
            item(key = "next_class_hero") {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Next class",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (nextClassTuple != null) {
                                val nowMins = LocalTime.now().hour * 60 + LocalTime.now().minute
                                val startMins = parseTimeMins(nextClassTuple.startTime)
                                val diffMins = startMins - nowMins

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                                ) {
                                    Text(
                                        text = if (diffMins in 1..60) "In $diffMins min" else "At ${nextClassTuple.startTime}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (nextClassTuple != null) {
                            val course = courseMap[nextClassTuple.courseId]
                            Text(
                                text = "${course?.code ?: "Class"} — ${course?.title ?: ""}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.alarm_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${nextClassTuple.startTime} – ${nextClassTuple.endTime}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(R.drawable.home_icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = nextClassTuple.roomOverride.ifEmpty {
                                            course?.room ?: "Room N/A"
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "No remaining classes scheduled for today!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Today Section
            item(key = "today_section_header") {
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (todaySchedules.isNotEmpty()) {
                items(todaySchedules) { schedule ->
                    val course = courseMap[schedule.courseId]
                    val dotColor = parseColorHex(course?.colorHex ?: "10B981")

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(dotColor)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "${course?.code ?: "Class"} ${course?.title ?: ""}".trim(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = schedule.roomOverride.ifEmpty {
                                            course?.room ?: "Venue N/A"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Text(
                                text = schedule.startTime,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                item(key = "today_empty") {
                    Text(
                        text = "No classes scheduled for today.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Upcoming Section
            item(key = "upcoming_section_header") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upcoming",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "See all >",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onSeeAllUpcoming() }
                    )
                }
            }

            if (upcomingItems.isNotEmpty()) {
                items(upcomingItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.bar_chart_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.dueText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                item(key = "upcoming_empty") {
                    Text(
                        text = "No upcoming assignments or exams.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Attendance Goal Progress Card
            if (courses.isNotEmpty() || schedules.isNotEmpty()) {
                item(key = "attendance_goal_card") {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToAttendance() },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.size(52.dp)
                                ) {
                                    CircularProgressIndicator(
                                        progress = { 0.92f },
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                    )
                                    Text(
                                        text = "92%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        text = "Attendance goal",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "You're on track this quarter",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Icon(
                                painter = painterResource(R.drawable.chevron_right_icon),
                                contentDescription = "Attendance details",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item(key = "bottom_space") {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private data class UpcomingDisplayItem(
    val title: String,
    val dueText: String,
    val timestamp: Long
)

private fun parseTimeMins(timeStr: String): Int {
    return try {
        val parts = timeStr.split(":").map { it.toInt() }
        parts[0] * 60 + parts[1]
    } catch (_: Exception) {
        0
    }
}

private fun formatDueText(timestamp: Long): String {
    val diffMs = timestamp - System.currentTimeMillis()
    val diffDays = (diffMs / (24 * 60 * 60 * 1000L)).toInt()
    return when {
        diffDays <= 0 -> "Due today"
        diffDays == 1 -> "Due tomorrow"
        diffDays in 2..6 -> "Due in $diffDays days"
        else -> {
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            "Due ${sdf.format(Date(timestamp))}"
        }
    }
}

private fun parseColorHex(hex: String): Color {
    return try {
        val colorInt = hex.removePrefix("#").toLong(16) or 0xFF000000
        Color(colorInt)
    } catch (_: Exception) {
        Color(0xFF10B981)
    }
}
