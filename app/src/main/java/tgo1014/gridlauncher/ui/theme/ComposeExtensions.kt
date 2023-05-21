package tgo1014.gridlauncher.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

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

val isPreview @Composable get() = LocalInspectionMode.current