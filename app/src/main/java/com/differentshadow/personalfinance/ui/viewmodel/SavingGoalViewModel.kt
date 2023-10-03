package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.utils.ValidationResult
import com.differentshadow.personalfinance.domain.form.GoalFormInputEvent
import com.differentshadow.personalfinance.domain.form.SavingGoalForm
import com.differentshadow.personalfinance.domain.form.TransactionForm
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.domain.model.SavingGoalUIState
import com.differentshadow.personalfinance.domain.usecase.savinggoal.CreateGoalUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.DeleteGoalUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.GetGoalsUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.RemoveGoalUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.UpdateGoalUseCase
import com.differentshadow.personalfinance.utils.CategoryList
import com.differentshadow.personalfinance.utils.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SavingGoalViewModel @Inject constructor(
    private val createGoalUseCase: CreateGoalUseCase,
    private val getSavingGoalsUseCase: GetGoalsUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val removeGoalUseCase: RemoveGoalUseCase,
): ViewModel()  {

    private val _uiState = MutableLiveData(SavingGoalUIState())
    val uiState: LiveData<SavingGoalUIState> = _uiState

    private val _formState = MutableStateFlow(SavingGoalForm())
    val formState: Flow<SavingGoalForm> = _formState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isFetching = true)
            getSavingGoalsUseCase.invoke(false).collect {

                _uiState.postValue(_uiState.value?.copy(savingGoals = it, isFetching = false))
            }

        }
    }

    fun onInputChange(eventType: GoalFormInputEvent) {
        when(eventType) {
            is GoalFormInputEvent.onTitleChanged -> {
                _formState.update {
                    it.copy(
                        title = eventType.title
                    )
                }
            }
            is GoalFormInputEvent.onCategoryChanged -> {
                _formState.update {
                    it.copy(
                        category = eventType.category
                    )
                }
            }
            is GoalFormInputEvent.onGoalAmountChanged -> {
                _formState.update {
                    it.copy(
                        goalAmount = eventType.goalAmount
                    )
                }

            }
            is GoalFormInputEvent.onGoalDateChanged -> {
                _formState.update {
                    it.copy(
                        goalDate = eventType.date
                    )
                }

            }
            is GoalFormInputEvent.onSubmit -> {
                onSubmit()
                _formState.update {
                    SavingGoalForm()
                }
            }
        }
    }

    fun selectedGoal(goalId: Long)  {
        _uiState.value?.let {goalState ->
            val goal = goalState.savingGoals.filter {  it.id == goalId }[0]

            goalState.copy(savingGoal = goal)
        }
    }


    private fun validateTitle(): ValidationResult {
        if (_formState.value.title.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Title is required"
            )
        }

        return ValidationResult(
            successful = true
        )
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

    private fun validateGoalAmount(): ValidationResult {
        val goalAmountFormatted = _formState.value.goalAmount.replace(",", "").replace(" ", "").trim()
        if(goalAmountFormatted.isEmpty() || goalAmountFormatted.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Goal Amount is invalid"
            )
        }

        if (goalAmountFormatted.toDouble() == 0.0 || goalAmountFormatted.toDouble().isNaN()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Goal Amount is required"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateGoalDate(): ValidationResult {
        if (_formState.value.goalDate == 0L) {
            return ValidationResult(
                successful = false,
                errorMessage = "Goal date is invalid"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun onSubmit() {
        val titleResult = validateTitle()
        val categoryResult = validateCategory()
        val goalAmountResult = validateGoalAmount()
        val goalDateResult = validateGoalDate()

        val hasError = listOf(
            titleResult,
            categoryResult,
            goalAmountResult,
            goalDateResult
        ).any { !it.successful }

        if (hasError) {
            _formState.update {
                it.copy(
                    titleErrorMsg = titleResult.errorMessage,
                    categoryErrorMsg = categoryResult.errorMessage,
                    goalAmountErrorMsg = goalAmountResult.errorMessage,
                    goalDateErrorMsg = goalDateResult.errorMessage,
                )
            }

            return
        }

        viewModelScope.launch {
            createGoalUseCase.invoke(
                title = _formState.value.title,
                category = _formState.value.category,
                goalAmount = _formState.value.goalAmount.toDouble(),
                goalDate = _formState.value.goalDate
            )
            _formState.update {
                SavingGoalForm()
            }
            validationEventChannel.send(ValidationEvent.Success)
        }
    }
}