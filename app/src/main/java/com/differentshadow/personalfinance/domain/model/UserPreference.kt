package com.differentshadow.personalfinance.domain.model

import com.differentshadow.personalfinance.UserPreferences.DarkThemeConfigProto
import java.util.Currency
import java.util.Locale

data class UserPreference(
    val theme: DarkThemeConfigProto = DarkThemeConfigProto.SYSTEM_DEFAULT,
    val language: String = "English",
    val currency: String = Currency.getInstance(Locale.getDefault()).currencyCode
)
