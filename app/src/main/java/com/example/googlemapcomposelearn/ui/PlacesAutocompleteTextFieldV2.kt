package com.example.googlemapcomposelearn.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.googlemapcomposelearn.R
import com.example.googlemapcomposelearn.model.AutoCompletePlace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesAutocompleteTextFieldV2(
    predictions: List<AutoCompletePlace>,
    onQueryChanged: (String) -> Unit,
    searchText: String = "",
    onPlaceSelected: (AutoCompletePlace) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var padding by remember { mutableStateOf(16.dp) }

    SearchBar(
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = padding, vertical = 0.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = searchText,
                onQueryChange = { onQueryChanged(it) },
                onSearch = { expanded = false },
                expanded = expanded,
                onExpandedChange = {
                    expanded = it
                    println("xxxx log expanded inputfield = $expanded")
                    padding = if (expanded) 0.dp else 16.dp
                },
                placeholder = { Text("Search here") },
                leadingIcon = { Icon(painterResource(R.drawable.icons8_google_maps_48), contentDescription = null) },
                trailingIcon = {
                    if (searchText.isNotBlank())
                        IconButton(onClick = { onQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "clear")
                        }
                },
            )
        },
        expanded = expanded,
        onExpandedChange = {
            expanded = it
            println("xxxx log expanded outside = $expanded")

        },
        colors = SearchBarColors(containerColor = Color.White, dividerColor = Color.Black),
        shadowElevation = 4.dp,
    ) {
        for (prediction in predictions) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        expanded = false
                        onPlaceSelected(prediction)
                    },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_location_pin_24),
                    tint = Color.Gray,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = prediction.primaryText.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 56.dp, end = 16.dp)
            )
        }
    }
}
