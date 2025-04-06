package com.ansar.lvkapp.feature.photoEditor

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ansar.lvkapp.MainScreen
import com.ansar.lvkapp.MainScreen.State
import com.ansar.lvkapp.R
import com.ansar.lvkapp.model.Image
import com.ansar.lvkapp.movable.Movable
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

class PhotoEditorScreen : Screen {
    @OptIn(ExperimentalComposeApi::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val res = LocalContext.current.resources
        var size by remember { mutableStateOf(IntSize.Zero) }
        var max by remember { mutableStateOf(3F) }
        val density = LocalDensity.current
        val captureController = rememberCaptureController()
        val uiScope = rememberCoroutineScope()
        var ticketBitmap: ImageBitmap? by remember { mutableStateOf(null) }

//        LaunchedEffect(Unit) {
//            ticketBitmap = captureController.captureAsync().await()
//        }

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
            navigator.push(MainScreen())
//                Dialog(onDismissRequest = { }) {
//                    Column(
//                        modifier = Modifier
//                            .background(LightGray)
//                            .padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text("Preview of Ticket image \uD83D\uDC47")
//                        Spacer(Modifier.size(16.dp))
//                        Image(
//                            bitmap = bitmap,
//                            contentDescription = "Preview of ticket"
//                        )
//                        Spacer(Modifier.size(4.dp))
//                        Button(onClick = { ticketBitmap = null }) {
//                            Text("Close Preview")
//                        }
//                    }
//                }
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            var viewState by remember { mutableStateOf(false) }
            Button(
                modifier = Modifier.padding(top = 20.dp),
                onClick = {
                    viewState = !true
                    uiScope.launch {
                        ticketBitmap = captureController.captureAsync().await()
                    }
                }
            ) {
                Text("Preview Ticket Image")
            }
            Box(
                Modifier
                    .align(Alignment.Center)
                    .capturable(captureController)
                    .clip(RoundedCornerShape(28.dp))
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
//                                    Switch(
//                                        modifier = Modifier.align(Alignment.Center),
//                                        checked = movableState == State.Resize,
//                                        onCheckedChange = {
//                                            movableState = when (movableState) {
//                                                State.Resize -> State.Crop
//                                                State.Crop -> State.Resize
//                                            }
//                                        }
//                                    )
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
                        R.drawable.ic_user
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
                }
            )


        }


    }
}