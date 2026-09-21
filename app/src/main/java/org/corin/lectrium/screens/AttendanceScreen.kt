// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.corin.lectrium.R
import java.util.Locale

data class CourseAttendanceData(
    val code: String,
    val title: String,
    val units: Int = 3,
    val sessionHours: Float = 1.5f,
    var missedHours: Float = 0f
) {
    val totalHours: Float
        get() = units * 18f

    val maxAbsenceHours: Float
        get() = totalHours * 0.20f

    val attendancePercentage: Int
        get() {
            val attended = (totalHours - missedHours).coerceAtLeast(0f)
            return ((attended / totalHours) * 100f).toInt().coerceIn(0, 100)
        }

    val remainingAbsenceHours: Float
        get() = (maxAbsenceHours - missedHours).coerceAtLeast(0f)

    val status: String
        get() = when {
            missedHours > maxAbsenceHours -> "Advised to Withdraw"
            remainingAbsenceHours <= sessionHours -> "Critical"
            remainingAbsenceHours <= sessionHours * 2 -> "Warning"
            else -> "Safe"
        }
}

@Composable
fun AttendanceScreen(
    modifier: Modifier = Modifier,
    courses: List<CourseAttendanceData> = emptyList(),
    onUpdateMissedHours: (code: String, deltaHours: Float) -> Unit = { _, _ -> }
) {
    val totalCourses = courses.size
    val averagePercentage = if (totalCourses > 0) courses.map { it.attendancePercentage }.average().toInt() else 100
    val totalMissed = courses.sumOf { it.missedHours.toDouble() }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "header_title") {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Attendance Tracker",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Monitor your course attendance and absence limits",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (courses.isNotEmpty()) {
                item(key = "summary_card") {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Overall Attendance",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "$averagePercentage%",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Total Missed",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = "${String.format(Locale.getDefault(), "%.1f", totalMissed)} hrs",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                items(courses, key = { it.code }) { course ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${course.code} — ${course.title}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${course.units} Unit${if (course.units > 1) "s" else ""}  •  Max Absences: ${String.format(Locale.getDefault(), "%.1f", course.maxAbsenceHours)} hrs",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = when (course.status) {
                                        "Safe" -> MaterialTheme.colorScheme.primaryContainer
                                        "Warning" -> MaterialTheme.colorScheme.tertiaryContainer
                                        else -> MaterialTheme.colorScheme.errorContainer
                                    }
                                ) {
                                    Text(
                                        text = course.status,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = when (course.status) {
                                            "Safe" -> MaterialTheme.colorScheme.onPrimaryContainer
                                            "Warning" -> MaterialTheme.colorScheme.onTertiaryContainer
                                            else -> MaterialTheme.colorScheme.onErrorContainer
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Attendance Rate: ${course.attendancePercentage}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Missed: ${String.format(Locale.getDefault(), "%.1f", course.missedHours)} / ${String.format(Locale.getDefault(), "%.1f", course.maxAbsenceHours)} hrs",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { (course.missedHours / course.maxAbsenceHours).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = when (course.status) {
                                    "Safe" -> MaterialTheme.colorScheme.primary
                                    "Warning" -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.error
                                },
                                trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = if (course.remainingAbsenceHours > 0f) {
                                    "Allowance left: ${String.format(Locale.getDefault(), "%.1f", course.remainingAbsenceHours)} hrs (~${(course.remainingAbsenceHours / course.sessionHours).toInt()} classes)"
                                } else {
                                    "Exceeded maximum absence threshold"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = if (course.remainingAbsenceHours > 0f) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        onUpdateMissedHours(course.code, -course.sessionHours)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("+ Present")
                                }

                                OutlinedButton(
                                    onClick = {
                                        onUpdateMissedHours(course.code, course.sessionHours)
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("+ Absence (${course.sessionHours}h)")
                                }
                            }
                        }
                    }
                }
            } else {
                item(key = "empty_state") {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.bar_chart_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "No Course Schedules Added",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Once you set up your class schedules, your course presence, attendance metrics, and absence allowance limits will automatically appear here.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
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

@Preview(showBackground = true)
@Composable
fun AttendanceScreenPreview() {
    AttendanceScreen()
}
