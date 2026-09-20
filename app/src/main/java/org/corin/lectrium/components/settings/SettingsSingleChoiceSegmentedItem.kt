// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SettingsSingleChoiceSegmentedItem(
    segmentedItemTitle: String,
    segmentedItemOpts: List<T>,
    segmentedItemSelectedOption: T,
    onSelectedSegmentedItemOption: (T) -> Unit,
    segmentedItemOptionLabel: (T) -> String,
    modifier: Modifier = Modifier,
    segmentedItemSubtitle: String? = null,
    @DrawableRes segmentedItemIcon: Int? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (segmentedItemIcon != null) {
                SettingsIcon(iconRes = segmentedItemIcon)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = segmentedItemTitle,
                    style = MaterialTheme.typography.bodyLarge
                )

                if (segmentedItemSubtitle != null) {
                    Text(
                        text = segmentedItemSubtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
        ) {
            segmentedItemOpts.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = segmentedItemSelectedOption == option,
                    onClick = { onSelectedSegmentedItemOption(option) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = segmentedItemOpts.size,
                        baseShape = RoundedCornerShape(8.dp)
                    )
                ) {
                    Text(
                        text = segmentedItemOptionLabel(option),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
