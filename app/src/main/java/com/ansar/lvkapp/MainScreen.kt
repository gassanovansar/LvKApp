package com.ansar.lvkapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlin.math.roundToInt

class MainScreen : Screen {

    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val res = LocalContext.current.resources
            var image1 by remember {
                mutableStateOf(
                    ImageBitmap.imageResource(
                        res,
                        R.drawable.landscape1,
                    )
                )
            }
            var image2 by remember {
                mutableStateOf(
                    ImageBitmap.imageResource(
                        res,
                        R.drawable.landscape2,
                    )
                )
            }
            var image3 by remember {
                mutableStateOf(
                    ImageBitmap.imageResource(
                        res,
                        R.drawable.landscape3,
                    )
                )
            }

            var state by remember { mutableStateOf(State.Resize) }
            Switch(
                modifier = Modifier.align(Alignment.Center),
                checked = state == State.Resize,
                onCheckedChange = {
                    state = when (state) {
                        State.Resize -> State.Crop
                        State.Crop -> State.Resize
                    }
                }
            )

            Movable(image = image1, state)
            Movable(image = image2, state)
            Movable(image = image3, state)
        }
    }
}


//+++TODO MOVE - двигать
//++TODO MOVE - двигать
//+++TODO ZOOM - по краям - оцентровать
//TODO CROP - обрезка


enum class State {
    Resize,
    Crop
}

@Composable

private fun Movable(
    image: ImageBitmap,
    state: State
) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var width by remember { mutableStateOf(200F) }
    var height by remember { mutableStateOf(200F) }


    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .size(with(density) { width.toDp() }, with(density) { height.toDp() })
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(state) {
                detectTransformGestures { _, pan, zoom, _ ->
                    if (state == State.Resize) {
                        offsetX += pan.x
                        offsetY += pan.y
                        width *= zoom
                        height *= zoom
                    } else if (state == State.Crop) {

                    }

                }
            }
    ) {
        Image(
            bitmap = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        if (state == State.Resize) {
            val minSize = 200

            ResizeHandle(Alignment.TopStart) { dx, dy ->
                val newWidth = width - dx
                val newHeight = height - dy
                if (newWidth >= minSize) {
                    offsetX += dx
                    width = newWidth
                }
                if (newHeight >= minSize) {
                    offsetY += dy
                    height = newHeight
                }
            }

            // Top
            ResizeHandle(Alignment.TopCenter) { _, dy ->
                val newHeight = height - dy
                if (newHeight >= minSize) {
                    offsetY += dy
                    height = newHeight
                }
            }

            // Top-Right
            ResizeHandle(Alignment.TopEnd) { dx, dy ->
                val newWidth = width + dx
                val newHeight = height - dy
                if (newWidth >= minSize) width = newWidth
                if (newHeight >= minSize) {
                    offsetY += dy
                    height = newHeight
                }
            }

            // Right
            ResizeHandle(Alignment.CenterEnd) { dx, _ ->
                val newWidth = width + dx
                if (newWidth >= minSize) width = newWidth
            }

            // Bottom-Right
            ResizeHandle(Alignment.BottomEnd) { dx, dy ->
                val newWidth = width + dx
                val newHeight = height + dy
                if (newWidth >= minSize) width = newWidth
                if (newHeight >= minSize) height = newHeight
            }

            // Bottom
            ResizeHandle(Alignment.BottomCenter) { _, dy ->
                val newHeight = height + dy
                if (newHeight >= minSize) height = newHeight
            }

            // Bottom-Left
            ResizeHandle(Alignment.BottomStart) { dx, dy ->
                val newWidth = width - dx
                val newHeight = height + dy
                if (newWidth >= minSize) {
                    offsetX += dx
                    width = newWidth
                }
                if (newHeight >= minSize) height = newHeight
            }

            // Left
            ResizeHandle(Alignment.CenterStart) { dx, _ ->
                val newWidth = width - dx
                if (newWidth >= minSize) {
                    offsetX += dx
                    width = newWidth
                }
            }
        } else if (state == State.Crop) {

        }
//        //TODO FIX ALL ROUND
//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .size(20.dp)
//                .background(Color.DarkGray, shape = CircleShape)
//                .pointerInput(Unit) {
//                    detectDragGestures { change, dragAmount ->
//                        width += with(density) { dragAmount.x.toDp() }
//                        height += with(density) { dragAmount.y.toDp() }
//                    }
//                }
//        )


    }
}

@Composable
fun BoxScope.ResizeHandle(
    alignment: Alignment,
    onDrag: (dx: Float, dy: Float) -> Unit
) {
    Box(
        modifier = Modifier
            .align(alignment)
            .size(16.dp)
            .background(Color.DarkGray, shape = CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            }
    )
}