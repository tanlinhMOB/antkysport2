package com.example.antkysport.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.antkysport.R
import androidx.compose.material.Card
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.example.antkysport.Screen.ui.theme.ComvietTheme


class WalletScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComvietTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "wallet") {
                    composable("wallet"){ WalletCompose(navController) }
                }

            }
        }
    }
}

@Composable
fun WalletCompose(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC)) // light background color
            .padding(16.dp)
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(16.dp))
        BalanceCard(navController = navController )
        Spacer(modifier = Modifier.height(24.dp))
        TransactionList()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Wallet",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BalanceCard(navController: NavController) {
    var showAddMoneyDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material3.Text(
                text = "Available Balance",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ActionButton(iconRes = R.drawable.plus, label = "Add Money") {
                    showAddMoneyDialog = true
                }
                ActionButton(iconRes = R.drawable.money_withdrawal, label = "Withdraw") {
                    showWithdrawDialog = true
                }
            }
        }
    }

    if (showAddMoneyDialog){
        Dialog(onDismissRequest = {showAddMoneyDialog = false}) {
            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add Money",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Enter Amount" , color = MaterialTheme.colorScheme.onSurfaceVariant)},
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.Button(
                        onClick = {
                            showAddMoneyDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Confirm", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }

    if (showWithdrawDialog){
        Dialog(onDismissRequest = {showAddMoneyDialog = false}) {
            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Add WithDraw",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Enter Amount" , color = MaterialTheme.colorScheme.onSurfaceVariant)},
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.Button(
                        onClick = {
                            showWithdrawDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Confirm", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButton(iconRes: Int, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(32.dp)
        )
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun TransactionList() {
    Column {
        Text(
            text = "Transactions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Example transaction items
        TransactionItem(
            logoRes = R.drawable.btn_1,
            name = "Dangote",
            company = "Dangote Inc",
            amount = "+N15,000",
            amountColor = Color.Green
        )
        Spacer(modifier = Modifier.height(10.dp))
        TransactionItem(
            logoRes = R.drawable.btn_2,
            name = "Shell",
            company = "Shell Inc",
            amount = "-N10,000",
            amountColor = Color.Red
        )
        // Add more TransactionItem calls as needed
    }
}

@Composable
fun TransactionItem(
    logoRes: Int,
    name: String,
    company: String,
    amount: String,
    amountColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = logoRes),
            contentDescription = name,
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Text(text = company, fontSize = 14.sp, color = Color.Gray)
        }
        Text(
            text = amount,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWalletScreen(){
    val navController = rememberNavController()
    WalletCompose(navController = navController)
}

