package com.ansar.lvkapp.uiKit.designe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.ansar.lvkapp.R
import com.ansar.lvkapp.base.ext.clickableRound
import com.ansar.lvkapp.uiKit.theme.AppTheme

@Composable
fun BackIcon(modifier: Modifier = Modifier, color: Color? = null, onClick: (() -> Unit)? = null) {
    val navigator = LocalNavigator.currentOrThrow
    Card(
        modifier
            .size(44.dp)
            .clickableRound(10.dp) {
                if (onClick == null) navigator.pop()
                else onClick()
            }, backgroundColor = AppTheme.colors.yellow1
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.ic_backtest),
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.Center),
                colorFilter = if (color == null) null else ColorFilter.tint(color = color)
            )
        }

    }

}