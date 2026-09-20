// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.corin.lectrium.model.ScreenDestinations

@Composable
public fun BottomNavBar(
    modifier: Modifier = Modifier,
    currentScreenDestination: ScreenDestinations,
    onScreenDestinationSelected: (ScreenDestinations) -> Unit
) {
    NavigationBar(modifier = modifier) {
        ScreenDestinations.entries.forEach { destination ->
            NavigationBarItem(
                selected = currentScreenDestination == destination,
                onClick = { onScreenDestinationSelected(destination) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.routeIcon),
                        contentDescription = destination.routeDesc
                    )
                },
                label = { Text(text = destination.routeLabel) }
            )
        }
    }
}