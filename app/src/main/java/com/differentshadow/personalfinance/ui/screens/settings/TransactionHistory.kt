package com.differentshadow.personalfinance.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.model.TransactionsUIState
import com.differentshadow.personalfinance.ui.components.FlatList
import com.differentshadow.personalfinance.utils.toCurrency
import java.util.Currency

@Composable
fun TransactionHistory(
    preferCurrency: Currency,
    uiState: TransactionsUIState,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: String) -> Unit
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp - 32

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = null
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp)
    ) { contentPadding ->
        if (uiState.transactions.isEmpty() || uiState.transactions.values.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(screenWidth.dp),
                    painter = painterResource(id = R.drawable.e_wallet_pana),
                    contentDescription = "Saving Goal"
                )

                Text(text = stringResource(R.string.you_don_t_have_transaction_added_at_the_moment), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
                Text(
                    text = "Add Transaction",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateTo("expense_form") })

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            FlatList {
                uiState.transactions.forEach { (headerTitle, items) ->
                    item {
                        Text(text = headerTitle, style = MaterialTheme.typography.labelLarge)
                    }

                    items(items, key = { it.id }) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f, true)
                                ) {
                                    Text(
                                        text = item.merchantName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = item.merchantName,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }

                                Text(
                                    text = item.total.toCurrency(preferCurrency),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}