package com.github.henokt.reusablecomposemodifiers.ui.examples

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.henokt.reusablecomposemodifiers.R
import com.github.henokt.reusablecomposemodifiers.ui.modifiers.zoomable

@Composable
fun ZoomableImage(
    imageUri: String,
    modifier: Modifier = Modifier,
) {
    Box(modifier = Modifier) {
        AsyncImage(
            model = imageUri,
            placeholder = painterResource(R.drawable.placeholder),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxSize()
                .zoomable(
                    enabled = true,
                    minScale = 1f,
                    maxScale = 4f,
                    panningSpeedMultiplier = 2f,
                    doubleTapEnabled = true,
                    doubleTapScale = 3f,
                    animate = true,
                    scaleAnimationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    panAnimationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessVeryLow
                    ),
                    doubleTapScaleAnimationSpec = tween(durationMillis = 500),
                    doubleTapPanAnimationSpec = tween(durationMillis = 500),
                ),
            )
        Text(
            text = "Pinch to zoom, double-tap to zoom in/out, pan to explore zoomed image.",
            color = Color.LightGray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}