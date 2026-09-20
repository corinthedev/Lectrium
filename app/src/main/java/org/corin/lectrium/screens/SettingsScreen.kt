// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.corin.lectrium.BuildConfig
import org.corin.lectrium.R
import org.corin.lectrium.components.bottomsheet.CustomColorPickerBottomSheet
import org.corin.lectrium.components.bottomsheet.NotificationTypesBottomSheet
import org.corin.lectrium.components.dialogs.CustomDurationDialog
import org.corin.lectrium.components.dialogs.SecretEasterEggDialog
import org.corin.lectrium.components.dialogs.SingleChoiceDialog
import org.corin.lectrium.components.settings.SettingsItem
import org.corin.lectrium.components.settings.SettingsSection
import org.corin.lectrium.components.settings.SettingsSingleChoiceSegmentedItem
import org.corin.lectrium.components.settings.SettingsToggleItem
import org.corin.lectrium.model.ColorSchemeMode
import org.corin.lectrium.model.CustomReminder
import org.corin.lectrium.model.NavBarStyle
import org.corin.lectrium.model.NotificationType
import org.corin.lectrium.model.ReminderTiming
import org.corin.lectrium.model.ThemeMode
import org.corin.lectrium.model.TimeUnit
import org.corin.lectrium.ui.theme.monospace
import org.corin.lectrium.viewmodel.ClassReminderViewModel
import org.corin.lectrium.viewmodel.NavBarViewModel
import org.corin.lectrium.viewmodel.ThemeViewModel

/**
 * Stateful entry point for [SettingsScreen]. Collects ViewModel states safely
 * and delegates UI rendering to [SettingsScreenContent].
 */
@Composable
fun SettingsScreen(
    classReminderViewModel: ClassReminderViewModel? = null,
    navBarViewModel: NavBarViewModel? = null,
    themeViewModel: ThemeViewModel? = null,
    currentNavBarStyle: NavBarStyle = NavBarStyle.BOTTOM,
    onNavBarStyleChange: (NavBarStyle) -> Unit = {},
    onNavigateToManageProfile: () -> Unit = {}
) {
    val activeNavBarStyle by (navBarViewModel?.navBarStyle?.collectAsStateWithLifecycle()
        ?: remember(currentNavBarStyle) { mutableStateOf(currentNavBarStyle) })

    val activeThemeMode by (themeViewModel?.themeMode?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(ThemeMode.SYSTEM) })

    val activeColorSchemeMode by (themeViewModel?.colorSchemeMode?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(ColorSchemeMode.DEFAULT) })

    val customColorHex by (themeViewModel?.customColorHex?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf("38BDF8") })

    val masterNotificationsEnabled by (classReminderViewModel?.masterNotificationsEnabled?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(true) })

    val enabledNotificationTypes by (classReminderViewModel?.enabledNotificationTypes?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(NotificationType.ALL) })

    val currentClassReminderTiming = classReminderViewModel?.classReminderTiming?.collectAsStateWithLifecycle()?.value
        ?: ReminderTiming.Off

    SettingsScreenContent(
        masterNotificationsEnabled = masterNotificationsEnabled,
        enabledNotificationTypes = enabledNotificationTypes,
        currentClassReminderTiming = currentClassReminderTiming,
        activeNavBarStyle = activeNavBarStyle,
        activeThemeMode = activeThemeMode,
        activeColorSchemeMode = activeColorSchemeMode,
        customColorHex = customColorHex,
        onMasterNotificationsToggled = { checked ->
            classReminderViewModel?.onMasterNotificationsToggled(checked)
        },
        onNotificationTypeToggled = { type, enabled ->
            classReminderViewModel?.onNotificationTypeToggled(type, enabled)
        },
        onReminderTimingSelected = { timing ->
            classReminderViewModel?.onReminderTimingSelected(timing)
        },
        onThemeModeSelected = { mode ->
            themeViewModel?.onThemeModeSelected(mode)
        },
        onColorSchemeModeSelected = { mode ->
            themeViewModel?.onColorSchemeModeSelected(mode)
        },
        onCustomColorHexChanged = { hex ->
            themeViewModel?.onCustomColorHexChanged(hex)
        },
        onNavBarStyleSelected = { newStyle ->
            navBarViewModel?.onNavBarStyleSelected(newStyle)
            onNavBarStyleChange(newStyle)
        },
        onNavigateToManageProfile = onNavigateToManageProfile
    )
}

/**
 * Fast, stateless UI content for [SettingsScreen]. Extremely efficient for rendering
 * and instant Compose Previews without ViewModel / Lifecycle collection overhead.
 */
@Composable
fun SettingsScreenContent(
    masterNotificationsEnabled: Boolean,
    enabledNotificationTypes: Set<NotificationType>,
    currentClassReminderTiming: ReminderTiming,
    activeNavBarStyle: NavBarStyle,
    activeThemeMode: ThemeMode,
    activeColorSchemeMode: ColorSchemeMode,
    customColorHex: String,
    onMasterNotificationsToggled: (Boolean) -> Unit,
    onNotificationTypeToggled: (NotificationType, Boolean) -> Unit,
    onReminderTimingSelected: (ReminderTiming) -> Unit,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onColorSchemeModeSelected: (ColorSchemeMode) -> Unit,
    onCustomColorHexChanged: (String) -> Unit,
    onNavBarStyleSelected: (NavBarStyle) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToManageProfile: () -> Unit = {}
) {
    val context = LocalContext.current

    var showClassReminderDialog by remember { mutableStateOf(false) }
    var showCustomDurationDialog by remember { mutableStateOf(false) }
    var showNotificationTypesBottomSheet by remember { mutableStateOf(false) }
    var showCustomColorPickerBottomSheet by remember { mutableStateOf(false) }
    var showEasterEggDialog by remember { mutableStateOf(false) }
    var appVersionClickCount by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item(key = "section_notifications") {
            SettingsSection(sectionTitle = "Notifications") {
                SettingsToggleItem(
                    toggleItemTitle = "Master Notifications",
                    toggleItemSubtitle = if (masterNotificationsEnabled) {
                        "Turn off all app notifications"
                    } else {
                        "All app notifications are currently disabled"
                    },
                    toggleItemIconRes = R.drawable.notifications_icon,
                    toggleItemChecked = masterNotificationsEnabled,
                    toggleItemCheckChanged = onMasterNotificationsToggled
                )
                SettingsItem(
                    itemTitle = "Notification Categories",
                    itemSubtitle = if (masterNotificationsEnabled) {
                        "${enabledNotificationTypes.size} of ${NotificationType.entries.size} categories enabled"
                    } else {
                        "Disabled while Master Notifications is OFF"
                    },
                    itemIconRes = R.drawable.notifications_icon,
                    onClick = if (masterNotificationsEnabled) {
                        { showNotificationTypesBottomSheet = true }
                    } else null
                )
                SettingsItem(
                    itemTitle = "Class Reminders",
                    itemSubtitle = if (masterNotificationsEnabled) {
                        "Current: ${currentClassReminderTiming.label}"
                    } else {
                        "Disabled while Master Notifications is OFF"
                    },
                    itemIconRes = R.drawable.alarm_icon,
                    onClick = if (masterNotificationsEnabled) {
                        { showClassReminderDialog = true }
                    } else null
                )
            }
        }

        item(key = "section_schedule") {
            SettingsSection(sectionTitle = "Schedule Display") {

            }
        }

        item(key = "section_attendance") {
            SettingsSection(sectionTitle = "Attendance") {

            }
        }

        item(key = "section_account") {
            SettingsSection(sectionTitle = "Account and Data") {
                SettingsItem(
                    itemTitle = "Manage Profile",
                    itemIconRes = R.drawable.person_icon,
                    trailingContent = {
                        Icon(
                            painter = painterResource(R.drawable.chevron_right_icon),
                            contentDescription = "Navigate to Manage Profile",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = onNavigateToManageProfile
                )
            }
        }

        item(key = "section_accessibility") {
            SettingsSection(sectionTitle = "Accessibility and Display") {
                SettingsSingleChoiceSegmentedItem(
                    segmentedItemTitle = "App Theme",
                    segmentedItemSubtitle = "Choose theme mode preference",
                    segmentedItemOpts = ThemeMode.entries,
                    segmentedItemSelectedOption = activeThemeMode,
                    segmentedItemIcon = R.drawable.palette_icon,
                    onSelectedSegmentedItemOption = onThemeModeSelected,
                    segmentedItemOptionLabel = { mode ->
                        when (mode) {
                            ThemeMode.SYSTEM -> "System"
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Dark"
                        }
                    }
                )
                SettingsSingleChoiceSegmentedItem(
                    segmentedItemTitle = "Color Scheme",
                    segmentedItemSubtitle = "Choose color palette",
                    segmentedItemOpts = ColorSchemeMode.entries,
                    segmentedItemSelectedOption = activeColorSchemeMode,
                    segmentedItemIcon = R.drawable.palette_icon,
                    onSelectedSegmentedItemOption = onColorSchemeModeSelected,
                    segmentedItemOptionLabel = { mode ->
                        when (mode) {
                            ColorSchemeMode.DEFAULT -> "Sky Blue"
                            ColorSchemeMode.DYNAMIC -> "Dynamic"
                            ColorSchemeMode.CUSTOM -> "Custom"
                        }
                    }
                )

                if (activeColorSchemeMode == ColorSchemeMode.CUSTOM) {
                    val previewColor = runCatching {
                        Color(android.graphics.Color.parseColor("#FF$customColorHex"))
                    }.getOrDefault(MaterialTheme.colorScheme.primary)

                    SettingsItem(
                        itemTitle = "Customize Theme Color",
                        itemSubtitle = "Hex: #$customColorHex",
                        itemIconRes = R.drawable.palette_icon,
                        trailingContent = {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(previewColor)
                                    .border(1.5.dp, MaterialTheme.colorScheme.outline, CircleShape)
                            )
                        },
                        onClick = {
                            showCustomColorPickerBottomSheet = true
                        }
                    )
                }

                SettingsSingleChoiceSegmentedItem(
                    segmentedItemTitle = "Navigation Bar Style",
                    segmentedItemSubtitle = "Choose a preferred navigation layout",
                    segmentedItemOpts = NavBarStyle.entries,
                    segmentedItemSelectedOption = activeNavBarStyle,
                    segmentedItemIcon = R.drawable.dock_icon,
                    onSelectedSegmentedItemOption = onNavBarStyleSelected,
                    segmentedItemOptionLabel = { style ->
                        when (style) {
                            NavBarStyle.BOTTOM -> "Bottom"
                            NavBarStyle.FLOATING -> "Floating"
                        }
                    }
                )
            }
        }

        item(key = "section_about") {
            SettingsSection(sectionTitle = "About") {
                SettingsItem(
                    itemTitle = "App Version",
                    itemIconRes = R.drawable.info_icon,
                    trailingContent = {
                        Text(
                            text = BuildConfig.VERSION_NAME,
                            style = MaterialTheme.typography.bodySmall.monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = {
                        appVersionClickCount++
                        val remaining = 7 - appVersionClickCount
                        if (remaining in 1..3) {
                            Toast.makeText(
                                context,
                                "You are $remaining step${if (remaining > 1) "s" else ""} away from a secret...",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (remaining <= 0) {
                            appVersionClickCount = 0
                            showEasterEggDialog = true
                        }
                    }
                )
            }
        }
    }

    if (showCustomColorPickerBottomSheet) {
        CustomColorPickerBottomSheet(
            initialColorHex = customColorHex,
            onColorSelected = onCustomColorHexChanged,
            onDismissRequest = {
                showCustomColorPickerBottomSheet = false
            }
        )
    }

    if (showEasterEggDialog) {
        SecretEasterEggDialog(
            onDismissRequest = {
                showEasterEggDialog = false
            }
        )
    }

    if (showNotificationTypesBottomSheet) {
        NotificationTypesBottomSheet(
            enabledTypes = enabledNotificationTypes,
            onTypeToggle = onNotificationTypeToggled,
            onDismissRequest = {
                showNotificationTypesBottomSheet = false
            }
        )
    }

    if (showClassReminderDialog) {
        SingleChoiceDialog(
            dialogTitle = "Class Reminder Timing",
            dialogOpts = ReminderTiming.dialogOptions,
            dialogSelectedOpt = currentClassReminderTiming,
            dialogOptionLabel = { option -> option.label },
            isSelected = { option, selected ->
                if (option is ReminderTiming.CustomPlaceholder) {
                    selected is ReminderTiming.Custom
                } else {
                    option == selected
                }
            },
            onDialogOptionSelected = { selectedTiming ->
                if (selectedTiming is ReminderTiming.CustomPlaceholder) {
                    showClassReminderDialog = false
                    showCustomDurationDialog = true
                } else {
                    onReminderTimingSelected(selectedTiming)
                    showClassReminderDialog = false
                }
            },
            onDialogDismiss = {
                showClassReminderDialog = false
            }
        )
    }

    if (showCustomDurationDialog) {
        val initialCustom = (currentClassReminderTiming as? ReminderTiming.Custom)?.customReminder
            ?: CustomReminder(2, TimeUnit.MINUTES)

        CustomDurationDialog(
            initialCustomReminder = initialCustom,
            maxMinutes = CustomReminder.DEFAULT_MAX_MINUTES,
            onConfirm = { customReminder ->
                onReminderTimingSelected(ReminderTiming.Custom(customReminder))
                showCustomDurationDialog = false
            },
            onDismiss = {
                showCustomDurationDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        masterNotificationsEnabled = true,
        enabledNotificationTypes = NotificationType.ALL,
        currentClassReminderTiming = ReminderTiming.presets[1],
        activeNavBarStyle = NavBarStyle.BOTTOM,
        activeThemeMode = ThemeMode.SYSTEM,
        activeColorSchemeMode = ColorSchemeMode.CUSTOM,
        customColorHex = "38BDF8",
        onMasterNotificationsToggled = {},
        onNotificationTypeToggled = { _, _ -> },
        onReminderTimingSelected = {},
        onThemeModeSelected = {},
        onColorSchemeModeSelected = {},
        onCustomColorHexChanged = {},
        onNavBarStyleSelected = {}
    )
}
