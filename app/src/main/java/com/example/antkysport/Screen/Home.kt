package com.example.antkysport.Screen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.antkysport.R
import com.example.antkysport.Screen.ui.theme.ComvietTheme
import com.example.antkysport.ViewModel.ProductViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComvietTheme{
                // Khởi tạo NavController để điều hướng giữa các màn hình
                val navController = rememberNavController()

                // Scaffold chứa bố cục chính của ứng dụng, bao gồm bottom bar và phần nội dung chính
                Scaffold(
                    bottomBar = { BottomAppBarContent(navController, "Home") } // Thiết lập thanh điều hướng dưới cùng
                ) {
                    NavHostScreen(navController = navController, productViewModel = ProductViewModel()) // Đặt NavHost để điều hướng màn hình
                }

            }
        }
    }
}

@Composable
fun NavHostScreen(navController: NavHostController, productViewModel: ProductViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                productViewModel = productViewModel
            )
        }

        composable(
            "productDetail/{title}/{code}/{quantity}/{price}/{size}/{color}/{image}/{category}/{description}/{createdAt}/{updatedAt}",
        ) { backStackEntry ->
            ProductDetailScreen(navController = navController, backStackEntry = backStackEntry)
        }
        composable("cart") {
            Cart(navController = navController) // Màn hình giỏ hàng
        }
        composable("history") {
            History(navController)
        }
        composable("account"){
            ProfileScreenUI(navController)
        }

        composable("wallet"){
            WalletCompose(navController)
        }

    }
}


@Composable
fun SearchBar(productViewModel: ProductViewModel) {
    var searchText by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
            },
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            placeholder = { Text("Tìm kiếm tên sản phẩm") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null ,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                // Nếu đang tìm kiếm, hiển thị icon hủy
                if (isSearching) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        modifier = Modifier.clickable {
                            searchText = "" // Xóa nội dung tìm kiếm
                            isSearching = false
                            productViewModel.fetchProducts() // Gọi lại danh sách đầy đủ
                        }
                    )
                }
            },
            shape = MaterialTheme.shapes.small
        )
        Spacer(modifier = Modifier.width(8.dp))

        // Nút tìm kiếm
        Button(
            onClick = {
                if (searchText.isNotEmpty()) {
                    productViewModel.searchProduct(searchText) // Gọi tìm kiếm khi nhấn nút
                    isSearching = true // Đặt trạng thái đang tìm kiếm
                }
            },
            modifier = Modifier.height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Tìm kiếm")
        }
    }
}


@Composable
fun BannerSlider(imageList: List<Int>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(imageList) { imageRes ->
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(360.dp)
                    .height(180.dp)
            )
        }
    }
}

@Composable
fun CategoryItem(imageRes: Int, title: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp) // Khoảng cách xung quanh Card
            .clickable { /* Xử lý sự kiện khi click vào Category */ },
        elevation = 4.dp, // Đổ bóng cho Card
        shape = MaterialTheme.shapes.medium // Bo góc cho Card
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp) // Padding bên trong Card
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
        }
    }
}


@Composable
fun CategoryList(categories: List<Pair<Int, String>>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(imageRes = category.first, title = category.second)
        }
    }
}

@Composable
fun ProductItem(
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
    modifier: Modifier = Modifier,
    onclick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onclick),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(MaterialTheme.colors.surface)
        ) {
            Image(
                painter = rememberImagePainter(data = image), // Sử dụng Coil để tải hình ảnh từ URL
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$price $",
                    style = MaterialTheme.typography.button.copy(color = Color.Red)
                )
                Text(
                    text = "Code: $code",
                    style = MaterialTheme.typography.button,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ProductListScreen(navController: NavHostController, productViewModel: ProductViewModel) {
//    val products = if (productViewModel.searchResults.isNotEmpty()) {
//        Log.d("ProductListScreen", "Displaying search results: ${productViewModel.searchResults.joinToString { it.title }}")
//        productViewModel.searchResults
//    } else {
//        Log.d("ProductListScreen", "Displaying full product list: ${productViewModel.productList.joinToString { it.title }}")
//        productViewModel.productList
//    }
    val products = listOf(
        ProductItem(
            title = "Sản phẩm 1",
            code = "SP001",
            quantity = 10,
            price = 100.0,
            size = arrayOf("S", "M", "L"),
            color = arrayOf("Đỏ", "Xanh", "Vàng"),
            image = "https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcT5CdY9VHE33vBsrR95psqSIKh6mXAIj2e2hwnuWOBFqQexIoFOpCei3SKlYnBxpd7_rucqcQ3IyjlxkVIC5DDJDkAdYIDt0wsTd_qhaT-ME6Vy1kdp0bUcog&usqp=CAc",
            category = "Áo thun",
            description = "Mô tả sản phẩm 1",
            createdAt = "2024-11-21",
            updatedAt = "2024-11-21"
        ),
        ProductItem(
            title = "Sản phẩm 2",
            code = "SP002",
            quantity = 5,
            price = 150.0,
            size = arrayOf("M", "L"),
            color = arrayOf("Đen", "Trắng"),
            image = "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcR1y5mgINhTGxjG8ckeeGN2fvmca4bRyPYPJDnHiOTW6aeC-8aeB_e2_6RCd3G-0SZVbvJK0LWJ0PzSwHmmrZ2dPicGBJ_9j0HFGhNM6QoanYnUK5Ez-gs8iSQm3yaQqmbwwaFsTg&usqp=CAc",
            category = "Áo thun",
            description = "Mô tả sản phẩm 2",
            createdAt = "2024-11-20",
            updatedAt = "2024-11-21"
        )
    )
    val isRefreshing = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    SwipeRefresh(
        state = SwipeRefreshState(isRefreshing.value),
        onRefresh = {
            coroutineScope.launch {
                isRefreshing.value = true
                // Gọi lại hàm để tải lại sản phẩm
                productViewModel.fetchProducts()
                isRefreshing.value = false
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                // Mã hóa URL
                val encodedImageUrl = URLEncoder.encode(product.image, StandardCharsets.UTF_8.toString())
                val encodedSize = URLEncoder.encode(product.size.joinToString(","), StandardCharsets.UTF_8.toString())
                val encodedColor = URLEncoder.encode(product.color.joinToString(","), StandardCharsets.UTF_8.toString())
                ProductItem(
                    title = product.title,
                    code = product.code,
                    quantity = product.quantity,
                    price = product.price,
                    size = product.size,
                    color = product.color,
                    image = product.image,
                    category = product.category,
                    description = product.description,
                    createdAt = product.createdAt,
                    updatedAt = product.updatedAt,
                    onclick = {
                        navController.navigate(
                            "productDetail/${product.title}/${product.code}/${product.quantity}/${product.price}/$encodedSize/$encodedColor/$encodedImageUrl/${product.category}/${product.description}/${product.createdAt}/${product.updatedAt}"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, productViewModel: ProductViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
    ) {
        SearchBar(productViewModel= ProductViewModel())
        Spacer(modifier = Modifier.height(16.dp))

        // Phần banner
        val imageList = listOf(
            R.drawable.banner1,
            R.drawable.banner2
        )
        BannerSlider(imageList = imageList)
        Spacer(modifier = Modifier.height(16.dp))

        // Phần danh mục (CategoryList)
        Text(
            text = "Danh Mục",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        val categories = listOf(
            Pair(R.drawable.category1, "Category 1"),
            Pair(R.drawable.category2, "Category 2"),
            Pair(R.drawable.category3, "Category 3")
        )
        CategoryList(categories)
        Spacer(modifier = Modifier.height(16.dp))

        // Phần sản phẩm (ProductListScreen)
        Text(
            text = "Sản Phẩm",
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ProductListScreen(navController = navController, productViewModel = productViewModel)
    }
}


@Composable
fun BottomAppBarContent(navController: NavHostController, currentScreen: String) {
    val background_btnBottom_navi = Color(0xFFFFFFFF)
    val selected_btn_choose = Color(0xFF000000) // Màu đen cho mục được chọn
    val selected_btn_not_choose = Color(0xFFBEBEBE) // Màu xám khói cho mục không được chọn

    BottomNavigation(
        backgroundColor = background_btnBottom_navi,
        modifier = Modifier
            .height(100.dp)
            .navigationBarsPadding()
    ) {

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Trang Chủ") },
            label = { Text("Home", textAlign = TextAlign.Center) },
            selected = currentScreen == "home", // Kiểm tra nếu màn hình hiện tại là "home"
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = selected_btn_choose, // Màu nâu cho mục được chọn
            unselectedContentColor = selected_btn_not_choose, // Màu xanh nhạt cho mục không được chọn
            modifier = Modifier
                .height(if (currentScreen == "home") 120.dp else 100.dp) // Phóng to khi được chọn
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Bill, contentDescription = "Hóa dơn") },
            label = { Text("Hóa đơn", textAlign = TextAlign.Center) },
            selected = currentScreen == "history", // Kiểm tra nếu màn hình hiện tại là "favorites"
            onClick = {
                navController.navigate("history")
            },
            selectedContentColor = selected_btn_choose,
            unselectedContentColor = selected_btn_not_choose,
            modifier = Modifier
                .height(if (currentScreen == "history") 120.dp else 100.dp)
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Giỏ hàng") },
            label = { Text("Giỏ hàng", textAlign = TextAlign.Center) },
            selected = currentScreen == "cart", // Kiểm tra nếu màn hình hiện tại là "cart"
            onClick = {
                navController.navigate("cart") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = selected_btn_choose,
            unselectedContentColor = selected_btn_not_choose,
            modifier = Modifier
                .height(if (currentScreen == "cart") 120.dp else 100.dp)
        )

        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Tài Khoản") },
            label = { Text("Tài Khoản", textAlign = TextAlign.Center) },
            selected = currentScreen == "account", // Kiểm tra nếu màn hình hiện tại là "account"
            onClick = {
                navController.navigate("account")
            },
            selectedContentColor = selected_btn_choose,
            unselectedContentColor = selected_btn_not_choose,
            modifier = Modifier
                .height(if (currentScreen == "account") 120.dp else 100.dp)
        )

    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val fakeProductViewModel = ProductViewModel() // Fake ProductViewModel để test preview
    ComvietTheme {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomAppBarContent(navController, "Home") }
        ) {
            HomeScreen(navController = navController, productViewModel = fakeProductViewModel)
        }
    }
}
