// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.corin.lectrium.model.CustomReminder
import org.corin.lectrium.model.TimeUnit
import org.corin.lectrium.ui.theme.monospace

@Composable
fun CustomDurationDialog(
    initialCustomReminder: CustomReminder = CustomReminder(2, TimeUnit.MINUTES),
    maxMinutes: Int = CustomReminder.DEFAULT_MAX_MINUTES,
    onConfirm: (CustomReminder) -> Unit,
    onDismiss: () -> Unit
) {
    var textValue by remember { mutableStateOf(initialCustomReminder.value.toString()) }
    var selectedUnit by remember { mutableStateOf(initialCustomReminder.unit) }

    val numericValue = textValue.toIntOrNull()
    val isValid = numericValue != null && CustomReminder.isValid(numericValue, selectedUnit, maxMinutes)

    val errorMessage = when {
        numericValue == null || numericValue <= 0 -> "Please enter a positive number"
        !CustomReminder.isValid(numericValue, selectedUnit, maxMinutes) ->
            "Duration cannot exceed ${CustomReminder.formatMinutes(maxMinutes)}"
        else -> null
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Custom Reminder Duration",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Unit:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimeUnit.entries.forEach { unit ->
                        FilterChip(
                            selected = selectedUnit == unit,
                            onClick = { selectedUnit = unit },
                            label = { Text(unit.unitLabel) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = textValue,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            textValue = input
                        }
                    },
                    textStyle = MaterialTheme.typography.bodyLarge.monospace,
                    label = { Text("Duration (${selectedUnit.unitLabel})") },
                    isError = errorMessage != null,
                    supportingText = {
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else if (numericValue != null) {
                            val custom = CustomReminder(numericValue, selectedUnit)
                            Text(text = "Reminder set to ${custom.displayLabel} before class.")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (numericValue != null && isValid) {
                                onConfirm(CustomReminder(numericValue, selectedUnit))
                            }
                        },
                        enabled = isValid
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}