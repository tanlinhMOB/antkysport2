package com.example.antkysport.Model

data class OrderRequest(
    val shippingAddress: ShippingAddress, // Địa chỉ giao hàng
    val totalAmount: Double,              // Tổng số tiền của đơn hàng
    val items: List<OrderItem>            // Danh sách sản phẩm trong đơn hàng
)

data class ShippingAddress(
    val address: String,        // Địa chỉ chi tiết
    val phoneNumber: String     // Số điện thoại liên hệ
)

data class OrderItem(
    val code: String,
    val size: String,       // Đảm bảo sử dụng đúng tên trường: `size`
    val color: String?,
    val quantity: Int       // Đảm bảo sử dụng đúng tên trường: `quantity`
)
data class OrdersResponse(
    val orders: List<OrderResponse> // Danh sách đơn hàng
)
data class OrderResponse(
    val orderId: String,
    val totalAmount: Double,
    val createdAt: String,
    val status: String,
    val items: List<Item>
)
data class Item(
    val code: String,
    val quantity: Int
)


