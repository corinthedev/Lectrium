// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components.navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.corin.lectrium.R
import org.corin.lectrium.model.ScreenDestinations
import kotlin.math.roundToInt

data class SpeedDialOption(
    val label: String,
    val iconRes: Int,
    val onClick: () -> Unit
)

@Composable
fun GooMorphFloatingNavBar(
    currentScreenDestination: ScreenDestinations,
    onScreenDestinationSelected: (ScreenDestinations) -> Unit,
    isFabVisible: Boolean,
    speedDialOptions: List<SpeedDialOption> = emptyList(),
    onFabClick: () -> Unit = {},
    initialIsExpanded: Boolean = false,
    modifier: Modifier = Modifier
) {
    val destinations = ScreenDestinations.entries
    val itemWidth = 44.dp
    val padding = 4.dp
    val navBarWidth = itemWidth * destinations.size + (padding * 2) // 272.dp

    var isExpanded by remember { mutableStateOf(initialIsExpanded) }

    // Close speed dial when FAB becomes invisible or screen changes
    LaunchedEffect(isFabVisible, currentScreenDestination) {
        if (!isFabVisible) {
            isExpanded = false
        }
    }

    // Spring progress for FAB entry: 0f (retracted inside dock) to 1f (extended beside dock)
    val fabProgress = remember { Animatable(if (isFabVisible) 1f else 0f) }

    LaunchedEffect(isFabVisible) {
        fabProgress.animateTo(
            targetValue = if (isFabVisible) 1f else 0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
    }

    val progress = fabProgress.value

    // Dock offset: 0.dp (when FAB hidden) -> -28.dp (when FAB visible)
    // Combined width when FAB visible = navBarWidth (272.dp) + gap (8.dp) + FAB (48.dp) = 328.dp
    // Center at x = 0 means dock center is at -28.dp.
    val dockOffset = (-28.dp) * progress

    // FAB center offset relative to screen center:
    // When progress = 0f: FAB center is at +112.dp (inside right end of dock: 136 - 24 = 112.dp)
    // When progress = 1f: FAB center is at +140.dp (Dock right edge +108.dp + gap 8.dp + FAB radius 24.dp = 140.dp)
    val startFabCenter = (navBarWidth / 2) - 24.dp // +112.dp
    val endFabCenter = (navBarWidth / 2) - 28.dp + 8.dp + 24.dp // +140.dp
    val fabCenterOffset = startFabCenter + ((endFabCenter - startFabCenter) * progress)

    val fabScale = 0.35f + (0.65f * progress)

    // Rotation angle for primary '+' icon (0° -> 135°)
    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 135f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "FabIconRotation"
    )

    // Animatable progress array for speed dial options
    val subItemAnimatables = remember(speedDialOptions.size) {
        List(speedDialOptions.size) { Animatable(if (initialIsExpanded) 1f else 0f) }
    }

    LaunchedEffect(isExpanded, speedDialOptions) {
        subItemAnimatables.forEachIndexed { index, animatable ->
            launch {
                if (isExpanded) {
                    delay(index * 35L)
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                } else {
                    animatable.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 150)
                    )
                }
            }
        }
    }

    val fabColor = MaterialTheme.colorScheme.primaryContainer
    val iconColor = MaterialTheme.colorScheme.onPrimaryContainer
    val fabShape = RoundedCornerShape(14.dp) // Sharp Rounded Square

    Box(
        modifier = modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        // LAYER 1: Vector Canvas Liquid Bridge connecting right edge of dock to FAB
        if (progress > 0.01f && progress < 0.85f) {
            Canvas(modifier = Modifier.wrapContentSize()) {
                val centerPx = Offset(size.width / 2f, size.height / 2f)
                val dockOffsetPx = dockOffset.toPx()
                val fabXOffsetPx = fabCenterOffset.toPx()

                val dockRightPx = centerPx.x + dockOffsetPx + (navBarWidth.toPx() / 2f)
                val fabLeftPx = centerPx.x + fabXOffsetPx - (24.dp.toPx() * fabScale)

                if (fabLeftPx > dockRightPx - 12.dp.toPx()) {
                    val bridgePath = Path().apply {
                        val neckRadiusPx = 18.dp.toPx() * (1f - progress * 0.9f)
                        val dipPx = (1f - progress) * 12.dp.toPx()

                        val topDockY = centerPx.y - neckRadiusPx
                        val bottomDockY = centerPx.y + neckRadiusPx
                        val topFabY = centerPx.y - neckRadiusPx
                        val bottomFabY = centerPx.y + neckRadiusPx

                        val midX = (dockRightPx + fabLeftPx) / 2f

                        moveTo(dockRightPx, topDockY)
                        cubicTo(
                            midX, topDockY + dipPx,
                            midX, topFabY + dipPx,
                            fabLeftPx, topFabY
                        )
                        lineTo(fabLeftPx, bottomFabY)
                        cubicTo(
                            midX, bottomFabY - dipPx,
                            midX, bottomDockY - dipPx,
                            dockRightPx, bottomDockY
                        )
                        close()
                    }

                    drawPath(
                        path = bridgePath,
                        color = fabColor
                    )
                }
            }
        }

        // LAYER 2: Permanent Centered Floating NavBar Dock
        FloatingNavBar(
            currentScreenDestination = currentScreenDestination,
            onScreenDestinationSelected = onScreenDestinationSelected,
            modifier = Modifier.offset(x = dockOffset)
        )

        // LAYER 3: Action FAB & Speed Dial Sub-Buttons
        if (progress > 0.01f) {
            Box(
                modifier = Modifier
                    .offset(x = fabCenterOffset)
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                // Vertical Speed Dial Sub-Buttons
                speedDialOptions.forEachIndexed { index, option ->
                    val subProgress = subItemAnimatables.getOrNull(index)?.value ?: 0f

                    if (subProgress > 0.01f) {
                        val verticalYOffset = (-(index + 1) * 54 * subProgress).dp

                        Layout(
                            content = {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                    shadowElevation = 0.dp,
                                    tonalElevation = 0.dp,
                                    modifier = Modifier.clickable {
                                        isExpanded = false
                                        option.onClick()
                                    }
                                ) {
                                    Text(
                                        text = option.label,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        softWrap = false,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        )
                                    )
                                }

                                FloatingActionButton(
                                    onClick = {
                                        isExpanded = false
                                        option.onClick()
                                    },
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = FloatingActionButtonDefaults.elevation(
                                        0.dp,
                                        0.dp,
                                        0.dp,
                                        0.dp
                                    ),
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = option.iconRes),
                                        contentDescription = option.label,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            modifier = Modifier
                                .offset { IntOffset(0, verticalYOffset.toPx().roundToInt()) }
                                .graphicsLayer {
                                    scaleX = 0.3f + (0.7f * subProgress)
                                    scaleY = 0.3f + (0.7f * subProgress)
                                    alpha = subProgress.coerceIn(0f, 1f)
                                }
                        ) { measurables, constraints ->
                            val labelPlaceable = measurables[0].measure(constraints)
                            val fabPlaceable = measurables[1].measure(constraints)

                            val gapPx = 8.dp.roundToPx()
                            val totalWidth = labelPlaceable.width + gapPx + fabPlaceable.width
                            val totalHeight = maxOf(labelPlaceable.height, fabPlaceable.height)

                            val halfWidth = totalWidth / 2f
                            val subFabRadiusPx = fabPlaceable.width / 2f

                            // Align sub-button FAB center at parent x = 0 (primary FAB center)
                            val fabX = (halfWidth - subFabRadiusPx).roundToInt()
                            val labelX =
                                (halfWidth - subFabRadiusPx - gapPx - labelPlaceable.width).roundToInt()

                            layout(totalWidth, totalHeight) {
                                val labelY = (totalHeight - labelPlaceable.height) / 2
                                val fabY = (totalHeight - fabPlaceable.height) / 2

                                labelPlaceable.placeRelative(labelX, labelY)
                                fabPlaceable.placeRelative(fabX, fabY)
                            }
                        }
                    }
                }

                // Primary Trigger FAB Button
                FloatingActionButton(
                    onClick = {
                        if (speedDialOptions.isNotEmpty()) {
                            isExpanded = !isExpanded
                        } else {
                            onFabClick()
                        }
                    },
                    containerColor = fabColor,
                    contentColor = iconColor,
                    shape = fabShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
                    modifier = Modifier
                        .size(48.dp)
                        .graphicsLayer {
                            scaleX = fabScale
                            scaleY = fabScale
                            alpha = progress.coerceIn(0f, 1f)
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .size(18.dp)
                                .rotate(iconRotation)
                        ) {
                            val strokeWidthPx = 2.8.dp.toPx()
                            drawLine(
                                color = iconColor,
                                start = Offset(0f, size.height / 2f),
                                end = Offset(size.width, size.height / 2f),
                                strokeWidth = strokeWidthPx,
                                cap = StrokeCap.Round
                            )
                            drawLine(
                                color = iconColor,
                                start = Offset(size.width / 2f, 0f),
                                end = Offset(size.width / 2f, size.height),
                                strokeWidth = strokeWidthPx,
                                cap = StrokeCap.Round
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 200)
@Composable
fun GooMorphFloatingNavBarPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            GooMorphFloatingNavBar(
                currentScreenDestination = ScreenDestinations.SCHEDULES,
                onScreenDestinationSelected = {},
                isFabVisible = true,
                speedDialOptions = listOf(
                    SpeedDialOption("Add Course & Schedule", R.drawable.dock_icon) {}
                )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 250)
@Composable
fun GooMorphFloatingNavBarExpandedPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp),
            contentAlignment = Alignment.Center
        ) {
            GooMorphFloatingNavBar(
                currentScreenDestination = ScreenDestinations.TASKS,
                onScreenDestinationSelected = {},
                isFabVisible = true,
                initialIsExpanded = true,
                speedDialOptions = listOf(
                    SpeedDialOption("Add Assignment", R.drawable.bar_chart_icon) {},
                    SpeedDialOption("Add Project", R.drawable.person_icon) {}
                )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 380, heightDp = 220)
@Composable
fun GooMorphFloatingNavBarLongOptionPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            GooMorphFloatingNavBar(
                currentScreenDestination = ScreenDestinations.SCHEDULES,
                onScreenDestinationSelected = {},
                isFabVisible = true,
                initialIsExpanded = true,
                speedDialOptions = listOf(
                    SpeedDialOption("Add Course & Schedule", R.drawable.dock_icon) {}
                )
            )
        }
    }
}
