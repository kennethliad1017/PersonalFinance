package com.differentshadow.personalfinance.data.repository.userpreference

import com.differentshadow.personalfinance.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepositoryBase {
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun setPreferCurrency(currencyCode: String)

    suspend fun setPreferLanguage(language: String)

    suspend fun setPreferTheme(themeMode: UserPreferences.DarkThemeConfigProto)
}