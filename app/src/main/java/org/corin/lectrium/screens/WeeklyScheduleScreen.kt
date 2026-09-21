// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.screens

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.corin.lectrium.components.schedule.TimetableGrid
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course

@Composable
fun WeeklyScheduleScreen(
    modifier: Modifier = Modifier,
    courses: List<Course> = emptyList(),
    schedules: List<ClassSchedule> = emptyList(),
    showSaturday: Boolean = false,
    showSunday: Boolean = false,
    onToggleSaturday: (Boolean) -> Unit = {},
    onToggleSunday: (Boolean) -> Unit = {}
) {
    val totalUnits = courses.sumOf { it.units }

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
                text = "Weekly Timetable",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "$totalUnits Total Units  •  ${courses.size} Enrolled Courses",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Weekend Day Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Weekend Days: ",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                FilterChip(
                    selected = showSaturday,
                    onClick = { onToggleSaturday(!showSaturday) },
                    label = { Text("Saturday") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                FilterChip(
                    selected = showSunday,
                    onClick = { onToggleSunday(!showSunday) },
                    label = { Text("Sunday") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timetable Grid Component
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                TimetableGrid(
                    courses = courses,
                    schedules = schedules,
                    showSaturday = showSaturday,
                    showSunday = showSunday
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
