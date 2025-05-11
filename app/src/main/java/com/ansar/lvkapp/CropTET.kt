package com.ansar.lvkapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ImageCropper(
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val bitmap = remember {
        BitmapFactory.decodeResource(context.resources, imageRes)
    }
    val imageWidth = bitmap.width
    val imageHeight = bitmap.height

    // Размеры crop-области (в px)
    var cropLeft by remember { mutableStateOf(imageWidth * 0.2f) }
    var cropTop by remember { mutableStateOf(imageHeight * 0.2f) }
    var cropRight by remember { mutableStateOf(imageWidth * 0.8f) }
    var cropBottom by remember { mutableStateOf(imageHeight * 0.8f) }

    // Для отображения в dp
    val boxWidthDp = with(density) { imageWidth.toDp() }
    val boxHeightDp = with(density) { imageHeight.toDp() }

    Box(
        modifier = modifier
            .size(boxWidthDp, boxHeightDp)
            .background(Color.Black)
    ) {
        // Само изображение
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Crop-рамка
        Box(
            Modifier
                .offset {
                    IntOffset(
                        (cropLeft / imageWidth * boxWidthDp.value).roundToInt(),
                        (cropTop / imageHeight * boxHeightDp.value).roundToInt()
                    )
                }
                .size(
                    width = ((cropRight - cropLeft) / imageWidth * boxWidthDp.value).dp,
                    height = ((cropBottom - cropTop) / imageHeight * boxHeightDp.value).dp
                )
                .border(2.dp, Color.Yellow)
        ) {
            // Угловые ручки (пример: верхний левый)
            val handleSize = 24.dp

            // Top-Left
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.TopStart)
                    .background(Color.Yellow, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            cropLeft = (cropLeft + dragAmount.x / boxWidthDp.value * imageWidth).coerceIn(0f, cropRight - 40f)
                            cropTop = (cropTop + dragAmount.y / boxHeightDp.value * imageHeight).coerceIn(0f, cropBottom - 40f)
                        }
                    }
            )
            // Top-Right
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.TopEnd)
                    .background(Color.Yellow, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            cropRight = (cropRight + dragAmount.x / boxWidthDp.value * imageWidth).coerceIn(cropLeft + 40f, imageWidth.toFloat())
                            cropTop = (cropTop + dragAmount.y / boxHeightDp.value * imageHeight).coerceIn(0f, cropBottom - 40f)
                        }
                    }
            )
            // Bottom-Left
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.BottomStart)
                    .background(Color.Yellow, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            cropLeft = (cropLeft + dragAmount.x / boxWidthDp.value * imageWidth).coerceIn(0f, cropRight - 40f)
                            cropBottom = (cropBottom + dragAmount.y / boxHeightDp.value * imageHeight).coerceIn(cropTop + 40f, imageHeight.toFloat())
                        }
                    }
            )
            // Bottom-Right
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.BottomEnd)
                    .background(Color.Yellow, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            cropRight = (cropRight + dragAmount.x / boxWidthDp.value * imageWidth).coerceIn(cropLeft + 40f, imageWidth.toFloat())
                            cropBottom = (cropBottom + dragAmount.y / boxHeightDp.value * imageHeight).coerceIn(cropTop + 40f, imageHeight.toFloat())
                        }
                    }
            )
        }
    }

    // Кнопка "Обрезать" (пример)
    Spacer(Modifier.height(16.dp))
    Button(onClick = {
        // Обрезка bitmap по выделенной области
        val cropped = Bitmap.createBitmap(
            bitmap,
            cropLeft.toInt(),
            cropTop.toInt(),
            (cropRight - cropLeft).toInt(),
            (cropBottom - cropTop).toInt()
        )
        // ... используйте cropped как нужно (например, покажите в Image)
    }) {
        Text("Обрезать")
    }
}