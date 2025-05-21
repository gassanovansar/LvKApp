package com.ansar.lvkapp.feature.photoEditor

import DraggableResizableImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.ansar.lvkapp.R

class PhotoEditorNew : Screen {
    @Composable
    override fun Content() {
        val res = LocalContext.current.resources
        var size by remember { mutableStateOf(IntSize.Zero) }
        val density = LocalDensity.current
        Box(
            modifier = Modifier
                .background(Color.Red)
                .fillMaxSize()
                .layout { measurable, _ ->
                    // Задаём очень большие ограничения
                    val placeable = measurable.measure(
                        androidx.compose.ui.unit.Constraints(
                            maxWidth = Int.MAX_VALUE,
                            maxHeight = Int.MAX_VALUE
                        )
                    )
                    // Layout будет размером placeable.width, даже если он больше экрана!
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                }
        ) {
            val imageWidthDp = with(density) { size.width.toDp() }
            val imageHeightDp = with(density) { size.height.toDp() }
            Box(
                modifier = Modifier
                    .align(Alignment.Center).layout { measurable, _ ->
                        // Задаём очень большие ограничения
                        val placeable = measurable.measure(
                            androidx.compose.ui.unit.Constraints(
                                maxWidth = Int.MAX_VALUE,
                                maxHeight = Int.MAX_VALUE
                            )
                        )
                        // Layout будет размером placeable.width, даже если он больше экрана!
                        layout(placeable.width, placeable.height) {
                            placeable.place(0, 0)
                        }
                    }
            ) {
                Box(
                    Modifier.clipToBounds().size(imageWidthDp,imageHeightDp)
                ) {
//                    DraggableResizableImage(
//                        imageRes = R.drawable.landscape2,
//                    )
                }
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .onSizeChanged {
                            println(it)
                            size = it
                        },
                    bitmap = ImageBitmap.imageResource(
                        res,
                        R.drawable.ic_user,
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }

        }
    }
}