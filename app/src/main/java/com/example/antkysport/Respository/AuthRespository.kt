package com.example.antkysport.Respository


import android.content.Context
import android.util.Log
import com.example.antkysport.Model.Cart
import com.example.antkysport.Model.CartItem
import com.example.antkysport.Model.CartRespone
import com.example.antkysport.Model.OrderItem
import com.example.antkysport.Model.OrderRequest
import com.example.antkysport.Model.OrderResponse
import com.example.antkysport.Model.OrdersResponse
import com.example.antkysport.Model.ShippingAddress
import com.example.antkysport.Model.UserResponse
import com.example.antkysport.Model.idRequest
import com.example.antkysport.Network.AuthResponse
import com.example.antkysport.Network.LoginRequest
import com.example.antkysport.Network.RegisterRequest
import com.example.antkysport.Network.RetrofitInstance
import retrofit2.Response

class AuthRespository(private val context: Context) {
    private val api = RetrofitInstance.api

    //Đăng ký người dùng
    suspend fun registerUser(name:String, username:String, email: String, password:String) : Response<AuthResponse>{
        val request = RegisterRequest(name, username, email,password)
        return api.registerUser(request)
    }
    //Đăng nhập người dùng
    suspend fun loginUser(email: String,password: String):Response<AuthResponse>{
        val request = LoginRequest(email,password)
        return api.loginUser(request)
    }
    suspend fun getUser(): Response<UserResponse>{
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("userToken",null)
        if(token.isNullOrEmpty()){
            Log.d("zzz", "token_AuthRespository không tồn tại")
        }
        return api.getUserById("Bearer $token")
    }
    //Them vao gio hang
    suspend fun addCart(code: String,size_item:String?,color:String?,quantity_cart: Int): Response<Unit> {
        // Lấy token từ SharedPreferences
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("userToken", null)

        if (token.isNullOrEmpty()) {
            throw Exception("Token không tồn tại. Người dùng chưa đăng nhập.")
        }

        // Tạo danh sách `CartItem`
        val cartItems = listOf(
            CartItem(
                code = code,
                size_item = size_item,
                color = color,
                quantity_cart = quantity_cart
            )
        )
        // Tạo đối tượng `Cart`
        val cartRequest = Cart(items = cartItems)

        // Gửi request tới API
        return api.addToCart("Bearer $token", cartRequest)
    }
    //lấy giỏ hàng
    suspend fun getCart(): Response<CartRespone> {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("userToken", null)

        if (token.isNullOrEmpty()) {
            throw Exception("Token không tồn tại. Người dùng chưa đăng nhập.")
        }

        val response = api.getCart("Bearer $token")
        return response
    }
    //xóa giỏ hàng
    suspend fun delToCart(_id: idRequest): Response<Unit> {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("userToken", null)

        if (token.isNullOrEmpty()) {
            throw Exception("Token không tồn tại. Người dùng chưa đăng nhập.")
        }
        return api.delCart("Bearer $token",_id)
    }
    //đặt hàng
    suspend fun checkout(
        items: List<OrderItem>,       // Danh sách sản phẩm trong đơn hàng
        address: String,              // Địa chỉ giao hàng
        phoneNumber: String,          // Số điện thoại
        totalAmount: Double           // Tổng tiền của đơn hàng
    ): Response<Unit> {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("userToken", null)

        if (token.isNullOrEmpty()) {
            throw Exception("Token không tồn tại. Người dùng chưa đăng nhập.")
        }

        // Tạo đối tượng OrderRequest với toàn bộ danh sách sản phẩm
        val orderRequest = OrderRequest(
            shippingAddress = ShippingAddress(
                address = address,
                phoneNumber = phoneNumber
            ),
            totalAmount = totalAmount,
            items = items // Gửi toàn bộ danh sách sản phẩm
        )
        Log.d("CheckoutPayload", "Payload: $orderRequest")

        return api.checkout("Bearer $token", orderRequest)
    }

    suspend fun getOrders(): Response<OrdersResponse> {
        // Lấy token từ SharedPreferences
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("userToken", null)
            ?: throw Exception("Token không tồn tại. Người dùng chưa đăng nhập.")

        // Gửi yêu cầu API với token Authorization
        return try {
            // Thực hiện yêu cầu getOrders() với token
            val response = api.getOrders("Bearer $token")

            if (response.isSuccessful) {
                // Trả về response nếu thành công
                response
            } else {
                // Xử lý nếu phản hồi không thành công
                throw Exception("Lỗi khi lấy dữ liệu: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            // Xử lý các lỗi ngoại lệ (ví dụ: lỗi kết nối mạng)
            throw Exception("Lỗi kết nối: ${e.message}")
        }
    }
}

