package com.example.antkysport.ViewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antkysport.Model.Cart
import com.example.antkysport.Model.Product
import com.example.antkysport.Network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ProductViewModel : ViewModel() {
    var productList by mutableStateOf<List<Product>>(emptyList()) // Danh sách sản phẩm
    var searchResults by mutableStateOf<List<Product>>(emptyList()) // Danh sách kết quả tìm kiếm
    var isSearching by mutableStateOf(false) // Trạng thái tìm kiếm
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product
    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getProducts()
                if (response.isSuccessful) {
                    val products = response.body()

                    if (!products.isNullOrEmpty()) {
                        productList = products
                        Log.d("ProductViewModel", "Products loaded successfully: ${productList.size}")
                        Log.d("ProductViewModel", "Products loaded successfully: ${productList}")
                    } else {
                        Log.w("ProductViewModel", "No products found in API response")
                        // Update UI state, e.g., show empty state
                    }
                } else {
                    Log.e(
                        "ProductViewModel",
                        "API Error: Code ${response.code()}, Message: ${response.message()}"
                    )
                    // Notify UI of error, e.g., via LiveData or StateFlow
                }
            } catch (e: HttpException) {
                Log.e("ProductViewModel", "HttpException: ${e.message()}")
                e.printStackTrace()
                // Notify UI of specific HTTP error
            } catch (e: IOException) {
                Log.e("ProductViewModel", "Network error: ${e.message}")
                e.printStackTrace()
                // Notify UI about network issues
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Unexpected error: ${e.message}")
                e.printStackTrace()
                // Notify UI about unexpected errors
            }
        }
    }


    fun getProductsByCodes(codes: List<String>, onSuccess: (List<Product>) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val products = codes.mapNotNull { code ->
                    try {
                        RetrofitInstance.api.getProductByCode(code) // Lấy sản phẩm qua mã
                    } catch (e: Exception) {
                        null // Bỏ qua nếu không tìm thấy sản phẩm
                    }
                }
                onSuccess(products) // Trả về danh sách sản phẩm
            } catch (e: Exception) {
                onError(e.message ?: "Lỗi không xác định khi lấy sản phẩm.")
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



}