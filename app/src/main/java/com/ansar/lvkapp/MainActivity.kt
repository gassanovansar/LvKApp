package com.ansar.lvkapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import com.ansar.lvkapp.feature.category.CategoryScreen
import com.ansar.lvkapp.feature.editor.EditorScreen
import com.ansar.lvkapp.feature.main.MainScreen
import com.ansar.lvkapp.uiKit.theme.AppTheme

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
                        Navigator(MainScreen())
//                DebugView()
                    }
//                    NotificationContainer()
                }

            }
        }
    }
}
