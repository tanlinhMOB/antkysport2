package com.example.antkysport.Screen

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.antkysport.Screen.ui.theme.ComvietTheme
import com.example.antkysport.ViewModel.AuthViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import com.example.antkysport.Model.OrderResponse
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class HistoryPaymentScreen : ComponentActivity() {
    private lateinit var viewModel: AuthViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = AuthViewModel(this) // Khởi tạo ViewModel với context

        setContent {
            ComvietTheme {
                val navController = rememberNavController()
                History(navController = navController, viewModel = viewModel)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun History(navController: NavController, viewModel: AuthViewModel) {

    // Fetch orders data when the composable is launched
    LaunchedEffect(Unit) {
        viewModel.fetchOrders()  // Gọi hàm fetchOrders() để tải dữ liệu
    }

    // Lấy dữ liệu đơn hàng từ StateFlow
    val orders = viewModel.ordersResponse.collectAsState().value?.orders ?: emptyList()

    Log.d("History", "Orders in Composable: $orders")
// Lọc các đơn hàng có trạng thái "Received"
    val receivedOrders = orders.filter { it.status == "Received" }
    val myInvoices = orders.filter { it.status != "Received" }
    val tabItems = listOf(
        TabItem(
            modifier = Modifier.padding(top = 25.dp),
            title = "Hóa đơn của tôi",
            content = { MyInvoicesTab(orders = myInvoices) } // Truyền danh sách đơn hàng vào MyInvoicesTab
        ),
        TabItem(
            modifier = Modifier.padding(top = 25.dp),
            title = "Lịch sử thanh toán",
            content = { PaymentHistoryTab(orders = receivedOrders) }
        ),
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var selectedTabIndex by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { index, item ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                item.title,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Hiển thị nội dung của tab đã chọn
            tabItems[selectedTabIndex].content()
        }
    }
}



data class TabItem(
    val modifier: Modifier = Modifier,
    val title: String,
    val content: @Composable () -> Unit // Hàm Composable để hiển thị nội dung cho tab
)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyInvoicesTab(orders: List<OrderResponse>) {
    LazyColumn {
        items(orders) { order ->
            // Gọi OrderCard cho mỗi đơn hàng
            OrderCard(order = order)
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderCard(order: OrderResponse) {
    // Sử dụng Card thay vì Surface để tạo hiệu ứng đổ bóng
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Khoảng cách giữa các card
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium // Tạo các góc bo tròn
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Hóa đơn: ${order.orderId}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Thành tiền: ${order.totalAmount} đ", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Ngày tạo: ${formatDate(order.createdAt)}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Trạng thái: ${order.status}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(createdAt: String): String {
    // Parse chuỗi ngày giờ theo định dạng ISO 8601
    val zonedDateTime = ZonedDateTime.parse(createdAt)

    // Định dạng lại ngày giờ theo định dạng "dd/MM/yyyy - HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")
    return zonedDateTime.format(formatter)
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentHistoryTab(orders: List<OrderResponse>) {
    LazyColumn {
        items(orders) { order ->
            // Gọi OrderCard cho mỗi đơn hàng
            OrderCard(order = order)
        }
    }
}
