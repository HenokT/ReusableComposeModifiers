@file:OptIn(ExperimentalFoundationApi::class)

package com.github.henokt.reusablecomposemodifiers.ui.examples

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.henokt.reusablecomposemodifiers.R
import com.github.henokt.reusablecomposemodifiers.ui.modifiers.zoomable
import com.github.henokt.reusablecomposemodifiers.ui.theme.ReusableComposeModifiersTheme
import com.github.henokt.reusablecomposemodifiers.ui.utils.sampleImages

@OptIn(
    ExperimentalFoundationApi::class,
)
@Composable
fun ZoomableImagePager(
    images: List<String>,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
    pagerState: PagerState =
        rememberPagerState(initialPage = initialPage) { images.size },
) {

    Box(modifier = Modifier) {
        HorizontalPager(
            state = pagerState,
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
        ) { page ->

            val image = images[page]
            AsyncImage(
                model = image,
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(
            text = "Swipe to navigate, pinch to zoom, double-tap to zoom in/out, pan to explore zoomed image.",
            color = Color.LightGray,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}


@Preview
@Composable
fun ImagePagerPreview() {
    ReusableComposeModifiersTheme {
        ZoomableImagePager(
            images = sampleImages
        )
    }
}