package com.differentshadow.personalfinance.ui.viewmodel

import android.util.Log
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.utils.CalendarDataSource
import com.differentshadow.personalfinance.utils.CalendarUiModel
import com.differentshadow.personalfinance.UserPreferences.DarkThemeConfigProto
import com.differentshadow.personalfinance.data.repository.userpreference.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(
    private val userPrefsRepo: UserPreferencesRepository,
): ViewModel() {
    private val _dataSource = CalendarDataSource()
    private val _selectedDate = MutableLiveData(_dataSource.today)
    val selectedDate: LiveData<LocalDate> = _selectedDate

    // get CalendarUiModel from CalendarDataSource, and the lastSelectedDate is Today.
    private val _calendarUiModel = MutableLiveData(_dataSource.getData(lastSelectedDate = _dataSource.today))
    val calendarUiModel: LiveData<CalendarUiModel> = _calendarUiModel

    private val _preferCurrency = MutableStateFlow(
        NumberFormat.getCurrencyInstance(
        Locale.getDefault()).currency)
    val preferCurrency: Flow<Currency> = _preferCurrency.asStateFlow()

    private val _preferLanguage = MutableStateFlow("English")
    val preferLanguage: Flow<String>  = _preferLanguage.asStateFlow()

    private val _preferTheme = MutableStateFlow(DarkThemeConfigProto.SYSTEM_DEFAULT)
    val preferTheme: Flow<DarkThemeConfigProto> = _preferTheme.asStateFlow()


    init {
        // Restore the selected currency and language from saved state if available
        viewModelScope.launch {
            userPrefsRepo.userPreferencesFlow.collect {userPrefs ->
                val currency = Currency.getInstance(userPrefs.preferCurrency)
                _preferCurrency.update {
                    currency
                }

                _preferLanguage.update {
                    userPrefs.preferLanguage
                }

                _preferTheme.update {
                    userPrefs.darkThemeConfig
                }
            }
        }
    }

    fun onCurrencyChanged(currency: Currency) {
        _preferCurrency.update {
            currency
        }
        viewModelScope.launch {
            userPrefsRepo.setPreferCurrency(currency.currencyCode)
        }
    }

    fun onLanguageChanged(language: String) {
        _preferLanguage.update {
            language
        }
        viewModelScope.launch {
            userPrefsRepo.setPreferLanguage(language)
        }
    }

    fun onThemeChanged(themeMode: DarkThemeConfigProto) {
        _preferTheme.update {
            themeMode
        }
        viewModelScope.launch {
            userPrefsRepo.setPreferTheme(themeMode)
        }
    }

    fun onSelectedDateChanged(date: CalendarUiModel.Date) {
        _selectedDate.value = date.date
        // refresh the CalendarUiModel with new data
        // by changing only the `selectedDate` with the date selected by User
        _calendarUiModel.postValue(_calendarUiModel.value?.visibleDates?.let {
            _calendarUiModel.value?.copy(
                selectedDate = date,
                visibleDates = it.map {dateModel ->
                    dateModel.copy(
                        isSelected = dateModel.date.isEqual(date.date)
                    )
                }
            )
        })
    }

    fun getData(): CalendarUiModel {
        return _dataSource.getData(lastSelectedDate = _dataSource.today)
    }

    fun onNextDate() {
        // refresh the CalendarUiModel with new data
        // by get data with new Start Date (which is the endDate+2 from the visibleDates)
        _calendarUiModel.value?.let {
            val finalStartDate = it.endDate.date.plusDays(2)
            _calendarUiModel.value = _dataSource.getData(startDate = finalStartDate, lastSelectedDate = it.selectedDate.date)
        }
    }



    fun onPrevDate() {
        // refresh the CalendarUiModel with new data
        // by get data with new Start Date (which is the startDate-1 from the visibleDates)
        _calendarUiModel.value?.let {
            val finalStartDate = it.startDate.date.minusDays(1)
            _calendarUiModel.value = _dataSource.getData(startDate = finalStartDate, lastSelectedDate = it.selectedDate.date)
        }
    }
}