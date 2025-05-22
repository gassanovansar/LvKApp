package com.ansar.lvkapp

import DraggableResizableImage
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class Screem : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val density = LocalDensity.current
        val screenModel = rememberScreenModel { MainActivityScreenModel() }
        val state by screenModel.stateFlow.collectAsState()
        var isVisible by remember { mutableStateOf(false) }
        if (isVisible) {
            ImageWithCutoutOverlay(
                state
            ) { it, z, id ->
                screenModel.changeImages(it, z, id)
            }
        }

        LaunchedEffect(screenModel) {
            launch {
                screenModel.container.sideEffectFlow.collect {
                    state.images.forEachIndexed { i, it ->
                        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                        BitmapFactory.decodeResource(context.resources, it.image, options)
                        val imageRes = options.outWidth to options.outHeight
                        screenModel.changeImages(it.copy(
                            original = imageRes,
                            width = with(density) { imageRes.first.toDp() },
                            height = with(density) { imageRes.second.toDp() }
                        ), (i + 1) == state.images.size)
                    }
                    isVisible = true
                }
            }
        }

    }

    @Composable
    fun ImageWithCutoutOverlay(
        state: MainActivityState,
        onChange: (ImageState, Boolean, Int) -> Unit
    ) {
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
            state.images.forEach {
                var offset by remember(it.offset) { mutableStateOf(it.offset) }
                var width by remember(it.width) { mutableStateOf(it.width) }
                var height by remember(it.height) { mutableStateOf(it.height) }
                Image(
                    painter = painterResource(id = it.image),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .offset {
                            IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                        }
                        .size(width, height)

                )
            }


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


                state.images.sortedBy { it.id }.forEachIndexed { i, it ->
//                    id = it.id,
//                    zIndex = state.zIndex,
                    DraggableResizableImage(
                        original = it.original,
//                        border = i == 0,
                        border = state.zIndex == it.id,
                        resultOffset = { offset, w, h, z ->
                            onChange(
                                it.copy(
                                    offset = offset,
                                    width = w,
                                    height = h,
                                ), z, it.id
                            )
//                    offset = it
                        })
                }

            }


        }
    }
}