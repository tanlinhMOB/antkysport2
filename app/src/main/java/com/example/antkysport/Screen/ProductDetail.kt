@file:OptIn(ExperimentalLayoutApi::class)

package com.example.antkysport.Screen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.antkysport.Screen.ui.theme.ComvietTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import com.example.antkysport.ViewModel.AuthViewModel

class ProductDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComvietTheme {
                val navController = rememberNavController()
                val authViewModel = AuthViewModel(this)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        ProductDetailScreen(
                            navController = navController,
                            backStackEntry = navController.previousBackStackEntry!!,
                            authViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                )
            }
        }
    }
}
@Composable
fun ProductDetailScreen(navController: NavHostController, backStackEntry: NavBackStackEntry, authViewModel: AuthViewModel,modifier: Modifier = Modifier) {
    val productTitle = backStackEntry.arguments?.getString("title") ?: ""
    val productCode = backStackEntry.arguments?.getString("code") ?: ""
    val productPrice = backStackEntry.arguments?.getString("price")?.toDouble() ?: 0.0
    val productImage = backStackEntry.arguments?.getString("image") ?: ""
    val productQuantity = backStackEntry.arguments?.getString("quantity") ?.toInt() ?:0
    val productDescription = backStackEntry.arguments?.getString("description") ?:""
    val productColor = backStackEntry.arguments?.getString("color")?.split(",") ?: emptyList() // Dạng List<String>
    val productSize = backStackEntry.arguments?.getString("size")?.split(",") ?: emptyList()   // Dạng List<String>

    // Xác định trạng thái hiển thị dựa trên productQuantity
    val productStatusText = if (productQuantity > 0) "Còn hàng" else "Đang hết hàng"
    val productStatusColor = if (productQuantity > 0) Color.Green else Color.Red
    var selectedsize by remember { mutableStateOf<String?>(null) } // Lưu giá trị size được chọn
    var selectedcolor by remember { mutableStateOf<String?>(null) } // Lưu giá trị color được chọn
    val context = LocalContext.current

    Log.d("pd", "selectedsize: "+selectedsize)
    Log.d("pd", "selectedcolor: "+selectedcolor)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 90.dp)
            .background(Color(0xFFF8F9FA)) // Màu nền sáng nhẹ cho toàn màn hình
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
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
                    fontSize = 32.sp,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(25.dp))
                Row( modifier = Modifier
                    .fillMaxWidth(), // Row sẽ chiếm toàn bộ chiều rộng
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Mã: $productCode",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Hiển thị Text với trạng thái
                    Text(
                        text = "Trạng thái: ${productStatusText}",
                        color = productStatusColor,
                        style = MaterialTheme.typography.bodyLarge // Bạn có thể thay đổi kiểu hiển thị ở đây
                    )
                }


                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Giá: $productPrice vnđ",
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Red),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
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
            }

            item {
                Text(
                    text = "Chọn cỡ:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SizeRadioButtons(productSize, onSizeSelected = {size-> selectedsize = size})

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Chọn màu sắc:",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ColorRadioButtons(productColor, onColorSelected = {color-> selectedcolor = color})

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                var quantity by remember { mutableStateOf(1) } // Biến lưu số lượng sản phẩm

                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    // Hàng chứa các nút tăng giảm số lượng
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Số lượng:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Text giảm số lượng
                            Text(
                                text = "-",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (quantity > 1) Color.Black else Color.Gray, // Màu sắc thay đổi theo trạng thái
                                modifier = Modifier
                                    .clickable(enabled = quantity > 1) { if (quantity > 1) quantity-- } // Giảm số lượng nếu > 1
                                    .padding(8.dp) // Thêm khoảng trống
                            )

                            // Hiển thị số lượng
                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black
                            )

                            // Text tăng số lượng
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (quantity < 5) Color.Black else Color.Gray, // Màu sắc thay đổi theo trạng thái
                                modifier = Modifier
                                    .clickable(enabled = quantity < 5) { if (quantity < 5) quantity++ } // Tăng số lượng nếu < 5
                                    .padding(8.dp) // Thêm khoảng trống
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa các thành phần

                    // Nút "Thêm vào giỏ"

                    Button(
                        onClick = {
                            // Gọi ViewModel để thêm sản phẩm vào giỏ hàng
                            if(selectedsize!=null&&selectedcolor!=null){
                                authViewModel.addToCart(
                                    code = productCode,
                                    size_item = selectedsize,
                                    color = selectedcolor,
                                    quantity_cart = quantity, // Truyền số lượng hiện tại
                                    onSuccess = {
                                        Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ", Toast.LENGTH_SHORT).show()
                                        navController.navigate("cart")
                                    },
                                    onError = {
                                        Toast.makeText(context, "Lỗi thêm sản phẩm", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }else{
                                Toast.makeText(context, "Vui lòng chọn size và màu trước khi thêm !", Toast.LENGTH_SHORT).show()
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(25.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text(text = "Thêm vào giỏ", fontSize = 18.sp, color = Color.White)
                    }
                }
            }

        }
    }
}

@Composable
fun SizeRadioButtons(sizes: List<String>,onSizeSelected: (String) -> Unit) {

    var selectedSize by remember { mutableStateOf<String?>(null) } // Keeps track of the selected size

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        sizes.forEach { size ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                RadioButton(
                    selected = selectedSize == size,
                    onClick = {
                        selectedSize = size // Update the selected size
                        onSizeSelected(size)
                    }
                )
                Text(
                    text = size,
                    modifier = Modifier.padding(start = 4.dp), // Add space between radio button and label
                    color = if (selectedSize == size) Color.Black else Color.Gray,
                    fontWeight = if (selectedSize == size) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun ColorRadioButtons(colors: List<String>,onColorSelected:(String)-> Unit) {
    var selectedColor by remember { mutableStateOf<String?>(null) }

    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        colors.forEach { color ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                RadioButton(
                    selected = selectedColor == color,
                    onClick = {
                        selectedColor = color
                        onColorSelected(color)
                    }
                )
                Text(
                    text = color,
                    modifier = Modifier.padding(start = 8.dp),
                    color = if (selectedColor == color) Color.Black else Color.Gray,
                    fontWeight = if (selectedColor == color) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

