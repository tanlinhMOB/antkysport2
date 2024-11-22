package com.example.antkysport.Network

import com.example.antkysport.Model.Product
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    // API lấy danh sách sản phẩm
    @GET("/products")
    suspend fun getProducts(): Response<List<Product>> // Trả về danh sách sản phẩm

    // Thêm hàm tìm kiếm sản phẩm vào interface ApiService
    @GET("/products/search")
    suspend fun searchProduct(@Query("q") query: String): Response<List<Product>>

    @POST("/products/add")
    suspend fun addProduct(@Body product: Product): Response<Unit>
}

//Đối tượng Retrofit để tạo các Api service
object RetrofitInstance{
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.24.57.154:3000/") //Địa chỉ IP your device
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}