package com.github.henokt.reusablecomposemodifiers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.henokt.reusablecomposemodifiers.ui.examples.ZoomableImage
import com.github.henokt.reusablecomposemodifiers.ui.examples.ZoomableImagePager
import com.github.henokt.reusablecomposemodifiers.ui.theme.ReusableComposeModifiersTheme
import com.github.henokt.reusablecomposemodifiers.ui.utils.sampleImages
import kotlinx.coroutines.launch

sealed class Example(val title: String) {
    data object ZoomableImage : Example("Zoomable Image")
    data object ZoomableImagePager : Example("Zoomable Image Pager")

    companion object {
        /**
         * Fore some reason if we don't use lazy here, the values list will contain null values.
         * It's probably an issue with the way sealed classes are implemented in Kotlin.
         */
        val values by lazy { listOf(ZoomableImage, ZoomableImagePager) }
    }
}

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            ReusableComposeModifiersTheme {
                var currentExample by remember { mutableStateOf<Example>(Example.ZoomableImage) }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Column(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = "Examples",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                )

                                LazyColumn(
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    items(Example.values) { example ->
                                        NavigationDrawerItem(
                                            label = { Text(text = example.title) },
                                            selected = example == currentExample,
                                            shape = RoundedCornerShape(
                                                topStartPercent = 0,
                                                bottomStartPercent = 0,
                                                topEndPercent = 50,
                                                bottomEndPercent = 50
                                            ),
                                            onClick = {
                                                currentExample = example
                                                scope.launch {
                                                    drawerState.close()
                                                }
                                            },
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    },
                ) {
                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = currentExample.title,
                                        style = MaterialTheme.typography.headlineMedium,
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Menu,
                                            contentDescription = null,
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .statusBarsPadding(),
                            )
                        },
                    ) { padding ->

                        AnimatedContent(
                            targetState = currentExample,
                            label = "",
                            transitionSpec = {
                                slideIn(
                                    animationSpec = tween(500)
                                ) { fullSize ->
                                    IntOffset(fullSize.width, 0)
                                }.togetherWith(
                                    slideOut(
                                        animationSpec = tween(500)
                                    ) { fullSize ->
                                        IntOffset(-fullSize.width, 0)
                                    }
                                )
                            }
                        ) {
                            when (it) {
                                is Example.ZoomableImage -> {
                                    ZoomableImage(
                                        imageUri = sampleImages.random(),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding)
                                    )
                                }

                                is Example.ZoomableImagePager -> {
                                    val shuffledImages = remember {
                                        sampleImages.shuffled()
                                    }
                                    ZoomableImagePager(
                                        images = shuffledImages,
                                        initialPage = 0,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding)
                                    )
                                }
                            }
                        }

                    }
                }

            }
        }
    }
}

