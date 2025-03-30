package com.example.googlemapcomposelearn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.googlemapcomposelearn.ui.MapScreen
import com.example.googlemapcomposelearn.ui.bottomSheet.MapWithBottomSheetLayout
import com.example.googlemapcomposelearn.ui.theme.GoogleMapComposeLearnTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoogleMapComposeLearnTheme {
//                MapScreen()
                MapWithBottomSheetLayout()
            }
        }
    }
}