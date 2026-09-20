// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

enum class IndicatorStyle {
    /** Smooth continuous width expansion & color interpolation driven directly by scroll position */
    ElasticPill,
    /** Active dot with an outer glowing halo accent */
    HaloDot,
    /** Animated pill morph with spring physics */
    SpringPill
}

/**
 * A highly customizable, fluid Page Indicator supporting multiple modern animation styles.
 *
 * @param pagerState The [PagerState] driving page changes and scroll offsets.
 * @param modifier Modifier for layout and positioning.
 * @param style Visual style mode ([IndicatorStyle.ElasticPill], [IndicatorStyle.HaloDot], or [IndicatorStyle.SpringPill]).
 * @param activeColor Color for the active indicator.
 * @param inactiveColor Color for unselected indicators.
 * @param indicatorHeight Height/diameter of the indicator shapes.
 * @param indicatorWidth Width of unselected circle indicators.
 * @param activeIndicatorWidth Expanded width for selected active pill indicators.
 * @param indicatorSpacing Distance between each indicator element.
 */
@Composable
fun PageIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    style: IndicatorStyle = IndicatorStyle.ElasticPill,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    indicatorHeight: Dp = 10.dp,
    indicatorWidth: Dp = 10.dp,
    activeIndicatorWidth: Dp = 28.dp,
    indicatorSpacing: Dp = 10.dp,
) {
    val pageCount = pagerState.pageCount
    if (pageCount <= 0) return

    val currentPosition by remember {
        derivedStateOf { pagerState.currentPage + pagerState.currentPageOffsetFraction }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(indicatorSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { pageIndex ->
            val distance = abs(currentPosition - pageIndex)
            val progress = (1f - distance).coerceIn(0f, 1f)

            when (style) {
                IndicatorStyle.ElasticPill -> {
                    // Continuous width scaling responding live to touch swipes
                    val width = indicatorWidth + ((activeIndicatorWidth - indicatorWidth) * progress)
                    val color = lerp(inactiveColor, activeColor, progress)

                    Box(
                        modifier = Modifier
                            .width(width)
                            .height(indicatorHeight)
                            .clip(CircleShape)
                            .background(color)
                    )
                }

                IndicatorStyle.HaloDot -> {
                    val isSelected = pageIndex == pagerState.currentPage
                    val haloColor = if (isSelected) activeColor.copy(alpha = 0.2f) else Color.Transparent
                    val color = lerp(inactiveColor, activeColor, progress)
                    val size = indicatorWidth + (4.dp * progress)

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(activeIndicatorWidth)
                            .height(indicatorHeight + 8.dp)
                    ) {
                        // Halo background ring
                        Box(
                            modifier = Modifier
                                .width(size + 8.dp)
                                .height(indicatorHeight + 8.dp)
                                .clip(CircleShape)
                                .background(haloColor)
                        )
                        // Core dot
                        Box(
                            modifier = Modifier
                                .width(size)
                                .height(size)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }

                IndicatorStyle.SpringPill -> {
                    val isSelected = pageIndex == pagerState.currentPage

                    val animatedWidth by animateDpAsState(
                        targetValue = if (isSelected) activeIndicatorWidth else indicatorWidth,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "springWidth"
                    )

                    val animatedColor by animateColorAsState(
                        targetValue = if (isSelected) activeColor else inactiveColor,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "springColor"
                    )

                    Box(
                        modifier = Modifier
                            .width(animatedWidth)
                            .height(indicatorHeight)
                            .clip(CircleShape)
                            .background(animatedColor)
                    )
                }
            }
        }
    }
}

