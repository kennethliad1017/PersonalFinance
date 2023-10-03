package com.differentshadow.personalfinance.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.domain.model.HomeUiState
import com.differentshadow.personalfinance.domain.usecase.savinggoal.GetGoalsOverviewUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetCurrentExpensesUseCase
import com.differentshadow.personalfinance.utils.toCurrency
import com.differentshadow.personalfinance.utils.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTotalExpensesUseCase: GetCurrentExpensesUseCase,
    private val getGoalsOverviewUseCase: GetGoalsOverviewUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: Flow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isFetching = false
                )
            }
            getGoalsOverviewUseCase.invoke().collect {goals ->
                _uiState.update {
                    it.copy(
                        overviewGoals = goals, isFetching = false
                    )
                }
            }
        }

        onSelectedDateChanged()
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        onSelectedDateChanged()
    }

    private fun onSelectedDateChanged() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isFetching = false
                )
            }
            val total = getTotalExpensesUseCase.invoke(LocalDateTime.of(_selectedDate.value, LocalTime.MIN).toLong(), LocalDateTime.of(_selectedDate.value, LocalTime.MAX).toLong()).first()

            Log.i("Home", total.toCurrency())

            _uiState.update {
                it.copy(
                    currentExpenses = BigDecimal(total), isFetching = false
                )
            }
        }
    }
}