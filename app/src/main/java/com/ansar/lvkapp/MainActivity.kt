package com.ansar.lvkapp

import DraggableResizableImage
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.ansar.lvkapp.feature.photoEditor.PhotoEditorNew
import com.ansar.lvkapp.uiKit.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    BottomSheetNavigator(
                        sheetShape = RoundedCornerShape(
                            topStartPercent = 8,
                            topEndPercent = 8
                        )
                    ) {
                        Navigator(Screem())
//                DebugView()
                    }
//                    NotificationContainer()
                }
//                Box(
//                    modifier = Modifier
//                        .requiredWidth(1000.dp)
//                        .requiredHeight(1000.dp)
//                ) {
//                    CaseEditor()
////                    DraggableResizableImage(R.drawable.landscape3)
//                }
//                ImageWithCutoutOverlay(ImageState(id = 1, R.drawable.landscape2))

            }

        }
    }
}

data class ImageState(
    val id: Int,
    val image: Int,
    val original: Pair<Int, Int> = Pair(0, 0),
    val offset: Offset = Offset(880f, 850f),
    val width: Dp = 0.dp,
    val height: Dp = 0.dp,
)

@Composable
fun ImageWithCutoutOverlay(it: ImageState) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val originalSize = remember(R.drawable.ic_user) {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_user, options)
        options.outWidth to options.outHeight
    }
    var isVisible by remember { mutableStateOf(true) }


    Box(
        modifier = Modifier
            .requiredWidth(1000.dp)
            .requiredHeight(1000.dp)
    ) {

        val imageRes = remember(it.image) {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeResource(context.resources, it.image, options)
            options.outWidth to options.outHeight
        }
        var offset by remember { mutableStateOf(Offset(880f, 850f)) }
        var width by remember { mutableStateOf(with(density) { imageRes.first.toDp() }) }
        var height by remember { mutableStateOf(with(density) { imageRes.second.toDp() }) }
        Image(
            painter = painterResource(id = R.drawable.landscape3),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .offset {
                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                }
                .size(width, height)
        )
        // Центрированное изображение
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = null,
            )
        }

        // Тёмный фон с "вырезом" в центре
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen // важный момент!
                }
                .drawWithContent {
                    drawContent() // нарисовать фон
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    val imageWidth = originalSize.first
                        .toDp()
                        .toPx()
                    val imageHeight = originalSize.second
                        .toDp()
                        .toPx()

                    val imageLeft = (canvasWidth - imageWidth) / 2f
                    val imageTop = (canvasHeight - imageHeight) / 2f

                    // Нарисовать тёмный прямоугольник
                    drawRect(color = Color.Black)

                    // Вырезать "окно" в центре под изображение
                    val cornerRadiusPx = with(density) { 28.dp.toPx() }
                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = Offset(imageLeft, imageTop),
                        size = Size(imageWidth, imageHeight),
                        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                        blendMode = BlendMode.Clear
                    )
                }
        )
        if (isVisible) {
//            ic.forEach {
//            DraggableResizableImage(it.image, original = imageRes, resultOffset = {
//                offset = it
//            }) { w, h ->
//                width = w
//                height = h
//            }
//            }

        }


    }
}


@Composable
fun CaseEditor(

) {
    Box(
        Modifier.fillMaxSize()
    ) {
//        DraggableResizableImage(R.drawable.landscape3)


    }
}

@Composable
fun MovableResizableImageWithCornerHandles(
    painter: Painter,
    imageWidth: Dp,
    imageHeight: Dp,
    modifier: Modifier = Modifier
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableStateOf(1f) }
    val minScale = 0.05f

    val density = LocalDensity.current
    val baseWidthPx = with(density) { imageWidth.toPx() }
    val baseHeightPx = with(density) { imageHeight.toPx() }

    val scaledWidth = imageWidth * scale
    val scaledHeight = imageHeight * scale

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .size(scaledWidth, scaledHeight)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    }
                }
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            val handleSize = 28.dp

            @Composable
            fun Handle(
                alignment: Alignment,
                onDrag: (Offset) -> Unit
            ) {
                Box(
                    modifier = Modifier
                        .size(handleSize)
                        .align(alignment)
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                onDrag(dragAmount)
                            }
                        }
                        .background(Color.White, shape = CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                )
            }

            // Левый верхний угол
            Handle(Alignment.TopStart) { dragAmount ->
                val delta = (-dragAmount.x - dragAmount.y) / (baseWidthPx + baseHeightPx)
                val newScale = (scale + delta).coerceAtLeast(minScale)
                val dx = baseWidthPx * (scale - newScale)
                val dy = baseHeightPx * (scale - newScale)
                offset += Offset(dx, dy)
                scale = newScale
            }
            // Правый верхний угол
            Handle(Alignment.TopEnd) { dragAmount ->
                val delta = (dragAmount.x - dragAmount.y) / (baseWidthPx + baseHeightPx)
                val newScale = (scale + delta).coerceAtLeast(minScale)
                val dy = baseHeightPx * (scale - newScale)
                offset += Offset(0f, dy)
                scale = newScale
            }
            // Левый нижний угол
            Handle(Alignment.BottomStart) { dragAmount ->
                val delta = (-dragAmount.x + dragAmount.y) / (baseWidthPx + baseHeightPx)
                val newScale = (scale + delta).coerceAtLeast(minScale)
                val dx = baseWidthPx * (scale - newScale)
                offset += Offset(dx, 0f)
                scale = newScale
            }
            // Правый нижний угол
            Handle(Alignment.BottomEnd) { dragAmount ->
                val delta = (dragAmount.x + dragAmount.y) / (baseWidthPx + baseHeightPx)
                val newScale = (scale + delta).coerceAtLeast(minScale)
                scale = newScale
            }
        }
    }
}

//@Composable
//fun ResizableDraggableImage(
//    imageRes: Int,
//    initialSizeDp: Dp = 150.dp,
//    cornerRadius: Dp = 16.dp
//) {
//    val density = LocalDensity.current
//
//    // Работаем в пикселях для плавности
//    var offsetX by remember { mutableStateOf(0f) }
//    var offsetY by remember { mutableStateOf(0f) }
//
//    val minSizePx = with(density) { 100.dp.toPx() }
//    val maxSizePx = with(density) { 400.dp.toPx() }
//    var sizePx by remember { mutableStateOf(with(density) { initialSizeDp.toPx() }) }
//
//    val handleSize = 24.dp
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        Box(
//            modifier = Modifier
//                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//                .size(with(density) { sizePx.toDp() })
//                .clip(RoundedCornerShape(cornerRadius))
//                .pointerInput(Unit) {
//                    detectDragGestures(
//                        onDrag = { change, dragAmount ->
//                            change.consume()
//                            offsetX += dragAmount.x
//                            offsetY += dragAmount.y
//                        }
//                    )
//                }
//        ) {
//            Image(
//                painter = painterResource(id = imageRes),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clip(RoundedCornerShape(cornerRadius))
//            )
//
//            // Ручка в правом нижнем углу для изменения размера
//            Box(
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .size(handleSize)
//                    .background(Color.Gray.copy(alpha = 0.5f), shape = CircleShape)
//                    .pointerInput(Unit) {
//                        detectDragGestures { change, dragAmount ->
//                            change.consume()
//                            sizePx = (sizePx + dragAmount.x.coerceAtLeast(dragAmount.y))
//                                .coerceIn(minSizePx, maxSizePx)
//                        }
//                    }
//            )
//        }
//    }
//}






