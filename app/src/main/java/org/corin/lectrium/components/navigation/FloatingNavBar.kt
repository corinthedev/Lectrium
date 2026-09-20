// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.corin.lectrium.model.ScreenDestinations
import kotlin.math.roundToInt

@Composable
fun FloatingNavBar(
    currentScreenDestination: ScreenDestinations,
    onScreenDestinationSelected: (ScreenDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    val destinations = ScreenDestinations.entries
    val selectedIndex = destinations.indexOf(currentScreenDestination).coerceAtLeast(0)

    val itemWidth = 56.dp
    val itemHeight = 44.dp
    val padding = 6.dp

    val density = LocalDensity.current
    val itemWidthPx = with(density) { itemWidth.toPx() }

    var isDragging by remember { mutableStateOf(false) }
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }

    val baseOffsetPx = selectedIndex * itemWidthPx
    val maxOffsetPx = (destinations.size - 1) * itemWidthPx

    // Calculate current indicator position
    val targetOffsetPx = if (isDragging) {
        (baseOffsetPx + dragOffsetPx).coerceIn(0f, maxOffsetPx)
    } else {
        baseOffsetPx
    }

    // Direct tracking while dragging, bouncy spring snap when releasing/tapping
    val animatedIndicatorOffsetPx by animateFloatAsState(
        targetValue = targetOffsetPx,
        animationSpec = if (isDragging) {
            spring(stiffness = Spring.StiffnessHigh)
        } else {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "IndicatorOffsetAnimation"
    )

    // Preview index for icon highlighting during drag
    val activeHighlightedIndex = if (isDragging) {
        (targetOffsetPx / itemWidthPx).roundToInt().coerceIn(0, destinations.size - 1)
    } else {
        selectedIndex
    }

    Surface(
        modifier = modifier.padding(vertical = 22.dp),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Box(
            modifier = Modifier
                .padding(padding)
                .pointerInput(destinations, selectedIndex) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            isDragging = true
                            dragOffsetPx = 0f
                        },
                        onDragEnd = {
                            val finalOffsetPx = (baseOffsetPx + dragOffsetPx).coerceIn(0f, maxOffsetPx)
                            val nearestIndex = (finalOffsetPx / itemWidthPx)
                                .roundToInt()
                                .coerceIn(0, destinations.size - 1)

                            isDragging = false
                            dragOffsetPx = 0f

                            // Screen transition ONLY happens on drag release!
                            onScreenDestinationSelected(destinations[nearestIndex])
                        },
                        onDragCancel = {
                            isDragging = false
                            dragOffsetPx = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            dragOffsetPx += dragAmount
                        }
                    )
                }
        ) {
            // Selected Indicator Pill
            Box(
                modifier = Modifier
                    .offset { IntOffset(animatedIndicatorOffsetPx.roundToInt(), 0) }
                    .width(itemWidth)
                    .height(itemHeight)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )

            // Destination Icons
            Row {
                destinations.forEachIndexed { index, destination ->
                    val isHighlighted = index == activeHighlightedIndex

                    Box(
                        modifier = Modifier
                            .width(itemWidth)
                            .height(itemHeight)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                isDragging = false
                                dragOffsetPx = 0f
                                onScreenDestinationSelected(destination)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = destination.routeIcon),
                            contentDescription = destination.routeDesc,
                            tint = if (isHighlighted) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}
