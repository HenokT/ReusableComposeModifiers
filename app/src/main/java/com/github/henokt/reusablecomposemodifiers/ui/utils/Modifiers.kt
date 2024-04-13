package com.github.henokt.reusablecomposemodifiers.ui.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

/**
 * Makes the modified element zoomable by capturing pinching and panning gestures. Supports
 * double-tap to zoom in and out.
 */
@Composable
fun Modifier.zoomable(
    enabled: Boolean = true,
    minScale: Float = 1f,
    maxScale: Float = 4f,
    panningSpeedMultiplier: Float = 4f,
    doubleTapEnabled: Boolean = true,
    doubleTapScale: Float = 3f,
    animate: Boolean = true,
    scaleAnimationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    ),
    panAnimationSpec: AnimationSpec<Offset> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessVeryLow
    )
): Modifier {
    if (!enabled) return this

    require(minScale > 0) { "minScale must be greater than 0" }
    require(minScale < maxScale) { "minScale must be less than maxScale" }
    require(panningSpeedMultiplier > 0) { "panningSpeedMultiplier must be greater than 0" }
    require(doubleTapScale in minScale..maxScale) {
        "doubleTapScale must be in the range of minScale and maxScale"
    }

    var scale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(scale, animationSpec = scaleAnimationSpec)
    var offset by remember { mutableStateOf(Offset.Zero) }
    val animatedOffset by animateOffsetAsState(offset, animationSpec = panAnimationSpec)

    return this then Modifier.pointerInput(Unit) {
        detectTransformGestures { _, offsetChange, zoomChange, _ ->
            scale = (scale * zoomChange).coerceIn(minScale, maxScale)

            // Update the offset to implement panning when zoomed.
            offset = when (scale) {
                1f -> Offset.Zero
                else -> {
                    val newOffset = offset + offsetChange * panningSpeedMultiplier
                    val maxOffsetX = size.width * (scale - 1) / 2
                    val maxOffsetY = size.height * (scale - 1) / 2
                    Offset(
                        x = newOffset.x.coerceIn(-maxOffsetX, maxOffsetX),
                        y = newOffset.y.coerceIn(-maxOffsetY, maxOffsetY)
                    )
                }
            }
        }
    }.let {
        if (doubleTapEnabled) {
            it.pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale == 1f) {
                            scale = doubleTapScale
                        } else {
                            scale = 1f
                            offset = Offset.Zero
                        }
                    }
                )
            }
        } else {
            it
        }
    }.graphicsLayer(
        scaleX = if (animate) animatedScale else scale,
        scaleY = if (animate) animatedScale else scale,
        translationX = if (animate) animatedOffset.x else offset.x,
        translationY = if (animate) animatedOffset.y else offset.y
    )
}