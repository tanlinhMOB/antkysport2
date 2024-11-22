//package com.example.comviet.Screen
//
//import android.content.Context
//import android.content.SharedPreferences
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.rememberImagePainter
//import com.example.comviet.Screen.ui.theme.ComvietTheme
//import com.google.gson.Gson
//
//class Favourite : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            ComvietTheme  {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                    topBar = { TopBar(title = "Yêu Thích") } // Gọi TopBar ở đây
//                ) { innerPadding -> // Nhận giá trị padding từ Scaffold
//                    FavouriteScreen(modifier = Modifier.padding(innerPadding)) // Áp dụng innerPadding
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TopBar(title: String) {
//    TopAppBar(
//        title = {
//            Text(
//                text = title,
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.Center,
//                fontSize = 20.sp
//            )
//        },
//        modifier = Modifier.fillMaxWidth(),
//        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
//    )
//}
//
//@Composable
//fun FavouriteScreen(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val sharedPreferences: SharedPreferences = context.getSharedPreferences("favourites_prefs", Context.MODE_PRIVATE)
//
//    // Lấy danh sách sản phẩm yêu thích từ SharedPreferences
//    var favouriteProducts by remember { mutableStateOf(listOf<ProductItem>()) }
//
//    // Sử dụng LaunchedEffect để cập nhật danh sách yêu thích từ SharedPreferences mỗi khi màn hình khởi tạo
//    LaunchedEffect(key1 = true) {
//        val savedFavourites = sharedPreferences.getString("favouriteItems", "[]")
//        favouriteProducts = Gson().fromJson(savedFavourites, Array<ProductItem>::class.java).toList()
//    }
//
//    // Biến trạng thái để điều khiển dialog hiển thị
//    var showDeleteDialog by remember { mutableStateOf(false) }
//    var productToDelete by remember { mutableStateOf<ProductItem?>(null) } // Sản phẩm đang chọn để xóa
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Thêm tiêu đề màn hình yêu thích
//        Text(
//            text = "Danh Sách Yêu Thích",
//            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
//            color = Color.Black,
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally) // Căn giữa chữ theo chiều ngang
//                .padding(bottom = 16.dp)
//        )
//
//        LazyColumn(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            items(favouriteProducts) { product ->
//                FavouriteItem(product = product, onDelete = {
//                    // Hiển thị dialog khi nhấn xóa
//                    productToDelete = product
//                    showDeleteDialog = true
//                })
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }
//
//    // Hiển thị dialog xác nhận xóa
//    if (showDeleteDialog && productToDelete != null) {
//        AlertDialog(
//            onDismissRequest = { showDeleteDialog = false }, // Đóng dialog khi click ra ngoài
//            title = {
//                Text(text = "Xóa sản phẩm")
//            },
//            text = {
//                Text(text = "Bạn có chắc chắn muốn xóa sản phẩm này khỏi danh sách yêu thích không?")
//            },
//            confirmButton = {
//                TextButton(onClick = {
//                    productToDelete?.let { product ->
//                        // Xóa sản phẩm khỏi danh sách yêu thích
//                        val updatedFavourites = favouriteProducts.toMutableList()
//                        updatedFavourites.remove(product)
//
//                        // Cập nhật lại SharedPreferences
//                        val editor = sharedPreferences.edit()
//                        editor.putString("favouriteItems", Gson().toJson(updatedFavourites))
//                        editor.apply()
//
//                        // Cập nhật lại danh sách hiển thị
//                        favouriteProducts = updatedFavourites
//                    }
//                    showDeleteDialog = false // Đóng dialog sau khi xóa
//                }) {
//                    Text("Có")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = {
//                    showDeleteDialog = false // Đóng dialog khi chọn Hủy
//                }) {
//                    Text("Hủy")
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun FavouriteItem(product: ProductItem, onDelete: () -> Unit, modifier: Modifier = Modifier) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .border(2.dp, Color.Black, RoundedCornerShape(8.dp))
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        // Cột bên trái - Hình ảnh sản phẩm
//        Image(
//            painter = rememberImagePainter(product.imageUrl),
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(80.dp)
//                .padding(end = 16.dp)
//        )
//
//        // Cột bên phải - Tên và giá sản phẩm
//        Column(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = product.name,
//                style = MaterialTheme.typography.titleMedium
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "$${product.price}",
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color(0xFF6200EE)
//            )
//        }
//
//        // Nút xóa
//        IconButton(onClick = { onDelete() }) {
//            Icon(
//                imageVector = Icons.Filled.Delete,
//                contentDescription = "Xóa sản phẩm",
//                tint = Color.Red
//            )
//        }
//    }
//}