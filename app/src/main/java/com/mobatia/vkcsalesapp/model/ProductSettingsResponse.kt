package com.mobatia.vkcsalesapp.model

data class ProductSettingsResponse(
    val brand_banner: ArrayList<BrandBanner>,
    val category: ArrayList<Category>,
    val color: ArrayList<Color>,
    val newarrivals: ArrayList<Newarrival>,
    val offer: ArrayList<Offer>,
    val popular: ArrayList<Popular>,
    val price_range: ArrayList<PriceRange>,
    val size: ArrayList<Size>,
    val type: ArrayList<Type>
)