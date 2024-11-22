package com.example.antkysport.Model

data class Product (
    var title: String,            // Tên sản phẩm
    var code: String,       // Mã sản phẩm của sản phẩm
    var quantity: Number,          // số lượng của sản phẩm
    var price: Double,             //Giá của sản phẩm
    var size: Array<String>,     // Kích thước sản phẩm
    var color: Array<String>,   //màu của sản phẩm
    var image: String,          // Ảnh của sản phẩm
    var category: String,        // Danh mục của sản phẩm
    var description: String,    // Mô tả sản phẩm
    val createdAt: String,       // Thời gian sản phẩm được tạo
    val updatedAt: String        // Thời gian sản phẩm được cập nhật        // Thời gian sản phẩm được cập nhật
)