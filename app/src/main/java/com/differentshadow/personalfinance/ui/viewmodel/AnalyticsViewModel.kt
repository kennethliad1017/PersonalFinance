package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.domain.model.AnalyticsUIState
import com.differentshadow.personalfinance.domain.model.Expense
import com.differentshadow.personalfinance.domain.usecase.transaction.GetLatestTransactionsUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionByIntervalUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionsUseCase
import com.differentshadow.personalfinance.utils.CalendarDataSource
import com.differentshadow.personalfinance.utils.TimeFrame
import com.differentshadow.personalfinance.utils.getDayWeekStart
import com.differentshadow.personalfinance.utils.getFirstDayOfMonth
import com.differentshadow.personalfinance.utils.getFirstDayOfYear
import com.differentshadow.personalfinance.utils.getLastDayOfMonth
import com.differentshadow.personalfinance.utils.getLastDayOfYear
import com.differentshadow.personalfinance.utils.toDateString
import com.differentshadow.personalfinance.utils.toDateStringList
import com.differentshadow.personalfinance.utils.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getTransactionByIntervalUseCase: GetTransactionByIntervalUseCase,
    private val getTransactionsUseCase: GetLatestTransactionsUseCase
) : ViewModel() {
    // TODO: Fetch expenses per category and show the percentage of each category's expenses
    // 1. Retrieve expenses grouped by category and calculate the total expense for each category.
    // 2. Calculate the total sum of all expenses to determine the percentage.
    // 3. Calculate the percentage of expenses for each category and display it in your UI.
    private val _uiState = MutableStateFlow(AnalyticsUIState())
    val uiState: Flow<AnalyticsUIState> = _uiState.asStateFlow()

    private val _analyticsUIState = MutableStateFlow<List<Expense>>(listOf())
    val analyticsUIState: Flow<List<Expense>> = _analyticsUIState.asStateFlow()


    private val _selectedTimeFrame = MutableStateFlow(TimeFrame.WEEKLY)
    val selectedTimeFrame: Flow<TimeFrame> = _selectedTimeFrame.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: Flow<LocalDate> = _selectedDate.asStateFlow()

    private val _dateRange = MutableStateFlow<Pair<LocalDate, LocalDate>>(
        _selectedDate.value.getDayWeekStart(DayOfWeek.SUNDAY) to _selectedDate.value.getDayWeekStart(
            DayOfWeek.SUNDAY
        ).plusDays(6)
    )

    val dateRange: Flow<Pair<LocalDate, LocalDate>> = _dateRange.asStateFlow()


    init {
        viewModelScope.launch {
            val (startDate, endDate) = _dateRange.value
            _uiState.update {
                it.copy(isFetching = true)
            }


            getTransactionsUseCase.invoke(beenDeleted = false).collect {transactions ->
                _uiState.update {
                    it.copy(analytics = transactions, isFetching = false)
                }
            }

            getTransactionByIntervalUseCase.invoke(
                startDate.toLong(),
                endDate.toLong()
            ).collect { transactions ->

                _uiState.update {
                    it.copy(analytics = transactions, isFetching = false)
                }

                transactions.forEach {transaction ->
                    _analyticsUIState.update {
                        it.plus(
                            Expense(
                                date = transaction.date.toLong("EEE d, MMM yyyy"),
                                amount = BigDecimal(transaction.total)
                            )
                        )
                    }
                }
            }
        }
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.update {
            date
        }
        onTimeFrameChanged()
    }


    fun setSelectedTimeFrame(timeFrame: TimeFrame) {
        _selectedTimeFrame.update {
            timeFrame
        }
        onTimeFrameChanged()
    }

    // change current date based on time frame

    private fun onTimeFrameChanged() {
        _dateRange.update {
            when (_selectedTimeFrame.value) {
                TimeFrame.WEEKLY -> {
                    val startOfWeek = _selectedDate.value.getDayWeekStart(DayOfWeek.SUNDAY)
                    val endOfWeek = startOfWeek.plusDays(6)
                    startOfWeek to endOfWeek
                }

                TimeFrame.MONTHLY -> {
                    val startOfMonth = _selectedDate.value.getFirstDayOfMonth().minusMonths(3)
                    val endOfMonth = _selectedDate.value.getLastDayOfMonth().plusMonths(3)
                    startOfMonth to endOfMonth
                }

                TimeFrame.ANNUALLY -> {
                    val startOfYear = _selectedDate.value.getFirstDayOfYear().minusYears(3)
                    val endOfYear = _selectedDate.value.getLastDayOfMonth().plusYears(3)
                    startOfYear to endOfYear
                }

                TimeFrame.ALL -> {
                    val started = _selectedDate.value.getFirstDayOfYear().minusYears(6)
                    val ended = _selectedDate.value
                    started to ended
                }
            }
        }
    }

    fun onNextDateRange() {
        _dateRange.update {
            when (_selectedTimeFrame.value) {
                TimeFrame.WEEKLY -> {
                    _selectedDate.update {
                        _selectedDate.value.getDayWeekStart(DayOfWeek.SUNDAY).plusDays(10)
                    }
                    val startOfWeek =
                        _selectedDate.value.getDayWeekStart(DayOfWeek.SUNDAY)
                    val endOfWeek = startOfWeek.plusDays(6)
                    startOfWeek to endOfWeek
                }

                TimeFrame.MONTHLY -> {
                    _selectedDate.update {
                        _selectedDate.value.getFirstDayOfMonth().plusMonths(7)
                    }
                    val startOfMonth = _selectedDate.value.getFirstDayOfMonth().minusMonths(3)
                    val endOfMonth = _selectedDate.value.getLastDayOfMonth().plusMonths(3)
                    startOfMonth to endOfMonth
                }

                TimeFrame.ANNUALLY -> {
                    _selectedDate.update {
                        _selectedDate.value.getFirstDayOfYear().plusYears(7)
                    }
                    val startOfYear = _selectedDate.value.getFirstDayOfYear().minusYears(3)
                    val endOfYear =_selectedDate.value.getLastDayOfYear().plusYears(3)
                    startOfYear to endOfYear
                }

                TimeFrame.ALL -> {
                    val started = _selectedDate.value.getFirstDayOfYear().minusYears(6)
                    val ended = _selectedDate.value
                    started to ended
                }
            }
        }
    }

    fun onPrevDateRange() {
        _dateRange.update {
            when (_selectedTimeFrame.value) {
                TimeFrame.WEEKLY -> {
                    _selectedDate.update {
                        _selectedDate.value.minusDays(7)
                    }
                    val startOfWeek =
                        _selectedDate.value.getDayWeekStart(DayOfWeek.SUNDAY)
                    val endOfWeek = startOfWeek.plusDays(6)
                    startOfWeek to endOfWeek
                }

                TimeFrame.MONTHLY -> {
                    _selectedDate.update {
                        _selectedDate.value.getFirstDayOfMonth().minusMonths(7)
                    }

                    val startOfMonth = _selectedDate.value.getFirstDayOfMonth().minusMonths(3)
                    val endOfMonth = _selectedDate.value.getLastDayOfMonth().plusMonths(3)
                    startOfMonth to endOfMonth
                }

                TimeFrame.ANNUALLY -> {
                    _selectedDate.update {
                        _selectedDate.value.getFirstDayOfYear().minusYears(7)
                    }
                    val startOfYear = _selectedDate.value.getFirstDayOfYear().minusYears(3)
                    val endOfYear = _selectedDate.value.getLastDayOfYear().plusYears(3)
                    startOfYear to endOfYear
                }

                TimeFrame.ALL -> {
                    val started = _selectedDate.value.getFirstDayOfYear().minusYears(6)
                    val ended = _selectedDate.value
                    started to ended
                }
            }
        }
    }


}