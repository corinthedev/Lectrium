// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.settings

import androidx.annotation.DrawableRes
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsToggleItem(
    toggleItemTitle: String,
    toggleItemChecked: Boolean,
    toggleItemCheckChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    toggleItemSubtitle: String? = null,
    @DrawableRes toggleItemIconRes: Int? = null
) {
    SettingsItem(
        itemTitle = toggleItemTitle,
        itemSubtitle = toggleItemSubtitle,
        itemIconRes = toggleItemIconRes,
        modifier = modifier,
        trailingContent = {
            Switch(
                checked = toggleItemChecked,
                onCheckedChange = toggleItemCheckChanged
            )
        }
    )
}
