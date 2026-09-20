// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.corin.lectrium.model.NavBarStyle

private val Context.navBarDataStore by preferencesDataStore(name = "nav_bar_preferences")

class NavBarPreferences(private val context: Context) {
    private object PreferenceKeys {
        val NAV_BAR_STYLE = stringPreferencesKey("nav_bar_style")
    }

    val navBarStyleFlow: Flow<NavBarStyle> = context.navBarDataStore.data
        .map { preferences ->
            val savedName = preferences[PreferenceKeys.NAV_BAR_STYLE] ?: NavBarStyle.BOTTOM.name
            try {
                NavBarStyle.valueOf(savedName)
            } catch (_: Exception) {
                NavBarStyle.BOTTOM
            }
        }

    suspend fun saveNavBarStyle(navBarStyle: NavBarStyle) {
        context.navBarDataStore.edit { preferences ->
            preferences[PreferenceKeys.NAV_BAR_STYLE] = navBarStyle.name
        }
    }
}
