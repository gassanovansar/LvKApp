package com.ansar.lvkapp.uiKit.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

//TODO MOVE TO MANAGER
internal val isDarkTheme = MutableStateFlow(false)


@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    val isDarkTheme by isDarkTheme.collectAsState()
    val typography: AppTypography = textStyles()
    val darkTheme: Boolean = isDarkTheme
    CompositionLocalProvider(
        LocalColors provides if (darkTheme) appDarkColors() else appLightColors(),
        LocalTypography provides typography,
        LocalViewConfiguration provides LocalViewConfiguration.current.updateViewConfiguration()
    ) {
        content()
    }
}

object AppTheme {


    var isDark: Boolean
        get() = isDarkTheme.value
        set(value) {
            isDarkTheme.value = value
        }


    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

}


fun ViewConfiguration.updateViewConfiguration() = object : ViewConfiguration {
    override val longPressTimeoutMillis
        get() = this@updateViewConfiguration.longPressTimeoutMillis

    override val doubleTapTimeoutMillis
        get() = this@updateViewConfiguration.doubleTapTimeoutMillis

    override val doubleTapMinTimeMillis
        get() = this@updateViewConfiguration.doubleTapMinTimeMillis

    override val touchSlop: Float
        get() = this@updateViewConfiguration.touchSlop
    override val minimumTouchTargetSize: DpSize
        get() = DpSize(40.dp, 40.dp)
}
