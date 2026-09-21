// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.corin.lectrium.model.Course
import org.corin.lectrium.model.Exam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExamDialog(
    courses: List<Course>,
    onDismissRequest: () -> Unit,
    onSaveExam: (Exam, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var durationStr by remember { mutableStateOf("90") }
    var weightStr by remember { mutableStateOf("30") }
    var selectedCourse by remember { mutableStateOf(courses.firstOrNull()) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Schedule Exam",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Exam Title (e.g. Midterm Exam)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = room,
                onValueChange = { room = it },
                label = { Text("Exam Room / Venue") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = durationStr,
                    onValueChange = { durationStr = it },
                    label = { Text("Duration (mins)") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = weightStr,
                    onValueChange = { weightStr = it },
                    label = { Text("Weight %") },
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
                        if (title.isNotBlank()) {
                            val exam = Exam(
                                id = "",
                                courseId = selectedCourse?.id ?: "",
                                title = title,
                                examTimestamp = System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000L),
                                durationMinutes = durationStr.toIntOrNull() ?: 90,
                                room = room,
                                weightPercentage = weightStr.toDoubleOrNull() ?: 30.0
                            )
                            onSaveExam(exam, selectedCourse?.code ?: "Course")
                            onDismissRequest()
                        }
                    }
                ) {
                    Text("Save Exam")
                }
            }
        }
    }
}
