package com.example.googlemapcomposelearn.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.googlemapcomposelearn.model.AutoCompletePlace

@Composable
fun PlacesAutocompleteTextField(
    predictions: List<AutoCompletePlace>,
    onQueryChanged: (String) -> Unit,
    searchText: String = "",
    onSelected: (AutoCompletePlace) -> Unit = {},
    selectedPlace: AutoCompletePlace? = null,
    textFieldMaxLines: Int = 1,
    scrollable: Boolean = true,
    placeHolderText: String = "",
    modifier: Modifier = Modifier,
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .then(if (scrollable) Modifier.verticalScroll(scrollState) else Modifier)
    ) {
        AutoCompleteTextField(
            queryText = searchText,
            onQueryChanged = { onQueryChanged(it) },
            maxLines = textFieldMaxLines,
            placeHolderText = placeHolderText,
        )

        for (prediction in predictions) {
            AutocompletePlaceRow(
                autocompletePlace = prediction,
                onPlaceSelected = {
                    keyboardController?.hide()
                    onSelected(it)
                },
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    queryText: String = "",
    onQueryChanged: (String) -> Unit,
    maxLines: Int,
    placeHolderText: String = "",
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 20.dp),
        shape = RoundedCornerShape(25.dp),
        value = queryText,
        onValueChange = { onQueryChanged(it) },
        trailingIcon = {
            if (queryText.isNotBlank())
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "clear")
                }
        },
        singleLine = maxLines == 1,
        placeholder = {
            Text(
                text = placeHolderText,
            )
        }
    )
}

@Composable
private fun AutocompletePlaceRow(
    autocompletePlace: AutoCompletePlace,
    onPlaceSelected: (AutoCompletePlace) -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize()
            .padding(16.dp)
            .clickable { onPlaceSelected(autocompletePlace) }

    ) {
        Text(
            text = autocompletePlace.primaryText.toString(),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}