// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.settings

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.corin.lectrium.R

@Composable
fun SettingsThemeToggleItem(
    isDarkTheme: Boolean,
    onThemeToggleChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    toggleItemTitle: String = "App Theme",
    toggleItemSubtitle: String? = if (isDarkTheme) "Dark theme enabled" else "Light theme enabled",
    @DrawableRes toggleItemIcon: Int? = R.drawable.palette_icon
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onThemeToggleChanged(!isDarkTheme) }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (toggleItemIcon != null) {
            SettingsIcon(iconRes = toggleItemIcon)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = toggleItemTitle,
                style = MaterialTheme.typography.bodyLarge
            )

            if (toggleItemSubtitle != null) {
                Text(
                    text = toggleItemSubtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = isDarkTheme,
            onCheckedChange = onThemeToggleChanged,
            thumbContent = {
                AnimatedContent(
                    targetState = isDarkTheme,
                    transitionSpec = {
                        (fadeIn(animationSpec = tween(300)) + scaleIn(animationSpec = tween(300)))
                            .togetherWith(fadeOut(animationSpec = tween(300)) + scaleOut(animationSpec = tween(300)))
                    },
                    label = "ThemeIconTransition"
                ) { dark ->
                    val iconRes = if (dark) R.drawable.dark_mode_icon else R.drawable.light_mode_icon
                    val iconDesc = if (dark) "Dark Mode" else "Light Mode"

                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = iconDesc,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                checkedIconColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedThumbColor = MaterialTheme.colorScheme.tertiary,
                uncheckedTrackColor = MaterialTheme.colorScheme.tertiaryContainer,
                uncheckedIconColor = MaterialTheme.colorScheme.onTertiary
            )
        )
    }
}
