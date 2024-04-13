package com.github.henokt.reusablecomposemodifiers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.github.henokt.reusablecomposemodifiers.ui.examples.ZoomablePager
import com.github.henokt.reusablecomposemodifiers.ui.theme.ReusableComposeModifiersTheme
import com.github.henokt.reusablecomposemodifiers.ui.utils.sampleImages

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge()
        setContent {
            ReusableComposeModifiersTheme {
               ZoomablePager(images = sampleImages, modifier = Modifier.fillMaxSize())
            }
        }
    }
}
