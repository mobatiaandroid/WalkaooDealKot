package com.mobatia.vkcsalesapp.model

data class LoginResponse(
    val address: String,
    val city_name: String,
    val credit_view: String,
    val cust_id: String,
    val customer_name: String,
    val dealer_count: Int,
    val dist_name: String,
    val login: String,
    val role_id: String,
    val role_name: String,
    val shopName: String,
    val state_code: String,
    val state_name: String,
    val user_email: String,
    val user_id: String,
    val user_name: String,
    val user_phone: String
)