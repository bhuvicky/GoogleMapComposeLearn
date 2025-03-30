package com.example.googlemapcomposelearn

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun SelectableButton(
    buttonState: ButtonState,
    onClick: () -> Unit,
    iconId: Int,
    contentDescription: Int,
) {
    SelectableButton(
        buttonState = buttonState,
        onClick = onClick,
    ) { tint ->
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(contentDescription),
            tint = tint
        )
    }
}

@Composable
fun SelectableButton(
    buttonState: ButtonState,
    onClick: () -> Unit,
    icon: @Composable (Color) -> Unit
) {
    IconButton(
        onClick = onClick
    ) {
        val tint = when (buttonState) {
            ButtonState.NORMAL -> MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            ButtonState.SELECTED -> MaterialTheme.colorScheme.primary
        }
        icon(tint)
    }
}

enum class ButtonState {
    NORMAL, SELECTED
}

data class ButtonStates(
    val currentLocation: ButtonState = ButtonState.NORMAL,
    val mockLocation: ButtonState = ButtonState.NORMAL,
    val map: ButtonState = ButtonState.NORMAL
)