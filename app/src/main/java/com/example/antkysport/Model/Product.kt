package com.example.antkysport.Model

data class Product(
    val _id: String,              // ID của sản phẩm
    val title: String,           // Tên sản phẩm
    val code: String,            // Mã sản phẩm
    val quantity: Int,           // Số lượng sản phẩm
    val price: Double,           // Giá của sản phẩm
    val size: List<String>,      // Kích thước sản phẩm
    val color: List<String>,     // Màu sắc của sản phẩm
    val image: String,           // URL hình ảnh sản phẩm
    val description: String,     // Mô tả sản phẩm
    val createdAt: String,       // Thời gian tạo
    val updatedAt: String        // Thời gian cập nhật
)