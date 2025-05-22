import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt

@Composable
fun DraggableResizableImage(
    modifier: Modifier = Modifier,
    original: Pair<Int, Int>,
    border : Boolean,
    resultOffset: (Offset, Dp, Dp, Boolean) -> Unit,

    ) {
    val context = LocalContext.current
    val density = LocalDensity.current

    // Получаем оригинальный размер изображения (в px)
    val originalSize = remember(original) { original }

    // Сохраняем исходное соотношение сторон
    val aspectRatio = remember(originalSize) {
        if (originalSize.second != 0) {
            originalSize.first.toFloat() / originalSize.second.toFloat()
        } else 1f
    }
    var offset by remember { mutableStateOf(Offset(880f, 850f)) }
    var width by remember {
        mutableStateOf(with(density) { originalSize.first.toDp() })
    }
    var height by remember {
        mutableStateOf(with(density) { originalSize.second.toDp() })
    }

    val minSize = 48.dp
    val handleSize = 24.dp
    val sensitivity = 2.0f // или даже 4.0f


//    Image(
//        painter = painterResource(id = imageRes),
//        contentDescription = null,
//        contentScale = ContentScale.FillBounds,
//        modifier = modifier
//            .offset {
//                IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
//            }
//            .size(width, height)
//    )
    Box(
        modifier = modifier
            .zIndex(if (border) 1F else 0F)
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(width, height)
            .border(2.dp, if (border) Color.Gray else Color.Transparent)
            .pointerInput(Unit) {
                detectDragGestures(onDragStart = { startOffset ->
                    resultOffset(offset, width, height, true)
                }) { change, dragAmount ->
                    change.consume()
                    offset += dragAmount
                    resultOffset(offset, width, height, false)
                }
            }

    ) {
        // Перемещение только по картинке!
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
            resultOffset(offset, width, height, false)
        }

        // Top-Left (фиксируем левую и верхнюю стороны)
        Box(
            Modifier
                .size(handleSize)
                .align(Alignment.TopStart)
                .background(if (border) Color.Gray else Color.Transparent, CircleShape)
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
                .background(if (border) Color.Gray else Color.Transparent, CircleShape)
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
                .background(if (border) Color.Gray else Color.Transparent, CircleShape)
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
                .background(if (border) Color.Gray else Color.Transparent, CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        resizeProportional(dragAmount, fixLeft = false, fixTop = false)
                    }
                }
        )

    }

}