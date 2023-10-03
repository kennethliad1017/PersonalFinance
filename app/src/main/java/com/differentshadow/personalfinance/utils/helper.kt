package com.differentshadow.personalfinance.utils

import android.content.Context
import android.os.Build
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.LocaleList
import com.differentshadow.personalfinance.R
import java.util.Calendar
import java.util.Currency
import java.util.Locale

fun dateAndTime(dateMillis: Long, timeMillis: Long): Long {
    val dateCalendar = Calendar.getInstance()
    dateCalendar.timeInMillis = dateMillis

    val timeCalendar = Calendar.getInstance()
    timeCalendar.timeInMillis = timeMillis

    // Extract time components
    val hour = timeCalendar.get(Calendar.HOUR_OF_DAY)
    val minute = timeCalendar.get(Calendar.MINUTE)
    val second = timeCalendar.get(Calendar.SECOND)

    // Apply time components to the date
    dateCalendar.set(Calendar.HOUR_OF_DAY, hour)
    dateCalendar.set(Calendar.MINUTE, minute)
    dateCalendar.set(Calendar.SECOND, second)

    return dateCalendar.timeInMillis
}



data class ValidationResult (
    val successful: Boolean,
    val errorMessage: String? = null,
)

val currencyList = Currency.getAvailableCurrencies()

fun getSupportedLanguages(context: Context): List<Locale> {
    val resources = context.resources
    // Retrieve the available locales
    val availableLocales: List<Locale> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val size = resources.configuration.locales.size()
        val localList: List<Locale> = listOf()
        for (index in 1..size) {
            localList.plus(
                resources.configuration.locales[index])
        }
        localList
    } else {
        @Suppress("DEPRECATION")
        listOf(resources.configuration.locale)
    }

    // Filter the locales to include only supported languages
    val supportedLanguages = availableLocales.filter { locale ->
        // Replace with your specific criteria for supported languages
        // For example, check if it's one of your supported languages
        listOf("en", "de", "es", "ja", "ko").contains(locale.language)
    }

    return supportedLanguages
}