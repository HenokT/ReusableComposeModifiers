package com.github.henokt.reusablecomposemodifiers.ui.modifiers

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize

/**
 * Makes the modified element zoomable by capturing pinching and panning gestures. It also supports
 * double-tap gesture for zooming in and out.
 */
@Composable
fun Modifier.zoomable(
    enabled: Boolean = true,
    minScale: Float = 1f,
    maxScale: Float = 4f,
    panningSpeedMultiplier: Float = 2f,
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
    ),
    doubleTapScaleAnimationSpec: AnimationSpec<Float> = tween(durationMillis = 500),
    doubleTapPanAnimationSpec: AnimationSpec<Offset> = tween(durationMillis = 500),
): Modifier {
    if (!enabled) return this

    require(minScale > 0) { "minScale must be greater than 0" }
    require(minScale < maxScale) { "minScale must be less than maxScale" }
    require(panningSpeedMultiplier > 0) { "panningSpeedMultiplier must be greater than 0" }
    require(doubleTapScale in minScale..maxScale) {
        "doubleTapScale must be in the range of minScale and maxScale"
    }

    var isDoubleTap by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        scale,
        animationSpec = if (isDoubleTap) doubleTapScaleAnimationSpec else scaleAnimationSpec,
        label = ""
    )
    var offset by remember { mutableStateOf(Offset.Zero) }
    val animatedOffset by animateOffsetAsState(
        offset,
        animationSpec = if (isDoubleTap) doubleTapPanAnimationSpec else panAnimationSpec,
        label = ""
    )

    return this then Modifier
        .pointerInput(Unit) {
            detectTransformGestures { _, offsetChange, zoomChange, _ ->
                isDoubleTap = false
                scale = (scale * zoomChange).coerceIn(minScale, maxScale)
                // Update the offset to implement panning when zoomed.
                offset = when (scale) {
                    1f -> Offset.Zero
                    else -> {
                        val newOffset = offset + offsetChange * panningSpeedMultiplier
                        newOffset.coerceWithZoomContext(size, scale)
                    }
                }
            }
        }
        .let { modifier ->
            if (doubleTapEnabled) {
                modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = { targetOffset ->
                            isDoubleTap = true
                            if (scale == 1f) {
                                scale = doubleTapScale
                                val centroid = Offset(size.width / 2f, size.height / 2f)
                                val newOffset = (centroid - targetOffset) * scale
                                offset = newOffset.coerceWithZoomContext(size, scale)
                            } else {
                                scale = 1f
                                offset = Offset.Zero
                            }
                        }
                    )
                }
            } else {
                modifier
            }
        }
        .graphicsLayer(
            scaleX = if (animate) animatedScale else scale,
            scaleY = if (animate) animatedScale else scale,
            translationX = if (animate) animatedOffset.x else offset.x,
            translationY = if (animate) animatedOffset.y else offset.y
        )
}

private fun Offset.coerceWithZoomContext(
    size: IntSize,
    scale: Float,
): Offset {
    val maxOffsetX = size.width * (scale - 1) / 2
    val maxOffsetY = size.height * (scale - 1) / 2
    return Offset(
        x = x.coerceIn(-maxOffsetX, maxOffsetX),
        y = y.coerceIn(-maxOffsetY, maxOffsetY)
    )
}