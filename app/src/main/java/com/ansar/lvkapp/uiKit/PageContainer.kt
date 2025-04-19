package com.ansar.lvkapp.uiKit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp


@Composable
fun PageContainer(
    modifier: Modifier = Modifier,
    background: Color = Color.White,
    backgroundImage: Painter? = null,
    brush: Brush? = null,
    fill: Boolean = true,
    isLoading: State<Boolean> = mutableStateOf(false),
//    error: State<Notification?> = mutableStateOf(null),
    header: @Composable (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
    footer: @Composable (BoxScope.() -> Unit)? = null,
) {
    var modifier = if (brush != null) {
        modifier.background(brush)
    } else {
        modifier.background(if (backgroundImage != null) Color.Transparent else background)
    }
    modifier = if (fill) {
        modifier
            .fillMaxHeight()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    } else {
        modifier.padding(bottom = 12.dp)
    }
    val localFocusManager = LocalFocusManager.current

    Box {
        if (backgroundImage != null) {
            Image(
                painter = backgroundImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        localFocusManager.clearFocus()
                    })
                }
        ) {
            header?.invoke()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill)
            ) {
                content()
            }
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                footer?.invoke(this)
            }


        }
    }


//    NotificationCenter(error.value)
//
//    if (isLoading.value) {
//        LoadingView(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Black.copy(alpha = 0.5F))
//        )
//    }


}

