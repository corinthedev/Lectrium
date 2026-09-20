// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.corin.lectrium.ui.theme.monospace

private val PRESET_COLORS = listOf(
    Color(0xFF38BDF8), // Sky Blue
    Color(0xFF2DD4BF), // Teal
    Color(0xFF8B5CF6), // Purple
    Color(0xFFEC4899), // Pink
    Color(0xFFEF4444), // Red
    Color(0xFFF59E0B), // Amber
    Color(0xFF10B981), // Emerald
    Color(0xFF6366F1), // Indigo
    Color(0xFF64748B)  // Slate
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomColorPickerBottomSheet(
    initialColorHex: String,
    onColorSelected: (String) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val initialColor = remember(initialColorHex) {
        parseColorHexOrDefault(initialColorHex, Color(0xFF38BDF8))
    }

    var red by remember { mutableFloatStateOf(initialColor.red * 255f) }
    var green by remember { mutableFloatStateOf(initialColor.green * 255f) }
    var blue by remember { mutableFloatStateOf(initialColor.blue * 255f) }
    var alpha by remember { mutableFloatStateOf(initialColor.alpha * 100f) }

    val currentColor = remember(red, green, blue, alpha) {
        Color(
            red = red / 255f,
            green = green / 255f,
            blue = blue / 255f,
            alpha = alpha / 100f
        )
    }

    var hexInput by remember(currentColor) {
        mutableStateOf(colorToHex(currentColor))
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Custom Theme Color",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Adjust RGB values, enter a hex code, or choose a swatch",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Live Preview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(currentColor)
                            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Live Theme Preview",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "#${colorToHex(currentColor)}",
                            style = MaterialTheme.typography.bodyMedium.monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(currentColor.copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Preview",
                            style = MaterialTheme.typography.labelMedium,
                            color = currentColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Preset Swatches
            Text(
                text = "Preset Swatches",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(PRESET_COLORS) { color ->
                    val isSelected = (colorToHex(color) == colorToHex(currentColor))
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                red = color.red * 255f
                                green = color.green * 255f
                                blue = color.blue * 255f
                                alpha = color.alpha * 100f
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hex Code TextField
            OutlinedTextField(
                value = hexInput,
                onValueChange = { input ->
                    val clean = input.filter { it.isLetterOrDigit() }.take(8).uppercase()
                    hexInput = clean
                    if (clean.length == 6 || clean.length == 8) {
                        val parsed = parseColorHexOrNull(clean)
                        if (parsed != null) {
                            red = parsed.red * 255f
                            green = parsed.green * 255f
                            blue = parsed.blue * 255f
                            alpha = parsed.alpha * 100f
                        }
                    }
                },
                label = { Text("Hex Color Code") },
                prefix = { Text("# ") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // RGBA Sliders
            RgbaSlider(
                label = "Red (R)",
                value = red,
                color = Color.Red,
                onValueChange = { red = it }
            )

            RgbaSlider(
                label = "Green (G)",
                value = green,
                color = Color.Green,
                onValueChange = { green = it }
            )

            RgbaSlider(
                label = "Blue (B)",
                value = blue,
                color = Color.Blue,
                onValueChange = { blue = it }
            )

            RgbaSlider(
                label = "Alpha (Opacity)",
                value = alpha,
                maxValue = 100f,
                color = MaterialTheme.colorScheme.primary,
                onValueChange = { alpha = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val defaultColor = Color(0xFF38BDF8)
                        red = defaultColor.red * 255f
                        green = defaultColor.green * 255f
                        blue = defaultColor.blue * 255f
                        alpha = defaultColor.alpha * 100f
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = {
                        onColorSelected(colorToHex(currentColor))
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply Color")
                }
            }
        }
    }
}

@Composable
private fun RgbaSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    color: Color,
    maxValue: Float = 255f
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value.toInt().toString(),
                style = MaterialTheme.typography.bodySmall.monospace
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..maxValue,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color
            )
        )
    }
}

private fun parseColorHexOrDefault(hex: String, default: Color): Color {
    return parseColorHexOrNull(hex) ?: default
}

private fun parseColorHexOrNull(hex: String): Color? {
    return runCatching {
        val clean = hex.removePrefix("#")
        val colorInt = if (clean.length == 6) {
            android.graphics.Color.parseColor("#FF$clean")
        } else if (clean.length == 8) {
            android.graphics.Color.parseColor("#$clean")
        } else {
            return null
        }
        Color(colorInt)
    }.getOrNull()
}

private fun colorToHex(color: Color): String {
    val argb = color.toArgb()
    return String.format("%06X", 0xFFFFFF and argb)
}
