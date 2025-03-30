package com.example.googlemapcomposelearn.nearbysearch

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.googlemapcomposelearn.R

data class PlaceCategoryChipModel(
    val placeCategory: PlaceCategory,
    @DrawableRes val categoryIcon: Int? = null,
    val isSelected: Boolean = false,
)


enum class PlaceCategory(val categoryName: String, @StringRes val categoryStringRes: Int) {
    RESTAURANT("restaurant", R.string.place_category_restaurant),
    GAS_STATION("gas_station", R.string.place_category_gas_station),
    SHOPPING_MALL("shopping_mall", R.string.place_category_shopping_mall)
}