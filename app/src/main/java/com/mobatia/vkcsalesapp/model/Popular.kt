package com.mobatia.vkcsalesapp.model

data class Popular(
    val category: List<Category>,
    val categoryid: String,
    val categoryname: String,
    val new_arrivals: List<Any>,
    val productSapId: String,
    val product_case: List<ProductCase>,
    val product_color: List<ProductColor>,
    val product_image: List<ProductImage>,
    val product_size: List<ProductSize>,
    val product_type: List<ProductType>,
    val productcost: String,
    val productdescription: String,
    val productid: String,
    val productname: String,
    val productoffer: String,
    val productquantity: String,
    val productviews: String,
    val timestamp: String
)