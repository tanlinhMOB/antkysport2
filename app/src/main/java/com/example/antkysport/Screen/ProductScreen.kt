package com.example.antkysport.Screen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.antkysport.R
import com.example.antkysport.Screen.ui.theme.ComvietTheme
import com.example.antkysport.ViewModel.UserViewModel


class ProductScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            ComvietTheme  {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileScreenUI()
//                }
            }
        }
    }


@Composable
fun ProfileScreenUI(
                    navController: NavController? = null,
                    userViewModel: UserViewModel = viewModel()
) { // Thêm NavController
    var showDialog by remember { mutableStateOf(false) } // Biến trạng thái để kiểm soát dialog
//    val userName by userViewModel.userName.observeAsState("Tên Người Dùng")
//    val userEmail by userViewModel.userEmail.observeAsState("email@domain.com")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)) // Màu nền cho phần dưới
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF)) // Màu xanh của phần header
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back), // Thay bằng icon của bạn
                contentDescription = "Back",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { /* Handle back click */ },
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    // Avatar
                    Image(
                        painter = painterResource(R.drawable.avt), // Thay bằng ảnh avatar của bạn
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }

                // Name
                Text(
                    text = "userName",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                // Email
                Text(
                    text = "userEmail",
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        Divider(
            color = Color.Gray, // Màu của đường phân cách
            thickness = 0.40.dp, // Độ dày của đường phân cách
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Menu Items
        ProfileMenuItem(iconId = R.drawable.btn_1, title = "Thông Báo", onClick = { /* Handle click */ })
        ProfileMenuItem(iconId = R.drawable.btn_2, title = "Sự Kiện", onClick = { /* Handle click */ })
        ProfileMenuItem(iconId = R.drawable.btn_5, title = "Chia Sẻ", onClick = { /* Handle click */ })
        ProfileMenuItem(iconId = R.drawable.wallet , title = "Ví tiền", onClick =  {
        navController?.navigate("wallet")
            Log.e("zzzzz", "ProfileScreenUI: Da vao", null)
        })
        ProfileMenuItem(iconId = R.drawable.btn_6, title = "Đăng Xuất", onClick = {
            showDialog = true // Khi nhấn Đăng Xuất, hiển thị dialog
        })

        // Dialog Đăng Xuất
        if (showDialog) {
            LogoutDialog(
                onConfirm = {
                    showDialog = false
                    navController?.navigate("login") { // Điều hướng về màn hình đăng nhập
                        popUpTo("profile") { inclusive = true } // Xóa màn hình profile khỏi backstack
                    }
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun ProfileMenuItem(iconId: Int, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable(onClick = onClick), // Truyền sự kiện onClick vào đây
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        // Arrow Icon
        Icon(
            painter = painterResource(id = R.drawable.arrow),
            contentDescription = null,
            tint = Color(0xFFB0B0B0),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun LogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Đăng Xuất") },
        text = { Text(text = "Bạn có chắc chắn muốn đăng xuất không?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(text = "Đăng Xuất")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "Hủy")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ComvietTheme  {
        ProfileScreenUI()
    }
}