package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.utils.ValidationResult
import com.differentshadow.personalfinance.domain.form.BudgetForm
import com.differentshadow.personalfinance.domain.form.BudgetFormInputEvent
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.domain.usecase.FormatStringToDateUseCase
import com.differentshadow.personalfinance.domain.model.BudgetUiState
import com.differentshadow.personalfinance.domain.model.DonutChartData
import com.differentshadow.personalfinance.domain.usecase.budget.CreateBudgetUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.DeleteBudgetUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.GetBudgetByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.GetBudgetsUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.RemoveBudgetUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.UpdateBudgetUseCase
import com.differentshadow.personalfinance.utils.BudgetResetFreqs
import com.differentshadow.personalfinance.utils.Categories
import com.differentshadow.personalfinance.utils.CategoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val getBudgetByIdUseCase: GetBudgetByIdUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val updateBudgetUseCase: UpdateBudgetUseCase,
    private val formatStringToDateUseCase: FormatStringToDateUseCase,
    /** deleteBudgetUseCase is soft-delete the removeBudgetUseCase premanently delete's it */
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val removeBudgetUseCase: RemoveBudgetUseCase
) : ViewModel() {
    private val _uiState = MutableLiveData(BudgetUiState())
    val uiState: LiveData<BudgetUiState> = _uiState

    private val _donutChartState = MutableStateFlow<List<DonutChartData>>(listOf())
    val donutChartState: Flow<List<DonutChartData>> = _donutChartState.asStateFlow()


    private val _formState = MutableStateFlow(BudgetForm())
    val formState: Flow<BudgetForm> = _formState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isFetching = true)
            getBudgetsUseCase.invoke(false).collect { budgets ->
                _uiState.postValue(_uiState.value?.copy(budgets = budgets, isFetching = false))

                val donutData = budgets.map { budget ->
                    when (budget.category) {
                        Categories.Dining.title -> {
                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Dining.color,
                                category = budget.category
                            )
                        }

                        Categories.Transportation.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Transportation.color,
                                category = budget.category
                            )
                        }

                        Categories.Utility.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Utility.color,
                                category = budget.category
                            )

                        }

                        Categories.RentMortgage.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.RentMortgage.color,
                                category = budget.category
                            )

                        }

                        Categories.Entertainment.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Entertainment.color,
                                category = budget.category
                            )
                        }

                        Categories.Health.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Health.color,
                                category = budget.category
                            )

                        }

                        Categories.Fitness.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Fitness.color,
                                category = budget.category
                            )

                        }

                        Categories.Clothing.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Clothing.color,
                                category = budget.category
                            )

                        }

                        Categories.PersonalCare.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.PersonalCare.color,
                                category = budget.category
                            )

                        }

                        Categories.Education.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Education.color,
                                category = budget.category
                            )

                        }

                        Categories.Travel.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Travel.color,
                                category = budget.category
                            )

                        }

                        Categories.GiftsandDonations.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.GiftsandDonations.color,
                                category = budget.category
                            )

                        }

                        Categories.HomeMaintenance.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.HomeMaintenance.color,
                                category = budget.category
                            )

                        }

                        Categories.Insurance.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Insurance.color,
                                category = budget.category
                            )

                        }

                        Categories.SavingsandInvestments.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.SavingsandInvestments.color,
                                category = budget.category
                            )

                        }

                        Categories.Miscellaneous.title -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Miscellaneous.color,
                                category = budget.category
                            )

                        }

                        else -> {

                            DonutChartData(
                                amount = budget.budgetLimit.toFloat(),
                                color = Categories.Grocery.color,
                                category = budget.category
                            )

                        }
                    }
                }

                _donutChartState.value = donutData
            }
        }
    }

    fun onInputChanged(event: BudgetFormInputEvent) {
        when (event) {
            is BudgetFormInputEvent.onCategoryChanged -> {
                _formState.update {
                    it.copy(
                        category = event.category
                    )
                }

            }

            is BudgetFormInputEvent.onBudgetLimitChanged -> {

                val budgetLimit = event.budgetLimit.replace(",", "").replace(" ", "").trim()
                _formState.update {
                    it.copy(
                        budgetLimit = budgetLimit
                    )
                }
            }

            is BudgetFormInputEvent.onResetFrequentChanged -> {
                _formState.update {
                    it.copy(
                        resetFrequent = event.resetPeriod
                    )
                }
            }

            is BudgetFormInputEvent.onExpenseChanged -> {
                _formState.update {
                    it.copy(
                        currentExpense = event.expense
                    )
                }
            }

            is BudgetFormInputEvent.onSubmit -> {
                createBudget()
                _formState.update {
                    BudgetForm()
                }
            }

            is BudgetFormInputEvent.onUpdate -> {
                updateExpenseBudget()
                _formState.update {
                    BudgetForm()
                }
            }
        }
    }

    fun getBudget(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isFetching = true)
            withContext(Dispatchers.IO) {
                val budgetResult = getBudgetByIdUseCase.invoke(id)

                budgetResult.onSuccess {budgetEntity ->
                    _uiState.postValue(_uiState.value?.copy(budgetEntity = budgetEntity, isFetching = false))
                    _formState.update {
                        BudgetForm(
                            category = budgetEntity.category,
                            currentExpense = budgetEntity.currentExpense.toString(),
                            budgetLimit = budgetEntity.budgetLimit.toString(),
                            resetFrequent = budgetEntity.resetFrequency
                        )
                    }
                }

                budgetResult.onFailure {
                    _uiState.postValue(
                        _uiState.value?.copy(
                            errorMessage = it.message,
                            isFetching = false
                        )
                    )
                    _formState.update {
                        BudgetForm()
                    }
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    private fun validateCategory(): ValidationResult {
        if (_formState.value.category.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Category is required"
            )
        }

        if (!CategoryList.contains(_formState.value.category)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Category is invalid"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateCurrentExpense(): ValidationResult {
        val expenseFormatted = _formState.value.currentExpense.toDouble()
        if (expenseFormatted.isNaN() || expenseFormatted == _uiState.value?.budgetEntity?.currentExpense) {
            return ValidationResult(
                successful = false,
                errorMessage = "Current Expense is invalid"
            )
        }

        return ValidationResult(
            successful = true
        )
    }


    private fun validateBudgetLimit(): ValidationResult {
        if (_formState.value.budgetLimit.isEmpty()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Budget Limit is required"
            )
        }

        if (_formState.value.budgetLimit.toDouble() == 0.0) {
            return ValidationResult(
                successful = false,
                errorMessage = "Budget Limit should not be zero"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateResetFreq(): ValidationResult {
        if (_formState.value.resetFrequent.isEmpty() || !BudgetResetFreqs.contains(_formState.value.resetFrequent)) {

            return ValidationResult(
                successful = false,
                errorMessage = "Reset Period is required"
            )
        }

        return ValidationResult(
            successful = true
        )
    }


    private fun createBudget() {
        val categoryResult = validateCategory()
        val budgetLimitResult = validateBudgetLimit()
        val resetFreqResult = validateResetFreq()

        val hasError = listOf(
            categoryResult,
            budgetLimitResult,
            resetFreqResult
        ).any { !it.successful }

        if (hasError) {
            _formState.update {
                it.copy(
                    categoryErrorMsg = categoryResult.errorMessage,
                    budgetLimitErrorMsg = budgetLimitResult.errorMessage,
                    resetFreqErrorMsg = resetFreqResult.errorMessage
                )
            }

            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            createBudgetUseCase.invoke(
                category = _formState.value.category,
                currentExpense = 0.0,
                budgetLimit = _formState.value.budgetLimit.toDouble(),
                resetFrequency = _formState.value.resetFrequent
            )
        }
    }

    private fun updateExpenseBudget() {
        val categoryResult = validateCategory()
        val budgetLimitResult = validateBudgetLimit()
        val resetFreqResult = validateResetFreq()
        val expenseResult = validateCurrentExpense()

        val hasError = listOf(
            categoryResult,
            expenseResult,
            budgetLimitResult,
            resetFreqResult
        ).any { !it.successful }

        if (hasError) {
            _formState.update {
                it.copy(
                    categoryErrorMsg = categoryResult.errorMessage,
                    budgetLimitErrorMsg = budgetLimitResult.errorMessage,
                    resetFreqErrorMsg = resetFreqResult.errorMessage,
                    currentExpenseErrorMsg = expenseResult.errorMessage,
                )
            }

            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            createBudgetUseCase.invoke(
                category = _formState.value.category,
                currentExpense = _formState.value.currentExpense.toDouble(),
                budgetLimit = _formState.value.budgetLimit.toDouble(),
                resetFrequency = _formState.value.resetFrequent
            )
        }
    }
}