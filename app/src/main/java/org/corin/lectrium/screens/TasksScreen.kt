// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.corin.lectrium.components.BouncyCheckbox
import org.corin.lectrium.components.dialogs.AddAssignmentDialog
import org.corin.lectrium.components.dialogs.AddProjectDialog
import org.corin.lectrium.model.Assignment
import org.corin.lectrium.model.AssignmentStatus
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.Project
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    assignments: List<Assignment> = emptyList(),
    projects: List<Project> = emptyList(),
    courses: List<Course> = emptyList(),
    onToggleAssignment: (Assignment) -> Unit = {},
    onAddAssignment: (Assignment, String) -> Unit = { _, _ -> },
    onAddProject: (Project, String) -> Unit = { _, _ -> }
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedFilter by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }

    val courseMap = courses.associateBy { it.id }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tasks & Assignments",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                SecondaryTabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Assignments (${assignments.size})") }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("Projects (${projects.size})") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (selectedTabIndex == 0) {
                    // Assignments View
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("All", "Pending", "Completed").forEach { status ->
                            FilterChip(
                                selected = selectedFilter == status,
                                onClick = { selectedFilter = status },
                                label = { Text(status) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val filteredAssignments = when (selectedFilter) {
                        "Pending" -> assignments.filter { it.status != AssignmentStatus.COMPLETED }
                        "Completed" -> assignments.filter { it.status == AssignmentStatus.COMPLETED }
                        else -> assignments
                    }

                    if (filteredAssignments.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(filteredAssignments) { assignment ->
                                val course = courseMap[assignment.courseId]
                                val isCompleted = assignment.status == AssignmentStatus.COMPLETED
                                val dateFormat =
                                    SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                                val dueStr = dateFormat.format(Date(assignment.dueTimestamp))

                                ElevatedCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        BouncyCheckbox(
                                            checked = isCompleted,
                                            onCheckedChange = { onToggleAssignment(assignment) }
                                        )

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = assignment.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )

                                            Text(
                                                text = "${course?.code ?: "Course"}  •  Due: $dueStr",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "No assignments in this filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    // Projects View
                    ProjectsScreen(projects = projects, courses = courses)
                }
            }
        }

        if (showAddDialog) {
            if (selectedTabIndex == 0) {
                AddAssignmentDialog(
                    courses = courses,
                    onDismissRequest = { showAddDialog = false },
                    onSaveAssignment = { assignment, courseCode ->
                        onAddAssignment(assignment, courseCode)
                    }
                )
            } else {
                AddProjectDialog(
                    courses = courses,
                    onDismissRequest = { showAddDialog = false },
                    onSaveProject = { project, courseCode ->
                        onAddProject(project, courseCode)
                    }
                )
            }
        }
    }
}
