package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.utils.ValidationResult
import com.differentshadow.personalfinance.utils.dateAndTime
import com.differentshadow.personalfinance.domain.form.BillReminderForm
import com.differentshadow.personalfinance.domain.form.BillReminderInputEvent
import com.differentshadow.personalfinance.domain.form.ValidationEvent
import com.differentshadow.personalfinance.domain.usecase.billreminder.CreateBillReminderUseCase
import com.differentshadow.personalfinance.utils.CategoryList
import com.differentshadow.personalfinance.utils.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BillReminderFormViewModel @Inject constructor(
    private val createBillReminderUseCase: CreateBillReminderUseCase
): ViewModel() {
    private val currentDateTime = System.currentTimeMillis()
    private val _uiState = MutableStateFlow(BillReminderForm(date = currentDateTime, time = currentDateTime))
    val uiState: StateFlow<BillReminderForm> = _uiState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onInputChanged(eventType: BillReminderInputEvent) {
        when (eventType) {
            is BillReminderInputEvent.onTitleChanged -> {
                _uiState.update {
                    it.copy(
                        title = eventType.value
                    )
                }
            }
            is BillReminderInputEvent.onCategoryChanged -> {
                _uiState.update {
                    it.copy(
                        category = eventType.value
                    )
                }
            }
            is BillReminderInputEvent.onDateChanged -> {
                _uiState.update {
                    it.copy(
                        date = eventType.value
                    )
                }
            }
            is BillReminderInputEvent.onTimeChanged -> {
                _uiState.update {
                    it.copy(
                        time = eventType.value
                    )
                }
            }
            is BillReminderInputEvent.onBillAmount -> {
                val billAmount = eventType.value.replace(",", "").replace(" ", "").trim()
                _uiState.update {
                    it.copy(
                        billAmount = billAmount
                    )
                }
            }
            is BillReminderInputEvent.onSubmit -> {
                onSubmit()
                val resetDateAndTime = LocalDateTime.now()
                _uiState.update {
                    BillReminderForm(
                        date = resetDateAndTime.toLong(),
                        time = resetDateAndTime.toLong()
                    )
                }
            }
        }
    }

    private fun validateTitle(): ValidationResult {
        if (_uiState.value.title.isBlank() || _uiState.value.title == "" || _uiState.value.title.isEmpty()) {
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
        if (_uiState.value.category.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Category is required"
            )
        }

        if (!CategoryList.contains(_uiState.value.category)) {
            return ValidationResult(
                successful = false,
                errorMessage = "Category is invalid"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateDate(): ValidationResult {
        if (_uiState.value.date <= 0L) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please select a valid date"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateTime(): ValidationResult {
        if (_uiState.value.time <= 0L) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please select a valid time"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateBillAmount(): ValidationResult {
        if (_uiState.value.billAmount.isBlank() || _uiState.value.billAmount.toDouble().isNaN() || _uiState.value.billAmount.toDouble() <= 0.0) {
            return ValidationResult(
                successful = false,
                errorMessage = "Bill Amount is required"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun onSubmit() {
        val titleValidationResult = validateTitle()
        val categoryValidationResult = validateCategory()
        val dateValidationResult = validateDate()
        val timeValidationResult = validateTime()
        val billAmountValidationResult = validateBillAmount()

        val hasError = listOf(
            titleValidationResult,
            categoryValidationResult,
            dateValidationResult,
            timeValidationResult,
            billAmountValidationResult
        ).any { !it.successful }

        if (hasError) {
            _uiState.update {
                it.copy(
                    titleErrorMsg = titleValidationResult.errorMessage,
                    categoryErrorMsg = categoryValidationResult.errorMessage,
                    dateErrorMsg = dateValidationResult.errorMessage,
                    timeErrorMsg = timeValidationResult.errorMessage,
                    billAmountErrorMsg = billAmountValidationResult.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            createBillReminderUseCase.invoke(
                title =  _uiState.value.title,
                category = _uiState.value.category,
                duedate = dateAndTime(_uiState.value.date, _uiState.value.time),
                amount = _uiState.value.billAmount.toDouble()
            )
            validationEventChannel.send(ValidationEvent.Success)
        }
    }
}