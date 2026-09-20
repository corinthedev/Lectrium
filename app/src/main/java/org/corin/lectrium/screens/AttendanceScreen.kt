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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.corin.lectrium.R
import java.util.Locale

private data class CourseAttendanceData(
    val code: String,
    val title: String,
    val units: Int = 3,
    val totalHours: Float = 54f, // 3 units * 18 weeks
    val maxAbsenceHours: Float = 9.0f, // 20% of 54h
    var missedHours: Float
) {
    val attendancePercentage: Int
        get() {
            val attended = (totalHours - missedHours).coerceAtLeast(0f)
            return ((attended / totalHours) * 100f).toInt().coerceIn(0, 100)
        }

    val remainingAbsenceHours: Float
        get() = (maxAbsenceHours - missedHours).coerceAtLeast(0f)

    val status: String
        get() = when {
            missedHours > maxAbsenceHours -> "Advised to Withdraw (5.0)"
            missedHours >= 7.5f -> "Critical (Max Absences)"
            missedHours >= 4.5f -> "Warning"
            else -> "Safe"
        }
}

@Composable
fun AttendanceScreen(modifier: Modifier = Modifier) {
    var courses by remember {
        mutableStateOf(
            listOf(
                CourseAttendanceData("CS101", "Data Structures & Algorithms", 3, 54f, 9.0f, 1.5f),
                CourseAttendanceData("MATH201", "Linear Algebra", 3, 54f, 9.0f, 4.5f),
                CourseAttendanceData("PHY102", "General Physics", 3, 54f, 9.0f, 7.5f),
                CourseAttendanceData("CHEM101", "Organic Chemistry", 3, 54f, 9.0f, 10.5f)
            )
        )
    }

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
                    text = "Monitor your course attendance and 20% maximum absence limits",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

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

            item(key = "rule_banner") {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.info_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "20% Absence Rule (9 Hours Limit)",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "For a 3-unit course (54 hrs), missing over 20% (> 9.0 hrs) triggers an Advised to Withdraw (DRP) or automatic 5.0 grade.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                    text = "${course.units} Units  •  Max Absences: 9.0 hrs (6 classes)",
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
                                text = "Missed: ${String.format(Locale.getDefault(), "%.1f", course.missedHours)} / 9.0 hrs",
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
                                "Allowance left: ${String.format(Locale.getDefault(), "%.1f", course.remainingAbsenceHours)} hrs (~${(course.remainingAbsenceHours / 1.5f).toInt()} classes) before 20% limit"
                            } else {
                                "Exceeded 20% maximum absence threshold"
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
                                    courses = courses.map {
                                        if (it.code == course.code) {
                                            it.copy(missedHours = (it.missedHours - 1.5f).coerceAtLeast(0f))
                                        } else it
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("+ Present")
                            }

                            OutlinedButton(
                                onClick = {
                                    courses = courses.map {
                                        if (it.code == course.code) {
                                            it.copy(missedHours = it.missedHours + 1.5f)
                                        } else it
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("+ Absence (1.5h)")
                            }
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
