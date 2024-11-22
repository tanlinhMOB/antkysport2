package com.example.antkysport.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun FinancialSummaryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3D6D6)) // Red gradient background
            .padding(16.dp)
    ) {
        // Top Icons and Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
            Text(
                text = "Mine",
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Income and Overview
        Text(
            text = "Income",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "+2,830.69",
            style = MaterialTheme.typography.h3,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Overview €456,295.56",
            style = MaterialTheme.typography.subtitle1,
            color = Color.Black.copy(alpha = 0.8f),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton(text = "Roll out", icon = Icons.Default.ArrowUpward, backgroundColor = Color(0xFF76C7C0))
            ActionButton(text = "Into", icon = Icons.Default.ArrowDownward, backgroundColor = Color(0xFFB39DDB))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Payment Details Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Payment details",
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )

                PaymentDetailItem(
                    title = "NASDAQ-100 After Hours Indicator",
                    amount = "+43.28",
                    cumulative = "129.00",
                    date = "Yesterday",
                    amountColor = Color(0xFF4CAF50)
                )
                PaymentDetailItem(
                    title = "Neos Therapeutics, Inc. (NEOS)",
                    amount = "+12.31",
                    cumulative = "678.00",
                    date = "Yesterday",
                    amountColor = Color(0xFFFFA726)
                )
            }
        }
    }
}

@Composable
fun ActionButton(text: String, icon: ImageVector, backgroundColor: Color) {
    Column(
        modifier = Modifier
            .size(80.dp)
            .background(backgroundColor, shape = CircleShape)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = text, tint = Color.White, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, color = Color.White, style = MaterialTheme.typography.body2, textAlign = TextAlign.Center)
    }
}

@Composable
fun PaymentDetailItem(
    title: String,
    amount: String,
    cumulative: String,
    date: String,
    amountColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
            Text(text = "$date (€)", style = MaterialTheme.typography.caption, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = amount, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold, color = amountColor)
            Text(text = cumulative, style = MaterialTheme.typography.caption, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBill(){
    FinancialSummaryScreen()
}
