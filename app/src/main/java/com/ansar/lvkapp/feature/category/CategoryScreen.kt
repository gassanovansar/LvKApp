package com.ansar.lvkapp.feature.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ansar.lvkapp.R
import com.ansar.lvkapp.base.ext.clickableRound
import com.ansar.lvkapp.feature.editor.EditorScreen
import com.ansar.lvkapp.uiKit.PageContainer
import com.ansar.lvkapp.uiKit.designe.AppTextField
import com.ansar.lvkapp.uiKit.designe.BackIcon
import com.ansar.lvkapp.uiKit.theme.AppTheme
import dev.shreyaspatil.capturable.capturable

class CategoryScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
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
                    Card(
                        Modifier
                            .padding(top = 32.dp)
                            .size(54.dp)
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(R.drawable.ic_apple),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.Center),
                            )
                        }

                    }
                    Card(
                        Modifier
                            .padding(top = 16.dp)
                            .size(54.dp)
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(R.drawable.ic_apple),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.Center),
                            )
                        }

                    }
                    Card(
                        Modifier
                            .padding(top = 16.dp)
                            .size(54.dp)
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(R.drawable.ic_apple),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.Center),
                            )
                        }

                    }
                    Card(
                        Modifier
                            .padding(top = 16.dp)
                            .size(54.dp)
                            .align(Alignment.CenterHorizontally)

                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(R.drawable.ic_apple),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.Center),
                            )
                        }

                    }

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

                    AppTextField(
                        value = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 10.dp),
                        hint = "Поиск модели",
                        left = {
                            Image(
                                painter = painterResource(R.drawable.ic_search),
                                contentDescription = null,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    ) {

                    }
                    Spacer(Modifier.size(16.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(12) {
                            PhoneItem(Modifier.clickableRound(10.dp) {
                                navigator.push(EditorScreen())
                            })
                        }
                    }


                }
            }
        })
    }

    @Composable
    private fun PhoneItem(modifier: Modifier) {
        Card(
            modifier,
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp)

        ) {
            Column() {
                Box(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 12.dp)
                        .padding(top = 8.dp)
                        .height(70.dp)
                        .background(AppTheme.colors.gray)
                        .fillMaxWidth()

                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "IPhone 14",
                    style = AppTheme.typography.semiBold.copy(
                        fontSize = 12.sp,
                        color = AppTheme.colors.black,
                    )
                )
            }
        }
    }
}