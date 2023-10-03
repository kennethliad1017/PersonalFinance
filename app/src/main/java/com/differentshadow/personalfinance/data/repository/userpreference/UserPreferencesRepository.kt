package com.differentshadow.personalfinance.data.repository.userpreference

import android.util.Log
import androidx.datastore.core.DataStore
import com.differentshadow.personalfinance.UserPreferences
import com.differentshadow.personalfinance.UserPreferences.DarkThemeConfigProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val userPreferencesStore: DataStore<UserPreferences>,
): UserPreferencesRepositoryBase {
    private val TAG: String = "UserPreferencesRepo"

    override val userPreferencesFlow: Flow<UserPreferences> = userPreferencesStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Log.e(TAG, "Error reading sort order preferences.", exception)
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun setPreferCurrency(currencyCode: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setPreferCurrency(currencyCode).build()
        }
    }

    override suspend fun setPreferLanguage(language: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setPreferLanguage(language).build()
        }
    }

    override suspend fun setPreferTheme(themeMode: DarkThemeConfigProto) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder().setDarkThemeConfig(themeMode).build()
        }
    }
}