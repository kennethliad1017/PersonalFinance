package com.differentshadow.personalfinance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


@Composable
fun TransactionItem(
    title: String, date: String,
    amount: String,
    transactionModifier: Modifier = Modifier
    .fillMaxWidth()
    .shadow(elevation = 4.dp)
    .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(8.dp))
    .padding(horizontal = 12.dp, vertical = 8.dp),) {
    Row (modifier = transactionModifier, verticalAlignment = Alignment.CenterVertically) {
        Column (modifier = Modifier.weight(1.0f, true)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = date, style = MaterialTheme.typography.bodySmall)
        }

        Text(text = amount, style = MaterialTheme.typography.titleLarge)
    }
}