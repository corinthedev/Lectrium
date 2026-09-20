// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.corin.lectrium.preferences.NavBarPreferences
import org.corin.lectrium.model.NavBarStyle

class NavBarViewModel(private val preferences: NavBarPreferences) : ViewModel() {
    val navBarStyle: StateFlow<NavBarStyle> = preferences.navBarStyleFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NavBarStyle.BOTTOM
        )

    fun onNavBarStyleSelected(newStyle: NavBarStyle) {
        viewModelScope.launch {
            preferences.saveNavBarStyle(newStyle)
        }
    }
}
