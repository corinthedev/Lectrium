// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun <T> SingleChoiceDialog(
    dialogTitle: String,
    dialogOpts: List<T>,
    dialogSelectedOpt: T,
    dialogOptionLabel: (T) -> String,
    isSelected: ((T, T) -> Boolean)? = null,
    onDialogOptionSelected: (T) -> Unit,
    onDialogDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDialogDismiss) {
        Surface(shape = MaterialTheme.shapes.large, tonalElevation = 6.dp) {
            Column(Modifier.padding(vertical = 8.dp)) {
                Text(
                    dialogTitle,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
                dialogOpts.forEach { option ->
                    val selected = isSelected?.invoke(option, dialogSelectedOpt) ?: (option == dialogSelectedOpt)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onDialogOptionSelected(option)
                            }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        RadioButton(selected = selected, onClick = null)
                        Spacer(Modifier.width(16.dp))
                        Text(dialogOptionLabel(option))
                    }
                }
            }
        }
    }
}