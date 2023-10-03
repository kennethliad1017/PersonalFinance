package com.differentshadow.personalfinance.ui.screens.tabs

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.ui.components.FlatList
import com.differentshadow.personalfinance.ui.components.items
import com.differentshadow.personalfinance.ui.theme.PersonalFinanceTheme
import com.differentshadow.personalfinance.ui.theme.PoppinsFamily

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = LocalTextStyle.current,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, style = style, fontFamily = fontFamily, fontWeight = fontWeight)
    }
}

@Composable
fun SettingScreen(
    onNavigate: (dest: String) -> Unit,
    tabNavigate: (dest: String) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val tabItemWidth = configuration.screenWidthDp / 5f


    val settings: Map<String, List<Pair<String, String>>> = mapOf(
        stringResource(R.string.content) to listOf(
            Pair(
                stringResource(R.string.bill_reminders),
                "billreminder"
            ),
            Pair(
                stringResource(R.string.transaction_history),
                "transaction_history"
            ),
        ),
        stringResource(R.string.preference) to listOf(
            Pair(
                stringResource(R.string.dark_mode),
                "theme"
            ),
            Pair(
                stringResource(R.string.currency),
                "currency"
            ),
            Pair(
                stringResource(R.string.language),
                "language"
            )
        ),
        stringResource(R.string.help_security) to listOf(
            Pair(
                stringResource(R.string.term_of_service),
                "terms-of-service"
            ),
            Pair(
                stringResource(R.string.privacy_policy),
                "privacy-policy"
            ),
            Pair(
                stringResource(R.string.about),
                "about"
            )
        ),
    )

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding(),
        bottomBar = {
            BottomAppBar {
                items.forEachIndexed { index, screen ->
                    NavigationBarItem(
                        selected = index == items.size - 1,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        onClick = { tabNavigate(screen.route) },
                        icon = {
                            Icon(
                                painterResource(id = screen.icon),
                                contentDescription = "Localized description",
                            )
                        })
                }
            }
        },

        ) {
        FlatList(modifier = Modifier.padding(it)) {
            settings.forEach { (headerTitle, items) ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = headerTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                items(items, key = { index -> index }) { (title, route) ->
                    ListItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clickable {
                                onNavigate(route)
                            }
                            .padding(
                                horizontal = 24.dp,
                                vertical = 8.dp
                            ),
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = PoppinsFamily
                    )
                }

                item  {
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro", apiLevel = 33)
@Composable
fun SettingsPreview() {
    PersonalFinanceTheme {
        Surface {
            SettingScreen(onNavigate = { /*TODO*/ }, tabNavigate = { /*TODO*/ })
        }
    }
}