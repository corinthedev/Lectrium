// Copyright (c) 2026 Corin. All rights reserved. Owned by Corin.
package org.corin.lectrium.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BouncyCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = MaterialTheme.colorScheme.outline
) {
    // Scale animation: 0.55f -> 1f with bouncy spring on checked, quicker tween on uncheck
    val scaleProgress by animateFloatAsState(
        targetValue = if (checked) 1f else 0.55f,
        animationSpec = if (checked) {
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        } else {
            tween(durationMillis = 180)
        },
        label = "CheckboxScaleSpec"
    )

    // Background color animation
    val backgroundColor by animateColorAsState(
        targetValue = if (checked) checkedColor else Color.Transparent,
        animationSpec = if (checked) tween(durationMillis = 200) else tween(durationMillis = 150),
        label = "CheckboxColorSpec"
    )

    // Outline color animation
    val borderColor by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedColor,
        animationSpec = tween(durationMillis = 200),
        label = "CheckboxBorderSpec"
    )

    // Checkmark path trim animation (0f -> 1f) starting ~90ms after scale begins
    val checkmarkProgress = remember { Animatable(if (checked) 1f else 0f) }

    // One-shot ripple animation
    val rippleScale = remember { Animatable(1f) }
    val rippleAlpha = remember { Animatable(0f) }

    LaunchedEffect(checked) {
        if (checked) {
            delay(90)
            checkmarkProgress.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        } else {
            checkmarkProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 120)
            )
        }
    }

    LaunchedEffect(checked) {
        if (checked) {
            rippleScale.snapTo(1f)
            rippleAlpha.snapTo(0.4f)
            launch {
                rippleScale.animateTo(
                    targetValue = 1.55f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            launch {
                rippleAlpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 350)
                )
            }
        }
    }

    Box(
        modifier = modifier
            .size(size * 1.5f)
            .clip(CircleShape)
            .then(
                if (onCheckedChange != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onCheckedChange(!checked)
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 1.5f)) {
            val center = Offset(size.toPx() * 0.75f, size.toPx() * 0.75f)
            val radius = (size.toPx() / 2f) * scaleProgress

            // 1. One-shot ripple
            if (rippleAlpha.value > 0f) {
                drawCircle(
                    color = checkedColor.copy(alpha = rippleAlpha.value),
                    radius = (size.toPx() / 2f) * rippleScale.value,
                    center = center
                )
            }

            // 2. Circle Background & Border
            if (checked) {
                drawCircle(
                    color = backgroundColor,
                    radius = radius,
                    center = center
                )
            } else {
                drawCircle(
                    color = borderColor,
                    radius = radius,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            // 3. Trimmed Checkmark Path
            if (checked && checkmarkProgress.value > 0f) {
                val path = Path().apply {
                    val boxSize = size.toPx()
                    val startX = center.x - boxSize * 0.22f
                    val startY = center.y
                    val midX = center.x - boxSize * 0.05f
                    val midY = center.y + boxSize * 0.18f
                    val endX = center.x + boxSize * 0.24f
                    val endY = center.y - boxSize * 0.18f

                    moveTo(startX, startY)
                    lineTo(midX, midY)
                    lineTo(endX, endY)
                }

                val pathMeasure = PathMeasure()
                pathMeasure.setPath(path, false)

                val trimmedPath = Path()
                pathMeasure.getSegment(
                    startDistance = 0f,
                    stopDistance = pathMeasure.length * checkmarkProgress.value,
                    destination = trimmedPath,
                    startWithMoveTo = true
                )

                drawPath(
                    path = trimmedPath,
                    color = Color.White,
                    style = Stroke(
                        width = 2.5.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }
    }
}
