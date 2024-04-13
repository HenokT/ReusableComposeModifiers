# Reusable Compose Modifiers

I created this repository with the intention of sharing useful Compose UI modifiers as I come up with them. 

## Modifier.zoomable

The first one of hopefully many to come is the `zoomable` modifier that makes the modified element zoomable by capturing pinching and panning gestures. It also supports double-tap gesture for zooming in and out.

In the following example, the `zoomable` modifier is applied to a `HorizontalPager` that lets you page through a list of images. You can see the full example [here](app/src/main/java/com/github/henokt/reusablecomposemodifiers/ui/examples/ZoomablePager.kt).

```kotlin
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
``` 

The result is as follows:

![zoomable-modifier-in-action](https://raw.githubusercontent.com/HenokT/ReusableComposeModifiers/main/docs/media/zoomable-modifier-demo.webm)