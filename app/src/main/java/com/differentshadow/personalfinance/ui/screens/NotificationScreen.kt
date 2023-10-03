package com.differentshadow.personalfinance.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.domain.model.NotificationUIState
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily


@Composable
fun Navbar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(48.dp)) {
            Icon(painter = painterResource(id = R.drawable.chevron_left), contentDescription = "To navigate back")
        }
        Text(text = title, fontFamily = PoppinsFamily, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.0f, true), textAlign = TextAlign.Center)
        Spacer(Modifier.size(48.dp))
    }
}

@Composable
fun NotificationScreen(
    notificationUiState: NotificationUIState,
    onNavigateBack: () -> Unit,
    onNavigateTo: (route: String) -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp - 32
    Scaffold(
        modifier = Modifier
            .safeDrawingPadding(),
        topBar = {
            Navbar(
                title = stringResource(R.string.notification),
                onNavigateBack = {
                    onNavigateBack()
                }
            )
        },
        contentWindowInsets = WindowInsets(left = 16.dp, top = 24.dp, right = 16.dp, bottom = 24.dp)
    ) {
        if (notificationUiState.billReminderNotify.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(modifier = Modifier.size(screenWidth.dp) ,painter = painterResource(id = R.drawable.push_notifications_pana), contentDescription = "Notification Illustration")
                Spacer(Modifier.height(12.dp))
                Text(text = stringResource(R.string.notification_empty_message), fontFamily = PoppinsFamily, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = { onNavigateTo("billreminder_form") }) {
                    Text(text = stringResource(R.string.add_bill_reminder), style = MaterialTheme.typography.labelLarge)
                }
            }
        } else {
            Column (modifier = Modifier
                .fillMaxSize()
                .padding(it)) {
                Text(text = "Notification Screen")
            }
        }

    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun NotificationPreview() {
    PersonalFinanceTheme {
        Surface {
            NotificationScreen(
                notificationUiState = NotificationUIState(),
                onNavigateBack = { /*TODO*/ }
            )
        }
    }
}