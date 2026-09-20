// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.corin.lectrium.model.ColorSchemeMode
import org.corin.lectrium.model.ThemeMode
import org.corin.lectrium.preferences.ThemePreferences

class ThemeViewModel(private val preferences: ThemePreferences) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = preferences.themeModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )

    val colorSchemeMode: StateFlow<ColorSchemeMode> = preferences.colorSchemeModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ColorSchemeMode.DEFAULT
        )

    val customColorHex: StateFlow<String> = preferences.customColorHexFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "38BDF8"
        )

    fun onThemeModeSelected(mode: ThemeMode) {
        viewModelScope.launch {
            preferences.saveThemeMode(mode)
        }
    }

    fun onColorSchemeModeSelected(mode: ColorSchemeMode) {
        viewModelScope.launch {
            preferences.saveColorSchemeMode(mode)
        }
    }

    fun onCustomColorHexChanged(hex: String) {
        viewModelScope.launch {
            preferences.saveCustomColorHex(hex)
        }
    }
}
