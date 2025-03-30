package com.example.googlemapcomposelearn.ui.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp

@Composable
fun BottomSheetContentLayout() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        /*bottomBar = {
            Spacer(
                modifier = Modifier
                    .background(Color.Black)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            )
        }*/
    ) { paddingValues ->
        // when sheet content added inside scaffold, it swipe up to top of screen.
        BottomSheetContent()
    }
}

@Composable
fun BottomSheetContent() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(16.dp)
        .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Available"
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Free"
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "DC Fast"
        )
    }
}
