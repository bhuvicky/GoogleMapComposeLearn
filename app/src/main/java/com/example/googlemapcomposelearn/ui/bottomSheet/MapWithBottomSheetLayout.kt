package com.example.googlemapcomposelearn.ui.bottomSheet

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.googlemapcomposelearn.ui.MapScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapWithBottomSheetLayout() {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        /*bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
        )*/
    )
    val systemUiController = rememberSystemUiController()
    systemUiController.setNavigationBarColor(Color.Black)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 192.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = { BottomSheetContentLayout() },
        sheetContainerColor = Color.White,
    ) {
        MapScreen()
    }
}