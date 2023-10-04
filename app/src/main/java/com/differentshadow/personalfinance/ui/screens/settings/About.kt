package com.differentshadow.personalfinance.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.ui.components.RichText
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "About PersonalFinance",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f, true),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.size(48.dp))
            }
        },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

            Text(
                text = "Version: 1.0.0",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Personal Finance Manager is your all-in-one financial companion. It helps you manage your finances with ease and provides essential tools for tracking expenses, setting budgets, achieving savings goals, and ensuring you never miss a bill payment.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            RichText(
                fullText = "This app uses illustration from Storyset, create by the Freepik Team",
                linkText = mapOf("Storyset" to "https://storyset.com"),
                modifier = Modifier.padding(bottom = 16.dp)
            )

//            Text(
//                text = "Contact Us:",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(bottom = 8.dp),
//                fontWeight = FontWeight.Bold
//            )
//
//            Text(
//                text = "If you have any questions, feedback, or need assistance, please don't hesitate to reach out to us at [Contact Email]. We're here to help!",
//                style = MaterialTheme.typography.bodyLarge,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun AboutScreenPreview() {
    PersonalFinanceTheme {
        Surface(modifier = Modifier.fillMaxSize(1.0f)) {
            AboutScreen(onNavigateBack = { /*TODO*/ })
        }
    }
}