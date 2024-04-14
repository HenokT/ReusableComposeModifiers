# Reusable Compose Modifiers

I created this repository with the intention of sharing useful Compose UI modifiers as I come up with them. 

## Modifier.zoomable

The first one of many to come is the [zoomable](app/src/main/java/com/github/henokt/reusablecomposemodifiers/ui/modifiers/zoomable.kt) modifier that makes the modified element zoomable by capturing pinching and panning gestures. It also supports double-tap gesture for auto zooming in and out.

An obvious use case for this modifier is when you want to create a zoomable image viewer as follows:

```kotlin
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
```

Here is what it will look like:

![zoomable-modifier-in-action](docs/media/zoomable_image.gif)

When your zoomable content is inside a container that has to detect touch gestures, like a `HorizontalPager` that relys on swipe gestures, you can apply the `zoomable` modifier to the container instead of the content. This way, the zoomable content will not interfere with the swipe gestures. Here is an example (you can see the full example [here](app/src/main/java/com/github/henokt/reusablecomposemodifiers/ui/examples/ZoomableImagePager.kt)): 

```kotlin
HorizontalPager(
    state = pagerState,
    modifier = modifier
        .fillMaxSize()
        .zoomable(),
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
``` 

And here is what it will look like:

![zoomable-modifier-in-action](docs/media/zoomable_image_pager.gif)