package com.github.henokt.reusablecomposemodifiers.ui.examples

import androidx.compose.ui.res.painterResource
import com.github.henokt.reusablecomposemodifiers.R
import com.github.henokt.reusablecomposemodifiers.ui.utils.zoomable

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.henokt.reusablecomposemodifiers.ui.theme.ReusableComposeModifiersTheme
import com.github.henokt.reusablecomposemodifiers.ui.utils.sampleImages

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
fun ZoomablePager(
    images: List<String>,
    modifier: Modifier = Modifier,
    initialPage: Int = 0,
) {
    val pagerState =
        rememberPagerState(initialPage = initialPage) { images.size }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Image ${pagerState.currentPage + 1} of ${images.size}",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                    )
                },
                modifier = Modifier
                    .statusBarsPadding(),
            )
        },
    ) { padding ->

        Text(
            text = "Image ${pagerState.currentPage + 1} of ${images.size}",
            style = MaterialTheme.typography.headlineMedium,
        )

HorizontalPager(
    state = pagerState,
    modifier = modifier
        .fillMaxSize()
        .padding(padding)
        .zoomable(
            enabled = true,
            minScale = 1f,
            maxScale = 4f,
            doubleTapEnabled = true,
            doubleTapScale = 3f,
            animate = true,
            // LibsTheme.animationSpecs.mediumSpring
            scaleAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            panAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
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
    }
}


@Preview
@Composable
fun ImagePagerPreview() {
    ReusableComposeModifiersTheme {
        ZoomablePager(
            images = sampleImages
        )
    }
}