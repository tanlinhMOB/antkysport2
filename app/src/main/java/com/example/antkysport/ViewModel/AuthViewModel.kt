package com.example.antkysport.ViewModel

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antkysport.Model.CartItem
import com.example.antkysport.Model.CartItemRespone
import com.example.antkysport.Model.OrderItem
import com.example.antkysport.Model.OrderResponse
import com.example.antkysport.Model.OrdersResponse
import com.example.antkysport.Model.UserResponse
import com.example.antkysport.Model.idRequest
import com.example.antkysport.Network.AuthResponse
import com.example.antkysport.Respository.AuthRespository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(context: Context): ViewModel() {
    private val respository = AuthRespository(context)

    private val _cartItems = MutableStateFlow<List<CartItemRespone>>(emptyList()) // StateFlow thay vì LiveData
    val cartItems: StateFlow<List<CartItemRespone>> = _cartItems.asStateFlow()                 // Expose ra ngoài

    // StateFlow để quản lý trạng thái đơn hàng
    private val _ordersResponse = MutableStateFlow<OrdersResponse?>(null)
    val ordersResponse: StateFlow<OrdersResponse?> get() = _ordersResponse

    var authToken : String? = null
    var errorsMessage: String? = null
    var userName: String = "Loading..."
    var userEmail: String = "Loading..."

    fun registerUser(name: String,username:String,email: String,password:String, onSuccess:()->Unit,onError: (String)->Unit,context: Context){
        viewModelScope.launch {
            val respone = respository.registerUser(name,username,email,password)
            handleResponse(respone,onSuccess,onError,context)
        }
    }

    fun loginUser(email: String,password: String,onSuccess: () -> Unit,onError: (String) -> Unit,context: Context){
        viewModelScope.launch {
            val response = respository.loginUser(email,password)
            handleResponse(response,onSuccess,onError,context)
        }
    }

    private fun handleResponse(response: Response<AuthResponse>, onSuccess: () -> Unit, onError: (String) -> Unit, context: Context){
        if(response.isSuccessful){
            authToken = response.body()?.token
            if(authToken != null){
                val sharedPreferences = context.getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                val expirationTime = System.currentTimeMillis() + 360000 // 5 phuts
                with(sharedPreferences.edit()){
                    putString("userToken",authToken)
                    putLong("tokenExpiration",expirationTime)
                    apply() // hoặc commit() để lưu ngay lập tucs
                }

            }
            Log.d("check", "handleResponse: $authToken")
            onSuccess()
        }else{
            errorsMessage = "Lỗi: ${response.message()}"
            onError(errorsMessage?: "Unknown Error")
        }
    }
    fun fetchUser(onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response: Response<UserResponse> = respository.getUser()
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    if (userResponse != null) {
                        val user = userResponse.user
                        userName = user.name
                        userEmail = user.email
                    } else {
                        onError("No user data available")
                    }
                } else {
                    onError("API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown Error")
            }
        }
    }

    fun addToCart(
        code: String,
        size_item: String?,
        color:String?,
        quantity_cart: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = respository.addCart(code,size_item,color ,quantity_cart)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Lỗi API: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Lỗi không xác định")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("zzz", "ViewModel đã bị hủy")
    }

    fun fetchCart() {
        viewModelScope.launch {
            try {
                val response = respository.getCart()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _cartItems.value = it.items
                    } ?: run {
                        _cartItems.value = emptyList() // Nếu API trả về `null`, thiết lập danh sách rỗng
                    }
                } else {
                    Log.e("Cart", "API Error: ${response.message()}")
                    _cartItems.value = emptyList() // Nếu lỗi API, thiết lập danh sách rỗng
                }
            } catch (e: Exception) {
                Log.e("Cart", "Error: ${e.message}")
                _cartItems.value = emptyList() // Xử lý lỗi ngoại lệ
            }
        }
    }
    fun delCartByIdItem(_id: idRequest,onSuccess: () -> Unit,
                        onError: (String) -> Unit){
        viewModelScope.launch{
            try {
                val response = respository.delToCart(_id)
                if(response.isSuccessful){
                    onSuccess()// nếu thiếu cặp ngoặc tròn sẽ ko callback
                }else if(_id==null){
                    onError("_id của item null.")
                }else{
                    Log.d("delCart", "Lỗi API: ${response.code()} - ${response.message()}")
                }
            }catch (e: Exception){
                onError(e.message ?: "Lỗi không xác định")
            }
        }
    }

    //đặt hàng
    fun checkout(
        items: List<OrderItem>,           // Danh sách sản phẩm
        address: String,                  // Địa chỉ giao hàng
        phoneNumber: String,              // Số điện thoại
        totalAmount: Double,              // Tổng tiền đã tính từ client
        onSuccess: () -> Unit,            // Callback khi thành công
        onError: (String) -> Unit         // Callback khi lỗi
    ) {
        viewModelScope.launch {
            try {
                // Gửi danh sách toàn bộ sản phẩm từ giỏ hàng
                val response = respository.checkout(
                    items = items,
                    address = address,
                    phoneNumber = phoneNumber,
                    totalAmount = totalAmount
                )
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Lỗi API: ${response.code()} - ${response.message()}")

                }
            } catch (e: Exception) {
                onError(e.message ?: "Lỗi không xác định")
            }
        }
    }

    // StateFlow để lưu trữ dữ liệu đơn hàng và trạng thái
    // StateFlow cho danh sách đơn hàng

    // Hàm để gọi API lấy đơn hàng
    fun fetchOrders() {
        viewModelScope.launch {
            try {
                // Gọi phương thức getOrders() trong repository
                val response: Response<OrdersResponse> = respository.getOrders()

                // Kiểm tra nếu yêu cầu thành công
                if (response.isSuccessful) {
                    // Cập nhật StateFlow với kết quả thành công
                    _ordersResponse.value = response.body()
                } else {
                    // Không cần StateFlow cho lỗi nếu không muốn sử dụng nó
                    _ordersResponse.value = null
                }
            } catch (e: Exception) {
                // Xử lý các ngoại lệ (lỗi kết nối, lỗi khác)
                _ordersResponse.value = null
            }
        }
    }
}




