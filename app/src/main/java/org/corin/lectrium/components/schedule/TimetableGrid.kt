// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import java.time.DayOfWeek

@Composable
fun TimetableGrid(
    modifier: Modifier = Modifier,
    courses: List<Course>,
    schedules: List<ClassSchedule>,
    showSaturday: Boolean = false,
    showSunday: Boolean = false
) {
    val visibleDays = mutableListOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY
    )
    if (showSaturday) visibleDays.add(DayOfWeek.SATURDAY)
    if (showSunday) visibleDays.add(DayOfWeek.SUNDAY)

    val hours = (7..20).toList() // 7 AM to 8 PM
    val courseMap = courses.associateBy { it.id }
    val hourRowHeight = 60.dp

    Column(modifier = modifier.fillMaxSize()) {
        // Day Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(vertical = 8.dp)
        ) {
            Box(modifier = Modifier.width(48.dp)) // Hour label offset space
            visibleDays.forEach { day ->
                Text(
                    text = day.name.take(3),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        // Timetable Grid Body
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hour Column
            Column(modifier = Modifier.width(48.dp)) {
                hours.forEach { hour ->
                    Box(
                        modifier = Modifier
                            .height(hourRowHeight)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = String.format("%02d:00", hour),
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            // Day Columns with Class Blocks
            Row(modifier = Modifier.weight(1f)) {
                visibleDays.forEach { day ->
                    val daySchedules = schedules.filter { it.dayOfWeek == day }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        // Background Grid Lines
                        Column {
                            hours.forEach { _ ->
                                Box(
                                    modifier = Modifier
                                        .height(hourRowHeight)
                                        .fillMaxWidth()
                                        .border(
                                            0.5.dp,
                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }

                        // Schedule Cards
                        daySchedules.forEach { schedule ->
                            val course = courseMap[schedule.courseId]
                            val (startMins, durationMins) = parseScheduleTime(
                                schedule.startTime,
                                schedule.endTime
                            )

                            if (startMins >= 7 * 60 && durationMins > 0) {
                                val topOffset =
                                    ((startMins - 7 * 60) / 60f * hourRowHeight.value).dp
                                val blockHeight = (durationMins / 60f * hourRowHeight.value).dp

                                val courseColor = parseColorHex(course?.colorHex ?: "38BDF8")

                                Surface(
                                    modifier = Modifier
                                        .padding(start = 2.dp, end = 2.dp, top = topOffset)
                                        .height(blockHeight)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp)),
                                    color = courseColor.copy(alpha = 0.25f),
                                    border = BorderStroke(1.dp, courseColor)
                                ) {
                                    Column(modifier = Modifier.padding(4.dp)) {
                                        Text(
                                            text = course?.code ?: "Class",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        if (blockHeight >= 40.dp) {
                                            Text(
                                                text = schedule.roomOverride.ifEmpty {
                                                    course?.room ?: ""
                                                },
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 9.sp
                                                ),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun parseScheduleTime(startTime: String, endTime: String): Pair<Int, Int> {
    return try {
        val startParts = startTime.split(":").map { it.toInt() }
        val endParts = endTime.split(":").map { it.toInt() }
        val startMins = startParts[0] * 60 + startParts[1]
        val endMins = endParts[0] * 60 + endParts[1]
        Pair(startMins, endMins - startMins)
    } catch (_: Exception) {
        Pair(0, 0)
    }
}

private fun parseColorHex(hex: String): Color {
    return try {
        val cleaned = hex.removePrefix("#")
        val colorInt = cleaned.toLong(16) or 0xFF000000
        Color(colorInt)
    } catch (_: Exception) {
        Color(0xFF38BDF8)
    }
}
