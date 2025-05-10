//package com.ansar.lvkapp.base
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.max
//import kotlin.math.roundToInt
//
//@Composable
//fun DraggableResizableImage(
//    imageRes: Int,
//    minSize: Dp = 64.dp
//) {
//    val density = LocalDensity.current
//    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
//    var size by remember { mutableStateOf(200.dp) } // стартовый размер
//    val handleSize = 24.dp
//
//    Box(
//        modifier = Modifier
//            .offset {
//                with(density) {
//                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
//                }
//            }
//            .size(size)
//            .pointerInput(Unit) {
//                detectDragGestures { change, dragAmount ->
//                    change.consume()
//                    offset += dragAmount
//                }
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = painterResource(id = imageRes),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.matchParentSize()
//        )
//        // Левый верхний угол
//        ResizeHandle(
//            modifier = Modifier.align(Alignment.TopStart),
//            onDrag = { dragAmount ->
//                with(density) {
//                    val newSize = max(size - dragAmount.x.toDp(), minSize)
//                    val deltaPx = (size - newSize).toPx()
//                    size = newSize
//                    offset += Offset(deltaPx, deltaPx)
//                }
//            },
//            handleSize = handleSize
//        )
//        // Правый верхний угол
//        ResizeHandle(
//            modifier = Modifier.align(Alignment.TopEnd),
//            onDrag = { dragAmount ->
//                with(density) {
//                    val newSize = max(size + dragAmount.x.toDp(), minSize)
//                    // Если размер уменьшился, смещаем offset только по вертикали
//                    val delta = newSize - size
//                    offset += Offset(0f, -delta.toPx())
//                    size = newSize
//                }
//            },
//            handleSize = handleSize
//        )
//        // Левый нижний угол
//        ResizeHandle(
//            modifier = Modifier.align(Alignment.BottomStart),
//            onDrag = { dragAmount ->
//                with(density) {
//                    val newSize = max(size - dragAmount.x.toDp(), minSize)
//                    val deltaPx = (size - newSize).toPx()
//                    size = newSize
//                    offset += Offset(deltaPx, 0f)
//                }
//            },
//            handleSize = handleSize
//        )
//        // Правый нижний угол
//        ResizeHandle(
//            modifier = Modifier.align(Alignment.BottomEnd),
//            onDrag = { dragAmount ->
//                with(density) {
//                    size = max(size + dragAmount.x.toDp(), minSize)
//                }
//            },
//            handleSize = handleSize
//        )
//    }
//}
//
//@Composable
//private fun ResizeHandle(
//    modifier: Modifier = Modifier,
//    onDrag: (Offset) -> Unit,
//    handleSize: Dp
//) {
//    Box(
//        modifier = modifier
//            .size(handleSize)
//            .background(Color.White, CircleShape)
//            .pointerInput(Unit) {
//                detectDragGestures { change, dragAmount ->
//                    change.consume()
//                    onDrag(dragAmount)
//                }
//            }
//    )
//}