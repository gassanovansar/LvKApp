package com.ansar.lvkapp

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.ansar.lvkapp.movable.Movable
import com.image.cropview.CropType
import com.image.cropview.EdgeType
import com.image.cropview.ImageCrop
import kotlin.math.roundToInt

class MainScreen : Screen {


    @Composable
    override fun Content() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeContent)
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


//            Movable(image = image1, state)
//            Movable(image = image2, state)
//            Movable(image = image3, state)
        }
    }


//+++TODO MOVE - двигать
//++TODO MOVE - двигать
//+++TODO ZOOM - по краям - оцентровать
//TODO CROP - обрезка


    enum class State {
        Resize,
        Crop,
    }


}

