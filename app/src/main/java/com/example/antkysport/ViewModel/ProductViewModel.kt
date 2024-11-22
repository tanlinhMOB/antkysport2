package com.example.antkysport.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antkysport.Model.Product
import com.example.antkysport.Network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductViewModel : ViewModel() {
    var productList by mutableStateOf<List<Product>>(emptyList()) // Danh sách sản phẩm
    var searchResults by mutableStateOf<List<Product>>(emptyList()) // Danh sách kết quả tìm kiếm
    var isSearching by mutableStateOf(false) // Trạng thái tìm kiếm
    init {
        fetchProducts()
    }



    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getProducts()
                if (response.isSuccessful) {
                    val products = response.body()
                    if (products != null) {
                        productList = products
                        Log.d("ProductViewModel", "Products loaded: $products")
                    } else {
                        Log.e("ProductViewModel", "No products found")
                    }
                } else {
                    Log.e("ProductViewModel", "API Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: HttpException) {
                Log.e("ProductViewModel", "HttpException: ${e.message}")
                e.printStackTrace()
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Exception: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun searchProduct(query: String) {
        viewModelScope.launch {
            isSearching = true // Đặt trạng thái đang tìm kiếm
            try {
                val response = RetrofitInstance.api.searchProduct(query) // Gọi API tìm kiếm
                if (response.isSuccessful) {
                    val products = response.body()
                    searchResults = products ?: emptyList() // Cập nhật kết quả tìm kiếm
                    Log.d("ProductViewModel", "Search results: ${searchResults.joinToString { it.title }}")
                } else {
                    Log.e("ProductViewModel", "API Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Search error: ${e.message}")
            } finally {
                isSearching = false // Kết thúc trạng thái tìm kiếm
            }
        }
    }

    // Hàm để hủy tìm kiếm và trả về danh sách sản phẩm đầy đủ
    fun cancelSearch() {
        searchResults = emptyList() // Xóa kết quả tìm kiếm
        isSearching = false // Đặt trạng thái không tìm kiếm
    }

    fun addProduct(
        title: String,
        code: String,
        quantity: Number,
        price: Double,
        size: Array<String>,
        color: Array<String>,
        image: String,
        category: String,
        description: String,
        createdAt: String,
        updatedAt: String,
        onResult: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.addProduct(
                    Product(
                        title = title,
                        code = code,
                        quantity = quantity,
                        price = price,
                        size = size,
                        color = color,
                        image = image,
                        category = category,
                        description = description,
                        createdAt = "2024-10-16", // Giá trị của ngày tạo
                        updatedAt = "2024-10-16"
                    )
                )
                if (response.isSuccessful) {
                    Log.d("ProductViewModel", "Sản phẩm đã được thêm thành công")
                    onResult() // Gọi lại hàm để quay lại màn hình trước
                } else {
                    Log.e("ProductViewModel", "Lỗi khi thêm sản phẩm: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Lỗi khi thêm sản phẩm: ${e.message}")
            }
        }
    }


}