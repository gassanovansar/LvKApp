package com.ansar.lvkapp.movable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ansar.lvkapp.MainScreen.State
import com.image.cropview.EdgeType
import com.image.cropview.ImageCrop
import kotlin.math.roundToInt

enum class ResizeType() {
    TopLeft, TopCenter, TopRight,
    LeftCenter, RightCenter,
    BottomLeft, BottomCenter, BottomRight,
}


private lateinit var imageCrop: ImageCrop

@Composable
fun Movable(
    modifier: Modifier = Modifier,
    image: ImageBitmap,
    state: State,
    cropOnClick: () -> Unit = {}
) {
    var image by remember { mutableStateOf(image) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    var width by remember { mutableStateOf(200F) }
    var height by remember { mutableStateOf(200F) }


    val density = LocalDensity.current

    var scale by remember { mutableStateOf(1f) }

    Box(
        modifier = Modifier
            .size(with(density) { width.toDp() }, with(density) { height.toDp() })
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(state) {
                detectTransformGestures { _, pan, zoom, _ ->
                    if (state == State.Resize) {

                        offsetX += pan.x //- if (zoom != 1F) width/2 else 0F
                        offsetY += pan.y //- if (zoom != 1F) height/2 else 0F

                        width *= zoom
                        height *= zoom

                        scale *= zoom
                    } else if (state == State.Crop) {

                    }

                }
            }
            .graphicsLayer {
//                scaleX = scale
//                scaleY = scale
//                // трансформация от центра
//                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }
    ) {


        if (state == State.Resize) {

            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            val minSize = 200

            ResizeHandle(Alignment.TopStart, ResizeType.TopLeft) { dx, dy ->
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
            ResizeHandle(Alignment.TopCenter, ResizeType.TopCenter) { _, dy ->
                val newHeight = height - dy
                if (newHeight >= minSize) {
                    offsetY += dy
                    height = newHeight
                }
            }

            // Top-Right
            ResizeHandle(Alignment.TopEnd, ResizeType.TopRight) { dx, dy ->
                val newWidth = width + dx
                val newHeight = height - dy
                if (newWidth >= minSize) width = newWidth
                if (newHeight >= minSize) {
                    offsetY += dy
                    height = newHeight
                }
            }

            // Right
            ResizeHandle(Alignment.CenterEnd, ResizeType.RightCenter) { dx, _ ->
                val newWidth = width + dx
                if (newWidth >= minSize) width = newWidth
            }

            // Bottom-Right
            ResizeHandle(Alignment.BottomEnd, ResizeType.BottomRight) { dx, dy ->
                val newWidth = width + dx
                val newHeight = height + dy
                if (newWidth >= minSize) width = newWidth
                if (newHeight >= minSize) height = newHeight
            }

            // Bottom
            ResizeHandle(Alignment.BottomCenter, ResizeType.BottomCenter) { _, dy ->
                val newHeight = height + dy
                if (newHeight >= minSize) height = newHeight
            }

            // Bottom-Left
            ResizeHandle(Alignment.BottomStart, ResizeType.BottomLeft) { dx, dy ->
                val newWidth = width - dx
                val newHeight = height + dy
                if (newWidth >= minSize) {
                    offsetX += dx
                    width = newWidth
                }
                if (newHeight >= minSize) height = newHeight
            }

            // Left
            ResizeHandle(Alignment.CenterStart, ResizeType.LeftCenter) { dx, _ ->
                val newWidth = width - dx
                if (newWidth >= minSize) {
                    offsetX += dx
                    width = newWidth
                }
            }
        } else if (state == State.Crop) {


            imageCrop = ImageCrop(bitmapImage = image.asAndroidBitmap())
            imageCrop.ImageCropView(
                modifier = Modifier.fillMaxSize(),
                guideLineColor = Color.LightGray,
                guideLineWidth = 2.dp,
                edgeCircleSize = 5.dp,
                edgeType = EdgeType.SQUARE
            )


            Button(
                modifier = Modifier
                    .height(60.dp)
                    .align(Alignment.BottomCenter)
                    .padding(start = 2.dp, end = 2.dp),
                onClick = {
                    val b = imageCrop.onCrop()
                    image = b.asImageBitmap()
                    imageCrop.resetView()
                }
            ) {
                Text(
                    text = "CropImage",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
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


@Composable
fun BoxScope.ResizeHandle(
    alignment: Alignment,
    resizeType: ResizeType,
    resizeSize: Dp = 24.dp,
    onDrag: (dx: Float, dy: Float) -> Unit
) {
    Box(
        modifier = Modifier
            .align(alignment)
//            .size(16.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount.x, dragAmount.y)
                }
            }
    ) {
        when (resizeType) {

            ResizeType.TopCenter, ResizeType.BottomCenter -> {
                Divider(
                    Modifier
                        .width(resizeSize)
                        .height(3.dp),
                    color = Color.Green
                )
            }

            ResizeType.LeftCenter, ResizeType.RightCenter -> {
                Divider(
                    Modifier
                        .height(resizeSize)
                        .width(3.dp),
                    color = Color.Green
                )
            }

            ResizeType.BottomRight, ResizeType.BottomLeft, ResizeType.TopRight, ResizeType.TopLeft -> {
                Divider(
                    Modifier
                        .align(alignment)
                        .height(resizeSize)
                        .width(3.dp),
                    color = Color.Green
                )
                Divider(
                    Modifier
                        .align(alignment)
                        .width(resizeSize)
                        .height(3.dp),
                    color = Color.Green
                )
            }
        }

    }
}