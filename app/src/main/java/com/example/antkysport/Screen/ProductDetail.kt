package com.example.antkysport.Screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.antkysport.Screen.ui.theme.ComvietTheme

import com.google.gson.Gson

class ProductDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComvietTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        ProductDetailScreen(
                            navController = navController,
                            backStackEntry = navController.previousBackStackEntry!!,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                )
            }
        }
    }
}

data class ProductItem(
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
    val updatedAt: String
)

data class CartItem(
    val code: String,
    val quantity: Number
)

@Composable
fun ProductDetailScreen(navController: NavHostController, backStackEntry: NavBackStackEntry, modifier: Modifier = Modifier) {
    val productTitle = backStackEntry.arguments?.getString("title") ?: ""
    val productCode = backStackEntry.arguments?.getString("code") ?: ""
    val productQuantity = backStackEntry.arguments?.getString("quantity")?.toInt() ?: 0
    val productPrice = backStackEntry.arguments?.getString("price")?.toDouble() ?: 0.0

    val sizeString = backStackEntry.arguments?.getString("size") ?: ""
    val colorString = backStackEntry.arguments?.getString("color") ?: ""
    val productSize = if (sizeString.isNotEmpty()) sizeString.split(",") else emptyList()
    val productColor = if (colorString.isNotEmpty()) colorString.split(",") else emptyList()

    val productImage = backStackEntry.arguments?.getString("image") ?: ""
    val productCategory = backStackEntry.arguments?.getString("category") ?: ""
    val productDescription = backStackEntry.arguments?.getString("description") ?: ""
    val productCreatedAt = backStackEntry.arguments?.getString("createdAt") ?: ""
    val productUpdatedAt = backStackEntry.arguments?.getString("updatedAt") ?: ""

    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF8F9FA)) // Màu nền sáng nhẹ cho toàn màn hình
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberImagePainter(productImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp)) // Bo góc hình ảnh
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = productTitle,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                fontSize = 32.sp, // Điều chỉnh kích thước chữ
                color = Color(0xFF333333) // Màu chữ tối
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Mã sản phẩm: $productCode",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF666666) // Màu chữ trung tính cho thông tin phụ
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Giá: $productPrice",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium // Chữ in đậm cho giá sản phẩm
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mô tả:",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF333333)
            )
            Text(
                text = productDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(24.dp))

            // Nút "Mua hàng"
            Button(
                onClick = {
                    val sharedPreferences: SharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    val newProduct = ProductItem(
                        title = productTitle,
                        code = productCode,
                        quantity = productQuantity,
                        price = productPrice,
                        size = productSize.toTypedArray(),
                        color = productColor.toTypedArray(),
                        image = productImage,
                        category = productCategory,
                        description = productDescription,
                        createdAt = productCreatedAt,
                        updatedAt = productUpdatedAt
                    )

                    val existingProducts = sharedPreferences.getString("cartItems", "[]")
                    val productList = Gson().fromJson(existingProducts, Array<ProductItem>::class.java).toMutableList()

                    productList.add(newProduct)

                    editor.putString("cartItems", Gson().toJson(productList))
                    editor.apply()

                    Toast.makeText(context, "Mua hàng thành công", Toast.LENGTH_SHORT).show()

                    navController.navigate("cart")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Mua hàng", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nút "Yêu thích"
            Button(
                onClick = {
                    val sharedPreferences: SharedPreferences = context.getSharedPreferences("favourites_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    // Tạo đối tượng sản phẩm mới
                    val favouriteProduct = ProductItem(
                        title = productTitle,
                        code = productCode,
                        quantity = productQuantity,
                        price = productPrice,
                        size = productSize.toTypedArray(),
                        color = productColor.toTypedArray(),
                        image = productImage,
                        category = productCategory,
                        description = productDescription,
                        createdAt = productCreatedAt,
                        updatedAt = productUpdatedAt
                    )

                    // Lấy danh sách sản phẩm yêu thích hiện tại từ SharedPreferences
                    val existingFavourites = sharedPreferences.getString("favouriteItems", "[]")
                    val favouriteList = Gson().fromJson(existingFavourites, Array<ProductItem>::class.java).toMutableList()

                    // Thêm sản phẩm vào danh sách yêu thích
                    favouriteList.add(favouriteProduct)

                    // Lưu lại danh sách yêu thích đã cập nhật
                    editor.putString("favouriteItems", Gson().toJson(favouriteList))
                    editor.apply()
                    Toast.makeText(context, Gson().toJson(favouriteList), Toast.LENGTH_LONG).show()
                    Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show()
                    // Điều hướng sang màn hình yêu thích sau khi thêm
                    navController.navigate("favourite")  // Điều hướng tới màn hình yêu thích
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(25.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text(text = "Yêu thích", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ComvietTheme  {
        val navController = rememberNavController()
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { innerPadding ->
                ProductDetailScreen(
                    navController = navController,
                    backStackEntry = navController.previousBackStackEntry!!,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}