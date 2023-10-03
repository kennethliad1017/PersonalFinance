package com.differentshadow.personalfinance.ui.screens.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.model.BillReminderUIState
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily
import java.time.LocalDateTime

@Composable
fun BillReminderScreen(
    reminderState: BillReminderUIState,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: String) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp - 32

    val currentDate = remember { mutableStateOf(LocalDateTime.now()) }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(44.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }


                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = { onNavigateTo("billreminder_form") }
                ) {
                    Icon(modifier = Modifier.size(24.dp), painter = painterResource(id = R.drawable.plus), contentDescription = null)
                }

            }
        },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp, top = 24.dp, bottom = 24.dp)
    ) {contentPadding ->

        if (reminderState.billReminders.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(modifier = Modifier.size(screenWidth.dp) ,painter = painterResource(id = R.drawable.calendar_rafiki), contentDescription = "Bill Reminder Empty Illustration")
                Spacer(Modifier.height(8.dp))
                Text(text = stringResource(R.string.no_bill_reminder_at_the_moment), fontFamily = PoppinsFamily, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            items(reminderState.billReminders, key = {it.id}) {item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = item.title)
                    Text(text = item.dueDate)
                }
            }
        }


    }
}