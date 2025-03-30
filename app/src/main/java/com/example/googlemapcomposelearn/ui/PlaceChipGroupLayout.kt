package com.example.googlemapcomposelearn.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.googlemapcomposelearn.model.MapUiEvent
import com.example.googlemapcomposelearn.nearbysearch.PlaceCategoryChipModel

// TODO: Selection state tightly coupled with the UI. This should be decoupled.
@Composable
fun PlaceChipGroupLayout(
    placeCategoryList: List<PlaceCategoryChipModel>,
    onPlaceCategorySelected: (MapUiEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val selectedFilters = remember {
        mutableStateListOf<PlaceCategoryChipModel>()
    }
    LazyRow(
        modifier = modifier.fillMaxWidth().wrapContentHeight()
            .padding(horizontal = 16.dp),
    ) {
        items(placeCategoryList.size) { index ->
            val item = placeCategoryList[index]
            FilterChip(
                modifier = modifier.wrapContentWidth().wrapContentHeight().padding(4.dp),
                onClick = {
                    if (selectedFilters.contains(item)) {
                        selectedFilters.remove(item)
                    } else {
                        selectedFilters.clear()
                        selectedFilters.add(item)
                    }
                    onPlaceCategorySelected(MapUiEvent.OnPlaceCategorySelected(item.placeCategory))
                },
                label = {
                    Text(text = stringResource(item.placeCategory.categoryStringRes))
                },
                colors = getSelectedChipColors(),
                selected = selectedFilters.contains(item),
                leadingIcon = item.categoryIcon?.let {
                    {
                        Icon(
                            painter = painterResource(id = item.categoryIcon),
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }
}

fun getSelectedChipColors() = SelectableChipColors(
    containerColor = Color.White,
    labelColor = Color.Black,
    leadingIconColor = Color.Black,
    trailingIconColor = Color.Black,

    disabledContainerColor = Color.White,
    selectedContainerColor = Color.Black,
    disabledLabelColor = Color.Black,
    selectedLabelColor = Color.White,
    disabledLeadingIconColor = Color.Black,
    selectedLeadingIconColor = Color.White,

    disabledTrailingIconColor = Color.Black,
    selectedTrailingIconColor = Color.White,
    disabledSelectedContainerColor = Color.White
)