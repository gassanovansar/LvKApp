package com.ansar.lvkapp.feature.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ansar.lvkapp.R
import com.ansar.lvkapp.feature.category.CategoryScreen
import com.ansar.lvkapp.uiKit.PageContainer
import com.ansar.lvkapp.uiKit.theme.AppTheme

class MainScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        PageContainer(
            content = {
                Box(Modifier.fillMaxSize()) {
                    Column {
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        0f to AppTheme.colors.yellow1,
                                        1f to AppTheme.colors.yellow2,
                                    )
                                )
                        )
                        Box(
                            Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .background(AppTheme.colors.white)
                        )
                    }
                    Button(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = { navigator.push(CategoryScreen()) },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppTheme.colors.white
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                            Image(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                painter = painterResource(R.drawable.ic_device),
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = ("выбрать модель телефона").uppercase(),
                                style = AppTheme.typography.bold.copy(
                                    fontSize = 16.sp,
                                    color = AppTheme.colors.black,
                                )
                            )
                        }
                    }
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 16.dp, end = 16.dp)
                    ) {
                        Language(Modifier.padding(start = 40.dp))
                        Language(Modifier.padding(start = 25.dp))
                        Language(Modifier.padding(start = 10.dp))

                    }

                    Column(
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "Следите за нами",
                            style = AppTheme.typography.bold.copy(
                                fontSize = 16.sp,
                                color = AppTheme.colors.black,
                            )
                        )
                        Row(Modifier.padding(top = 16.dp)) {
                            SocialNetwork(Modifier.weight(1f), text = "instragram")
                            SocialNetwork(Modifier.weight(1f), text = "tiktok")
                            SocialNetwork(Modifier.weight(1f), text = "whatsapp")
                        }
                    }
                }
            }
        )
    }

    @Composable
    private fun SocialNetwork(modifier: Modifier = Modifier, text: String) {
        Column(modifier) {
            Box(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(AppTheme.colors.gray)
                    .size(75.dp)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp),
                text = text.uppercase(),
                style = AppTheme.typography.bold.copy(
                    fontSize = 12.sp,
                    color = AppTheme.colors.black,
                    textAlign = TextAlign.Center
                )
            )
        }
    }

    @Composable
    private fun Language(modifier: Modifier) {

//        Box(modifier = modifier) {
        Card(
            shape = CircleShape,
            modifier = modifier,
            backgroundColor = Color.Transparent,
            border = BorderStroke(width = 2.dp, color = AppTheme.colors.blackAlpha)
        ) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(R.drawable.ic_ru),
                contentDescription = null
            )
//            }
        }


    }
}