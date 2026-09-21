// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.corin.lectrium.components.schedule.CalendarGridView
import org.corin.lectrium.model.Assignment
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.Exam
import org.corin.lectrium.model.Project
import org.corin.lectrium.preferences.ScheduleViewMode
import org.corin.lectrium.viewmodel.FreeTimeGap
import java.time.LocalDate

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    courses: List<Course> = emptyList(),
    schedules: List<ClassSchedule> = emptyList(),
    assignments: List<Assignment> = emptyList(),
    exams: List<Exam> = emptyList(),
    projects: List<Project> = emptyList(),
    viewMode: ScheduleViewMode = ScheduleViewMode.LIST,
    showSaturday: Boolean = false,
    showSunday: Boolean = false,
    freeGaps: List<FreeTimeGap> = emptyList(),
    onViewModeChange: (ScheduleViewMode) -> Unit = {},
    onToggleSaturday: (Boolean) -> Unit = {},
    onToggleSunday: (Boolean) -> Unit = {},
    onAddCourse: (Course, List<ClassSchedule>) -> Unit = { _, _ -> }
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Schedules",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // View Mode Segmented Control
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                ScheduleViewMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        selected = mode == viewMode,
                        onClick = { onViewModeChange(mode) },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ScheduleViewMode.entries.size
                        )
                    ) {
                        Text(
                            text = mode.label,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Free Time Gap Banner
            if (freeGaps.isNotEmpty()) {
                val gap = freeGaps.first()
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏳ Free Time Gap: ${gap.durationMinutes} mins between ${gap.previousCourseCode} and ${gap.nextCourseCode} on ${
                                gap.dayOfWeek.name.take(
                                    3
                                )
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Dynamic Schedule View Content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (viewMode) {
                    ScheduleViewMode.LIST -> {
                        DayScheduleScreen(
                            selectedDate = LocalDate.now(),
                            courses = courses,
                            schedules = schedules
                        )
                    }

                    ScheduleViewMode.DAILY -> {
                        DayScheduleScreen(
                            selectedDate = LocalDate.now(),
                            courses = courses,
                            schedules = schedules
                        )
                    }

                    ScheduleViewMode.WEEKLY -> {
                        WeeklyScheduleScreen(
                            courses = courses,
                            schedules = schedules,
                            showSaturday = showSaturday,
                            showSunday = showSunday,
                            onToggleSaturday = onToggleSaturday,
                            onToggleSunday = onToggleSunday
                        )
                    }

                    ScheduleViewMode.CALENDAR_GRID -> {
                        CalendarGridView(
                            courses = courses,
                            schedules = schedules,
                            assignments = assignments,
                            exams = exams,
                            projects = projects
                        )
                    }
                }
            }
        }
    }
}
