package tgo1014.gridlauncher.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import coil3.compose.AsyncImagePainter
import kotlin.random.Random

operator fun PaddingValues.plus(that: PaddingValues): PaddingValues = object : PaddingValues {
    override fun calculateBottomPadding(): Dp =
        this@plus.calculateBottomPadding() + that.calculateBottomPadding()

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        this@plus.calculateLeftPadding(layoutDirection) + that.calculateLeftPadding(layoutDirection)

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        this@plus.calculateRightPadding(layoutDirection) + that.calculateRightPadding(
            layoutDirection
        )

    override fun calculateTopPadding(): Dp =
        this@plus.calculateTopPadding() + that.calculateTopPadding()
}

fun Modifier.modifyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier =
    if (condition) this.then(modifier(Modifier)) else this

fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        this
    }
}

val isPreview @Composable get() = LocalInspectionMode.current

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun LazyListState.isScrollingDown() = this.isScrollInProgress && !isScrollingUp()

fun Modifier.onOpenNotificationShade(isOnTop: Boolean, onOpen: () -> Unit): Modifier {
    return this.pointerInput(isOnTop) {
        detectConsumedVerticalDragGestures(
            onVerticalDrag = { _, dragAmount ->
                if (isOnTop && dragAmount > 0) { // Swiping down
                    onOpen()
                }
            },
        )
    }
}

private val overshootEasing = Easing { fraction ->
    val tension = 2.0f
    var t = fraction
    if (t < 0.5f) {
        t *= 2.0f
        0.5f * t * t * ((tension + 1) * t - tension)
    } else {
        t = t * 2.0f - 2.0f
        0.5f * t * t * ((tension + 1) * t + tension) + 1.0f
    }
}

fun Modifier.flipRandomly() = composed {
    val animatedFloat = remember { Animatable(0f) }

    LaunchedEffect(animatedFloat) {
        animatedFloat.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    1000,
                    delayMillis = Random.nextInt(15_000, 30_000),
                    easing = overshootEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }
    this.graphicsLayer(rotationX = animatedFloat.value, cameraDistance = 300f)
}

fun Modifier.tileEditMode(isEditMode: Boolean) = composed {
    if (!isEditMode) {
        return@composed this
    }
    val animatedFloat = remember { Animatable(1f) }
    LaunchedEffect(animatedFloat) {
        animatedFloat.animateTo(
            targetValue = 0.85f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    600,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }
    this.scale(animatedFloat.value)
}

@Composable
fun AsyncImage(
    model: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) = coil3.compose.AsyncImage(
    model = model,
    contentDescription = contentDescription,
    modifier,
    transform,
    onState,
    alignment,
    contentScale,
    alpha,
    colorFilter,
    filterQuality
)