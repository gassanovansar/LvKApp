import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun DraggableResizableImage(
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    // Получаем оригинальный размер изображения (в px)
    val originalSize = remember(imageRes) {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeResource(context.resources, imageRes, options)
        options.outWidth to options.outHeight
    }

    // Сохраняем исходное соотношение сторон
    val aspectRatio = remember(originalSize) {
        if (originalSize.second != 0) {
            originalSize.first.toFloat() / originalSize.second.toFloat()
        } else 1f
    }

    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    var width by remember {
        mutableStateOf(with(density) { originalSize.first.toDp() })
    }
    var height by remember {
        mutableStateOf(with(density) { originalSize.second.toDp() })
    }

    val minSize = 48.dp
    val handleSize = 24.dp
    val handleColor = Color.Blue
    val sensitivity = 3.5f // или даже 4.0f

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .size(width, height)
                .border(2.dp, Color.Gray)
        ) {
            // Перемещение только по картинке!
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                        }
                    }
            )

            fun resizeProportional(
                dragAmount: Offset,
                fixLeft: Boolean,
                fixTop: Boolean
            ) {
                // Для каждого угла направление должно быть разным:
                val deltaPx = when {
                    // TopLeft: тянем влево-вверх — увеличиваем, вправо-вниз — уменьшаем
                    fixLeft && fixTop -> -(dragAmount.x + dragAmount.y)
                    // TopRight: тянем вправо-вверх — увеличиваем, влево-вниз — уменьшаем
                    !fixLeft && fixTop -> dragAmount.x - dragAmount.y
                    // BottomLeft: тянем влево-вниз — увеличиваем, вправо-вверх — уменьшаем
                    fixLeft && !fixTop -> -dragAmount.x + dragAmount.y
                    // BottomRight: тянем вправо-вниз — увеличиваем, влево-вверх — уменьшаем
                    else -> dragAmount.x + dragAmount.y
                } / sensitivity

                val widthPx = with(density) { width.toPx() }
                val heightPx = with(density) { height.toPx() }
                val minPx = with(density) { minSize.toPx() }

                val newWidthPx = (widthPx + deltaPx).coerceAtLeast(minPx)
                val newHeightPx = newWidthPx / aspectRatio

                val widthDiffPx = newWidthPx - widthPx
                val heightDiffPx = newHeightPx - heightPx

                val newOffset = offset.copy(
                    x = offset.x - if (fixLeft) widthDiffPx else 0f,
                    y = offset.y - if (fixTop) heightDiffPx else 0f
                )
                offset = newOffset
                width = with(density) { newWidthPx.toDp() }
                height = with(density) { newHeightPx.toDp() }
            }

            // Top-Left (фиксируем левую и верхнюю стороны)
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.TopStart)
                    .background(handleColor, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            resizeProportional(dragAmount, fixLeft = true, fixTop = true)
                        }
                    }
            )
// Top-Right (фиксируем правую и верхнюю стороны)
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.TopEnd)
                    .background(handleColor, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            resizeProportional(dragAmount, fixLeft = false, fixTop = true)
                        }
                    }
            )
// Bottom-Left (фиксируем левую и нижнюю стороны)
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.BottomStart)
                    .background(handleColor, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            resizeProportional(dragAmount, fixLeft = true, fixTop = false)
                        }
                    }
            )
// Bottom-Right (фиксируем правую и нижнюю стороны)
            Box(
                Modifier
                    .size(handleSize)
                    .align(Alignment.BottomEnd)
                    .background(handleColor, CircleShape)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            resizeProportional(dragAmount, fixLeft = false, fixTop = false)
                        }
                    }
            )





            // Левая ручка (по центру левого края)
            Box(
                Modifier
                    .size(handleSize, handleSize)
                    .align(Alignment.CenterStart)
                    .background(Color.Green, CircleShape)
                // .pointerInput(...) — логику crop добавим позже
            )

// Правая ручка (по центру правого края)
            Box(
                Modifier
                    .size(handleSize, handleSize)
                    .align(Alignment.CenterEnd)
                    .background(Color.Green, CircleShape)
                // .pointerInput(...) — логику crop добавим позже
            )

// Верхняя ручка (по центру верхнего края)
            Box(
                Modifier
                    .size(handleSize, handleSize)
                    .align(Alignment.TopCenter)
                    .background(Color.Green, CircleShape)
                // .pointerInput(...) — логику crop добавим позже
            )

// Нижняя ручка (по центру нижнего края)
            Box(
                Modifier
                    .size(handleSize, handleSize)
                    .align(Alignment.BottomCenter)
                    .background(Color.Green, CircleShape)
                // .pointerInput(...) — логику crop добавим позже
            )
















        }
    }
}