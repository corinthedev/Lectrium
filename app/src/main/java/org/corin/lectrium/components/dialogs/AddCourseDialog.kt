// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.corin.lectrium.R
import org.corin.lectrium.components.bottomsheet.CustomColorPickerBottomSheet
import org.corin.lectrium.model.ClassSchedule
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.CourseClassification
import org.corin.lectrium.model.CourseModality
import java.time.DayOfWeek

val colorSwatches = listOf(
    "38BDF8", "F43F5E", "10B981", "F59E0B", "8B5CF6", "EC4899", "6366F1", "14B8A6"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseDialog(
    onDismissRequest: () -> Unit,
    onSaveCourse: (Course, List<ClassSchedule>) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var code by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var section by remember { mutableStateOf("") }
    var unitsStr by remember { mutableStateOf("3") }
    var faculty by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var selectedColorHex by remember { mutableStateOf("38BDF8") }
    var showColorPickerSheet by remember { mutableStateOf(false) }

    var modality by remember { mutableStateOf(CourseModality.FACE_TO_FACE) }
    var classification by remember { mutableStateOf(CourseClassification.LECTURE) }

    var selectedDay by remember { mutableStateOf(DayOfWeek.MONDAY) }
    var startTime by remember { mutableStateOf("08:30") }
    var endTime by remember { mutableStateOf("10:00") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Add New Course",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text("Course Code (e.g. CS 101)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Course Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = section,
                    onValueChange = { section = it },
                    label = { Text("Section") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = unitsStr,
                    onValueChange = { unitsStr = it },
                    label = { Text("Units") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = faculty,
                onValueChange = { faculty = it },
                label = { Text("Faculty / Professor") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = room,
                onValueChange = { room = it },
                label = { Text("Room / Venue") },
                modifier = Modifier.fillMaxWidth()
            )

            // Course Color Badge Section
            Text("Course Color Badge", style = MaterialTheme.typography.titleSmall)
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(colorSwatches) { hex ->
                    val color = parseHex(hex)
                    val isSelected = hex.equals(selectedColorHex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 0.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { selectedColorHex = hex }
                    )
                }
            }

            // Custom Hex Input + RGBA Color Picker Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val activeBadgeColor = parseHex(selectedColorHex)

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(activeBadgeColor)
                        .border(1.5.dp, MaterialTheme.colorScheme.outline, CircleShape)
                )

                OutlinedTextField(
                    value = selectedColorHex,
                    onValueChange = { input ->
                        val clean = input.filter { it.isLetterOrDigit() }.take(6).uppercase()
                        selectedColorHex = clean
                    },
                    label = { Text("Custom Hex Color") },
                    prefix = { Text("# ") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                OutlinedButton(
                    onClick = { showColorPickerSheet = true }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.palette_icon),
                        contentDescription = "Pick Custom Color"
                    )
                }
            }

            Text("Class Schedule Slot", style = MaterialTheme.typography.titleSmall)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(DayOfWeek.entries) { day ->
                    FilterChip(
                        selected = day == selectedDay,
                        onClick = { selectedDay = day },
                        label = { Text(day.name.take(3)) }
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start (HH:mm)") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End (HH:mm)") },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = onDismissRequest) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.size(12.dp))

                Button(
                    onClick = {
                        if (code.isNotBlank() && title.isNotBlank()) {
                            val course = Course(
                                id = "",
                                code = code,
                                title = title,
                                section = section,
                                units = unitsStr.toIntOrNull() ?: 3,
                                faculty = faculty,
                                room = room,
                                colorHex = selectedColorHex.ifBlank { "38BDF8" },
                                modality = modality,
                                classification = classification
                            )
                            val schedule = ClassSchedule(
                                id = "",
                                courseId = "",
                                dayOfWeek = selectedDay,
                                startTime = startTime,
                                endTime = endTime,
                                roomOverride = room
                            )
                            onSaveCourse(course, listOf(schedule))
                            onDismissRequest()
                        }
                    }
                ) {
                    Text("Save Course")
                }
            }
        }
    }

    if (showColorPickerSheet) {
        CustomColorPickerBottomSheet(
            initialColorHex = selectedColorHex,
            onColorSelected = { newHex ->
                selectedColorHex = newHex
            },
            onDismissRequest = {
                showColorPickerSheet = false
            }
        )
    }
}

private fun parseHex(hex: String): Color {
    return try {
        val colorInt = hex.removePrefix("#").toLong(16) or 0xFF000000
        Color(colorInt)
    } catch (_: Exception) {
        Color(0xFF38BDF8)
    }
}
