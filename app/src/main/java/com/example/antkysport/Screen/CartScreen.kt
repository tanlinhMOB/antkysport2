package com.example.antkysport.Screen

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.antkysport.Model.CartItemRespone
import com.example.antkysport.Model.OrderItem
import com.example.antkysport.Model.idRequest
import com.example.antkysport.R
import com.example.antkysport.Screen.ui.theme.ComvietTheme
import com.example.antkysport.ViewModel.AuthViewModel
import com.example.antkysport.ViewModel.ProductViewModel

class CartScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel = AuthViewModel(this)
            val productViewModel = ProductViewModel()
            ComvietTheme {
                val navController = rememberNavController() // Khởi tạo NavHostController
                CartScreen(navController=navController, viewModel=authViewModel, productViewModel = productViewModel)
            }
        }
    }
}
@Composable
fun CartScreen(navController: NavHostController, viewModel: AuthViewModel, productViewModel: ProductViewModel) {
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }
    var cartItemToDelete by remember { mutableStateOf<CartItemRespone?>(null) }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val cartItems by viewModel.cartItems.collectAsState(initial = emptyList())
    var totalAmount by remember { mutableStateOf(0.0) }

    // Gọi fetchcart() khi composable được khởi chạy
    LaunchedEffect(Unit) {
        viewModel.fetchCart()
    }

    // Tính tổng tiền items
    LaunchedEffect(cartItems) {
        val codes = cartItems.map { it.code }
        productViewModel.getProductsByCodes(
            codes = codes,
            onSuccess = { products ->
                totalAmount = cartItems.sumOf { cartItem ->
                    val product = products.find { it.code == cartItem.code }
                    (product?.price ?: 0.0) * cartItem.quantity_cart
                }
            },
            onError = { errorMessage ->
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 110.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Tiêu đề Giỏ Hàng
        item {
            Text(
                text = "Giỏ Hàng",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        // Kiểm tra giỏ hàng có item không
        if (cartItems != null && cartItems.isNotEmpty()) {
            // Danh sách giỏ hàng
            items(cartItems) { item ->
                CartItemView(
                    item = item,
                    onDeleteClick = {
                        cartItemToDelete = item
                        showDeleteDialog = true
                    }
                )
            }

            // Tổng tiền
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = "Tổng cộng: $$totalAmount",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.End,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

            // Divider
            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            // Card hiển thị địa chỉ và số điện thoại
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Nhập Thông Tin Đặt Hàng",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        // Nhập địa chỉ
                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Địa chỉ") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Nhập số điện thoại
                        OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Số điện thoại") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
                        )
                    }
                }
            }

            // Thanh toán button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (address.isEmpty() || phoneNumber.isEmpty()) {
                            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val orderItems = cartItems.map { cartItem ->
                            OrderItem(
                                code = cartItem.code,
                                size = cartItem.size_item ?: "",    // Xử lý trường hợp null
                                color = cartItem.color ?: "",       // Xử lý trường hợp null
                                quantity = cartItem.quantity_cart
                            )
                        }

                        viewModel.checkout(
                            items = orderItems,
                            address = address,
                            phoneNumber = phoneNumber,
                            totalAmount = totalAmount,
                            onSuccess = {
                                Toast.makeText(context, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()
                                navController.navigate("home") // Hoặc chuyển hướng đến màn hình khác
                            },
                            onError = { errorMessage ->
                                Toast.makeText(context, "Đặt hàng thất bại: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(text = "Thanh Toán", color = Color.White)
                }

            }
        } else {
            // Giỏ hàng rỗng
            item {
                EmptyCartView(navController)
            }
        }

        // Xử lý xóa item khi hiển thị dialog
        if (showDeleteDialog && cartItemToDelete != null) {
            item {
                ConfirmDeleteDialog(
                    onConfirm = {
                        cartItemToDelete?.let { itemToDelete ->
                            viewModel.delCartByIdItem(
                                _id = idRequest(itemToDelete._id),
                                onSuccess = {
                                    // Thành công: cập nhật giỏ hàng
                                    viewModel.fetchCart()
                                    Toast.makeText(context, "Đã xóa item ${itemToDelete.code}", Toast.LENGTH_SHORT).show()
                                },
                                onError = { errorMessage ->
                                    // Hiển thị thông báo lỗi
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                        showDeleteDialog = false
                        cartItemToDelete = null
                    },
                    onDismiss = {
                        showDeleteDialog = false
                        cartItemToDelete = null
                    }
                )
            }
        }
    }
}

@Composable
fun CartItemView(item: CartItemRespone, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors( // Đặt màu nền cho Card
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sản phẩm: ${item.code}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Kích cỡ: ${item.size_item.orEmpty()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Màu sắc: ${item.color.orEmpty()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Số lượng: ${item.quantity_cart}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF6200EE)
                )
            }

            // Cập nhật kích thước Icon
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(25.dp) // Kích thước icon lớn hơn
                )
            }
        }
    }

}

@Composable
fun EmptyCartView(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Giỏ hàng rỗng",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text(text = "Về trang chủ", color = Color.White)
        }
    }
}

@Composable
fun ConfirmDeleteDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Xác nhận xóa") },
        text = { Text(text = "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?") },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = "Xóa")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = "Hủy")
            }
        }
    )
}

@Composable
fun PaymentDialog(onDismiss: () -> Unit, onPaymentOptionSelected: (String) -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Chọn phương thức thanh toán") },
        text = {
            Column {
                Button(
                    onClick = { onPaymentOptionSelected("Thanh toán tại nhà") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Thanh toán tại nhà")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onPaymentOptionSelected("Thanh toán bằng thẻ Visa") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Thanh toán bằng thẻ Visa")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Hủy")
            }
        }
    )
}

