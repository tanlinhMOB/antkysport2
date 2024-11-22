package com.example.antkysport.Screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.antkysport.Model.Cart
import com.example.antkysport.R
import com.example.antkysport.Screen.ui.theme.ComvietTheme
import com.google.gson.Gson

class CartScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComvietTheme {
                val navController = rememberNavController() // Khởi tạo NavHostController
                Cart(navController = navController)
            }
        }
    }
}

@Composable
fun Cart(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE)

    // Lấy danh sách sản phẩm từ SharedPreferences
    var cartList by remember {
        mutableStateOf(
            listOf(
                Gson().fromJson(sharedPreferences.getString("cartItems", "[]"), Array<Cart>::class.java).toList()
            )
        )
    }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPaymentSuccess by remember { mutableStateOf(false) }
    var cartToDelete by remember { mutableStateOf<Cart?>(null) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Giỏ Hàng",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center
        )

        if (cartList.isEmpty()) {
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
        } else {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
//                cartList.forEach { cart ->
//                    CartItem(
//                        code = cart.code,
//                        quantity = cart.quantity,
//                        onDeleteClick = {
//                            cartToDelete = cart
//                            showDeleteDialog = true
//                        }
//                    )
//                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showPaymentDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(text = "Thanh Toán", color = Color.White)
            }

            if (showPaymentDialog) {
                PaymentDialog(
                    onDismiss = { showPaymentDialog = false },
                    onPaymentOptionSelected = { option ->
                        if (option == "Thanh toán tại nhà") {
                            cartList = emptyList()

                            val editor = sharedPreferences.edit()
                            editor.putString("cartItems", Gson().toJson(cartList))
                            editor.apply()

                            showPaymentSuccess = true
                        }
                        showPaymentDialog = false
                    }
                )
            }

            if (showPaymentSuccess) {
                AlertDialog(
                    onDismissRequest = { showPaymentSuccess = false },
                    title = { Text("Thông báo") },
                    text = { Text("Thanh toán thành công!") },
                    confirmButton = {
                        Button(onClick = { showPaymentSuccess = false }) {
                            Text("OK")
                        }
                    }
                )
            }

            if (showDeleteDialog && cartToDelete != null) {
                ConfirmDeleteDialog(
                    onConfirm = {
//                        cartList = cartList.filter { it != cartToDelete }

                        val editor = sharedPreferences.edit()
                        editor.putString("cartItems", Gson().toJson(cartList))
                        editor.apply()

                        showDeleteDialog = false
                        cartToDelete = null
                    },
                    onDismiss = {
                        showDeleteDialog = false
                        cartToDelete = null
                    }
                )
            }
        }
    }
}

@Composable
fun CartItem(code: String, quantity: Number, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Mã: $code",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Số lượng: $quantity",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF6200EE)
                )
            }

            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
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
fun CartSummary(subtotal: Double, delivery: Double, tax: Double) {
    val calculatedDelivery = if (subtotal > 0) delivery else 0.0
    val calculatedTax = if (subtotal > 0) tax else 0.0
    val totalAmount = subtotal + calculatedDelivery + calculatedTax

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Tổng cộng
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tổng cộng")
            Text(text = "$$subtotal")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Phí vận chuyển
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Phí vận chuyển")
            Text(text = "$$calculatedDelivery")
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Thuế
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Thuế")
            Text(text = "$$calculatedTax")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Tổng tiền
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Tổng tiền", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "$$totalAmount",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
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

@Preview(showBackground = true)
@Composable
fun PreviewCartScreen() {
    val navController = rememberNavController()
    Cart(navController = navController)
}
