package com.ansar.lvkapp.feature.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ansar.lvkapp.R
import com.ansar.lvkapp.feature.ViewPhotoScreen
import com.ansar.lvkapp.feature.photoEditor.State
import com.ansar.lvkapp.model.Image
import com.ansar.lvkapp.movable.Movable
import com.ansar.lvkapp.uiKit.PageContainer
import com.ansar.lvkapp.uiKit.designe.BackIcon
import com.ansar.lvkapp.uiKit.theme.AppTheme
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditorScreen : Screen {
    @Composable
    override fun Content() {
        PageContainer(content = {
            Row {
                Column(
                    Modifier
                        .background(AppTheme.colors.white)
                        .weight(0.2f)
                        .fillMaxSize()
                ) {
                    BackIcon(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp)
                    )

                }
                Column(
                    modifier = Modifier
                        .weight(0.8f)
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                0f to AppTheme.colors.yellow1,
                                1f to AppTheme.colors.yellow2,
                            )
                        )
                ) {

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 16.dp)
                            .padding(top = 10.dp),
                        shape = RoundedCornerShape(10.dp),

                        ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = "IPhone 14 Pro",
                                style = AppTheme.typography.semiBold.copy(
                                    fontSize = 18.sp,
                                    color = AppTheme.colors.black,
                                    textAlign = TextAlign.Center,
                                )
                            )
                        }

                    }
                    Spacer(Modifier.size(16.dp))
                    Editor()

                }
            }
        })
    }

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
    @Composable
    private fun Editor() {
        val res = LocalContext.current.resources
        var size by remember { mutableStateOf(IntSize.Zero) }
        var max by remember { mutableStateOf(3F) }
        val density = LocalDensity.current
        val captureController = rememberCaptureController()
        val uiScope = rememberCoroutineScope()
        var ticketBitmap: ImageBitmap? by remember { mutableStateOf(null) }
        var containerImage by remember { mutableStateOf(R.drawable.ic_user) }
        var paddingTop by remember { mutableStateOf(0.dp) }
        var paddingStart by remember { mutableStateOf(0.dp) }
        var paddingBottom by remember { mutableStateOf(0.dp) }
        var paddingEnd by remember { mutableStateOf(0.dp) }

        var list by remember {
            mutableStateOf(
                listOf(
                    Image(id = 1, image = R.drawable.landscape1, 0f),
                    Image(id = 2, image = R.drawable.landscape2, 0f),
                    Image(id = 3, image = R.drawable.landscape3, 0f),
                )
            )
        }

        ticketBitmap?.let { bitmap ->

            val navigator = LocalNavigator.currentOrThrow
            navigator.push(ViewPhotoScreen(bitmap))

        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Transparent)
        ) {
            var viewState by remember { mutableStateOf(false) }
//            Button(
//                modifier = Modifier.padding(top = 20.dp),
//                onClick = {
//                    viewState = !viewState
//                    containerImage = R.drawable.ic_backtest
//                    paddingTop = 40.dp
//                    paddingStart = 40.dp
//                    uiScope.launch {
//                        delay(100)
//                        ticketBitmap = captureController.captureAsync().await()
//                    }
//                }
//            ) {
//                androidx.compose.material3.Text("Preview Ticket Image")
//            }


            Box(
                Modifier
                    .capturable(captureController)
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(28.dp))
                    .align(Alignment.Center)
                    .padding(
                        start = paddingStart,
                        top = paddingTop,
                        end = paddingEnd,
                        bottom = paddingBottom
                    )
                    .background(Color.Transparent)

            ) {
                val imageWidthDp = with(density) { size.width.toDp() }
                val imageHeightDp = with(density) { size.height.toDp() }
                if (size.width > 0) {
                    Box(
                        modifier = Modifier
                            .clipToBounds()
                            .align(Alignment.Center)
                            .size(imageWidthDp, imageHeightDp)
                    ) {
                        list.forEach {
                            var zIndex1 by remember(it.zIndex) {
                                mutableStateOf(it.zIndex)
                            }
                            var image by remember {
                                mutableStateOf(
                                    ImageBitmap.imageResource(
                                        res,
                                        it.image
                                    )
                                )
                            }
                            var movableState by remember { mutableStateOf(State.Resize) }

                            Movable(
                                modifier = Modifier
                                    .zIndex(zIndex1),
                                image = image,
                                state = movableState,
                                viewState = viewState,
                                cropOnClick = {
                                    movableState = State.Resize
                                },
                                changeState = {
                                    movableState = State.Resize
                                },
                                content = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_setting_crop),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable {
                                                movableState = State.Crop
                                            },
                                        contentScale = ContentScale.FillBounds
                                    )
                                }
                            ) {
                                list = list.map { list ->
                                    if (it.id == list.id) {
                                        list.copy(zIndex = ++max)
                                    } else list
                                }
                            }


                        }

                    }
                }


                Image(
                    modifier = Modifier
                        .onSizeChanged {
                            println(it)
                            size = it
                        },
                    bitmap = ImageBitmap.imageResource(
                        res,
                        containerImage
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
            }


            Switch(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 30.dp),
                checked = viewState,
                onCheckedChange = {
                    viewState = it
                    if (it) {
                        viewState = !viewState
                        containerImage = R.drawable.ic_back
                        paddingTop = 40.dp
                        paddingStart = 40.dp
                        uiScope.launch {
                            delay(100)
                            ticketBitmap = captureController.captureAsync().await()
                        }
                    }
                }
            )


        }


    }

}