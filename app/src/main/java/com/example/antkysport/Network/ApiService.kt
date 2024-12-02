package com.example.antkysport.Network

import com.example.antkysport.Model.Cart
import com.example.antkysport.Model.CartRespone
import com.example.antkysport.Model.OrderRequest
import com.example.antkysport.Model.OrderResponse
import com.example.antkysport.Model.OrdersResponse
import com.example.antkysport.Model.PriceDeserializer
import com.example.antkysport.Model.Product
import com.example.antkysport.Model.UserResponse
import com.example.antkysport.Model.idRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class RegisterRequest(
    val name: String,
    val username:String,
    val email: String,
    val password:String
)
data class LoginRequest(
    val email: String,
    val password: String
)
data class AuthResponse(
    val token:String
)
interface ApiService{
    //Api đăng ký
    @POST("/users/net")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>
    //Api đăng nhập
    @POST("/users/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @GET("/users/get-user")
    //API lấy đối tượng ra từ DB
    suspend fun getUserById(@Header("Authorization") token: String): Response<UserResponse>// token sẽ được lấy từ `sharedPreferences`
    // API lấy danh sách sản phẩm
    @GET("/products")
    suspend fun getProducts(): Response<List<Product>> // Trả về danh sách sản phẩm
    @GET("/products/getProductByCode/{code}")
    suspend fun getProductByCode(@Path("code") code: String): Product
    // Thêm hàm tìm kiếm sản phẩm vào interface ApiService
    @GET("/products/search")
    suspend fun searchProduct(@Query("q") query: String): Response<List<Product>>
    @POST("/carts/add")
    suspend fun addToCart(@Header("Authorization") token: String, @Body items: Cart): Response<Unit> // Unit nếu API không trả về dữ liệu
    @GET("/carts/get-cart")
    suspend fun getCart(@Header("Authorization") token: String): Response<CartRespone>
    @POST("/carts/del-cart")
    suspend fun delCart(@Header("Authorization") token: String, @Body _id: idRequest): Response<Unit>
    @POST("/carts/checkout")
    suspend fun checkout(@Header("Authorization") token: String,@Body orderRequest: OrderRequest): Response<Unit>
    @GET("/carts/getOrderUser")
    suspend fun getOrders(@Header("Authorization") token: String): Response<OrdersResponse>
}

//Đối tượng Retrofit để tạo các Api service
object RetrofitInstance {
    private val gson: Gson = GsonBuilder() // Tạo Gson từ GsonBuilder
        .registerTypeAdapter(Double::class.java, PriceDeserializer()) // Đăng ký Custom Deserializer
        .create() // Gọi create() để hoàn chỉnh đối tượng Gson

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.100:3000/") // Địa chỉ IP của bạn
            .addConverterFactory(GsonConverterFactory.create(gson)) // Truyền Gson đã hoàn chỉnh
            .build()
            .create(ApiService::class.java)
    }
}