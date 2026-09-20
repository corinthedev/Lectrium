// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.corin.lectrium.model.NavBarStyle
import org.corin.lectrium.model.ScreenDestinations

@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    currentScreenDestination: ScreenDestinations,
    onScreenDestinationSelected: (ScreenDestinations) -> Unit,
    navBarStyle: NavBarStyle = NavBarStyle.BOTTOM
) {
    when (navBarStyle) {
        NavBarStyle.BOTTOM -> BottomNavBar(
            modifier = modifier,
            currentScreenDestination = currentScreenDestination,
            onScreenDestinationSelected = onScreenDestinationSelected
        )
        NavBarStyle.FLOATING -> FloatingNavBar(
            modifier = modifier,
            currentScreenDestination = currentScreenDestination,
            onScreenDestinationSelected = onScreenDestinationSelected
        )
    }
}