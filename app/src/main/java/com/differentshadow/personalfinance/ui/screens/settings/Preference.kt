package com.differentshadow.personalfinance.ui.screens.settings

import androidx.appcompat.app.AppCompatDelegate
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.differentshadow.personalfinance.R
import com.differentshadow.personalfinance.UserPreferences.DarkThemeConfigProto
import com.differentshadow.personalfinance.utils.currencyList
import java.util.Currency
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    preferCurrency: Currency,
    onNavigateBack: () -> Unit,
    onCurrencyChanged: (value: Currency) -> Unit,
) {

    val dropdownCurrency = remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp, top = 24.dp, bottom = 24.dp)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.currency), style = MaterialTheme.typography.labelLarge)

                ExposedDropdownMenuBox(
                    expanded = dropdownCurrency.value,
                    onExpandedChange = {
                        dropdownCurrency.value = !dropdownCurrency.value
                    }
                ) {
                    OutlinedTextField(
                        value = preferCurrency.currencyCode,
                        onValueChange = {},
                        enabled = false,
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownCurrency.value) },
                        modifier = Modifier
                            .height(50.dp)
                            .width(112.dp)
                            .menuAnchor(),
                        textStyle = MaterialTheme.typography.labelLarge,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            disabledIndicatorColor = MaterialTheme.colorScheme.onBackground,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            disabledTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownCurrency.value,
                        onDismissRequest = { dropdownCurrency.value = false }
                    ) {
                        currencyList.forEach { currency ->
                            DropdownMenuItem(
                                text = {
                                       Text(text = currency.symbol)
                                },
                                trailingIcon = {
                                    if (currency.displayName == preferCurrency.displayName) {
                                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                                    }
                                },
                                onClick = {
                                    onCurrencyChanged(currency)
                                    dropdownCurrency.value = false
                                }
                            )
                        }
                    }
                }

            }
            Spacer(Modifier.height(8.dp))
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onCurrencyChanged(Currency.getInstance(Locale.getDefault())) }) {
                Text(text = stringResource(R.string.reset_default))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    preferLanguage: String,
    onNavigateBack: () -> Unit,
    onLanguageChanged: (value: String) -> Unit,
) {
    val dropdownLanguage = remember {
        mutableStateOf(false)
    }


    val supportedLanguages = mapOf(
        R.string.en to "en_US",
        R.string.de to "de_DE",
        R.string.es to "es_ES",
        R.string.ja to "ja_JP",
        R.string.ko to "ko_KR"
    ).mapKeys { stringResource(it.key) }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        },
        contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp, top = 24.dp, bottom = 24.dp)
    ) {contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.language), style = MaterialTheme.typography.labelLarge)

                ExposedDropdownMenuBox(
                    expanded = dropdownLanguage.value,
                    onExpandedChange = {
                        dropdownLanguage.value = !dropdownLanguage.value
                    }
                ) {
                    OutlinedTextField(
                        value = preferLanguage,
                        onValueChange = {},
                        enabled = false,
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownLanguage.value) },
                        modifier = Modifier
                            .height(50.dp)
                            .width(136.dp)
                            .menuAnchor(),
                        textStyle = MaterialTheme.typography.labelLarge,
                        shape = RoundedCornerShape(8.dp),
                        colors = TextFieldDefaults.colors(
                            disabledIndicatorColor = MaterialTheme.colorScheme.onBackground,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            disabledTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = dropdownLanguage.value,
                        onDismissRequest = { dropdownLanguage.value = false }
                    ) {
                        supportedLanguages.keys.forEach {selectionLocale ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = selectionLocale)
                                },
                                trailingIcon = {
                                    if (selectionLocale == preferLanguage) {
                                        Icon(imageVector = Icons.Rounded.Check, contentDescription = null)
                                    }
                                },
                                onClick = {
                                    onLanguageChanged(selectionLocale)
                                    dropdownLanguage.value = false
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}


@Composable
fun PreferThemeScreen(
    themeMode: DarkThemeConfigProto,
    onNavigateBack: () -> Unit,
    onThemeChanged: (theme: DarkThemeConfigProto) -> Unit
) {

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { onNavigateBack() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.chevron_left),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

            }
        },
        contentWindowInsets = WindowInsets(16.dp, 24.dp, 16.dp, 24.dp)
    ) {contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {

           Row(
               modifier = Modifier.fillMaxWidth(),
               verticalAlignment = Alignment.CenterVertically,
           ) {
               RadioButton(selected = themeMode == DarkThemeConfigProto.LIGHT, onClick = { onThemeChanged(DarkThemeConfigProto.LIGHT) })
               Spacer(Modifier.width(8.dp))
               Text(text = "Light")
           }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = themeMode == DarkThemeConfigProto.DARK, onClick = { onThemeChanged(DarkThemeConfigProto.DARK) })
                Spacer(Modifier.width(8.dp))
                Text(text = "Dark")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = themeMode == DarkThemeConfigProto.SYSTEM_DEFAULT || themeMode == DarkThemeConfigProto.UNRECOGNIZED, onClick = { onThemeChanged(DarkThemeConfigProto.SYSTEM_DEFAULT) })
                Spacer(Modifier.width(8.dp))
                Text(text = "System Default")
            }
            Text(text = "If system default is selected. PersonalFinance will automatically adjust your appearance based on your device's system settings", style = MaterialTheme.typography.labelMedium)
        }
    }
}