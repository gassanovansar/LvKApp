package com.ansar.lvkapp.uiKit.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

interface AppColors {
    val white: Color
    val yellow1: Color
    val yellow2: Color
    val black: Color
    val blackAlpha: Color
    val gray: Color
    val gray1: Color
}


class AppColorsImpl(
    override val white: Color, override val yellow1: Color, override val yellow2: Color,
    override val black: Color, override val gray: Color, override val blackAlpha: Color,
    override val gray1: Color

) : AppColors

internal fun appLightColors() = AppColorsImpl(
    white = Color(0xFFFFFFFF),
    yellow1 = Color(0xFFFAE000),
    yellow2 = Color(0xFFFFC300),
    black = Color(0xFF000000),
    gray = Color(0xFFD9D9D9),
    gray1 = Color(0xFFB6B6B6),
    blackAlpha = Color(0x91000000)
)


internal fun appDarkColors() = AppColorsImpl(
    white = Color(0xFFFFFFFF),
    yellow1 = Color(0xFFFAE000),
    yellow2 = Color(0xFFFFC300),
    black = Color(0xFF000000),
    gray = Color(0xFFD9D9D9),
    gray1 = Color(0xFFB6B6B6),
    blackAlpha = Color(0x91000000)
)


internal val LocalColors = compositionLocalOf<AppColors> {
    error(
        "No colors provided! Make sure to wrap all usages of components in a " +
                "AppTheme."
    )
}
