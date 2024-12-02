package com.example.antkysport.Model

data class CartItem(
    val code: String,        // Mã sản phẩm
    val size_item: String?,      // Kích cỡ sản phẩm
    val color: String?,          // Màu sắc sản phẩm
    val quantity_cart: Int,      // Số lượng trong giỏ hàng
)
data class Cart(
    val items: List<CartItem> = emptyList()  // Danh sách sản phẩm trong giỏ hàng
)
data class CartItemRespone(
    val code: String,        // Mã sản phẩm
    val size_item: String?,      // Kích cỡ sản phẩm
    val color: String?,          // Màu sắc sản phẩm
    val quantity_cart: Int,      // Số lượng trong giỏ hàng
    val _id:String
)
data class CartRespone(
    val items: List<CartItemRespone> = emptyList()  // Danh sách sản phẩm trong giỏ hàng
)
data class idRequest(val _id: String)

