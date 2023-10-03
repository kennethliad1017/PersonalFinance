package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.utils.ValidationResult
import com.differentshadow.personalfinance.domain.form.ExpenseFormInputEvent
import com.differentshadow.personalfinance.domain.form.TransactionForm
import com.differentshadow.personalfinance.domain.usecase.transaction.CreateTransactionUseCase
import com.differentshadow.personalfinance.utils.dateAndTime
import com.differentshadow.personalfinance.domain.form.ValidationEvent
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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ExpenseFormViewModel @Inject constructor(
    private val createExpenseUseCase: CreateTransactionUseCase
): ViewModel() {

    private val currentDateTime = System.currentTimeMillis()

    private val _uiState = MutableStateFlow(TransactionForm(date = currentDateTime, time = currentDateTime))
    val uiState: StateFlow<TransactionForm> = _uiState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    fun onInputChanged(eventType: ExpenseFormInputEvent) {
        when (eventType) {
            is ExpenseFormInputEvent.onMerchantChanged -> {
                _uiState.update {
                    it.copy(
                        merchantName = eventType.value
                    )
                }
            }
            is ExpenseFormInputEvent.onMerchantAddressChanged -> {
                _uiState.update {
                    it.copy(
                        address = eventType.value
                    )
                }
            }
            is ExpenseFormInputEvent.onDateChanged -> {
                _uiState.update {
                    it.copy(
                        date = eventType.value
                    )
                }
            }
            is ExpenseFormInputEvent.onTimeChanged -> {
                _uiState.update {
                    it.copy(
                        time = eventType.value
                    )
                }
            }
            is ExpenseFormInputEvent.onCategoryChanged -> {
                _uiState.update {
                    it.copy(
                        category = eventType.value
                    )
                }
            }
            is ExpenseFormInputEvent.onTotalExpenseChanged -> {
                val totalAmount = eventType.value.replace(",", "").replace(" ", "").trim()
                _uiState.update {
                    it.copy(
                        totalAmount = totalAmount
                    )
                }
            }
            is ExpenseFormInputEvent.onSubmit -> {
                submit()
                val resetDateAndTime = LocalDateTime.now()
                _uiState.update {
                    TransactionForm(
                        date = resetDateAndTime.toLong(),
                        time = resetDateAndTime.toLong()
                    )
                }
            }
        }
    }

    private fun submit() {
        val merchantValidationResult = validateMerchant()
        val addressValidationResult = validateAddress()
        val dateValidationResult = validateDate()
        val timeValidationResult = validateTime()
        val categoryValidationResult = validateCategory()
        val totalValidationResult = validateTotalExpense()

        val hasError = listOf(
            merchantValidationResult,
            addressValidationResult,
            dateValidationResult,
            timeValidationResult,
            categoryValidationResult,
            totalValidationResult,
        ).any { !it.successful }

        if (hasError) {
            _uiState.update {
                it.copy(
                    merchantErrorMsg = merchantValidationResult.errorMessage,
                    addressErrorMsg = addressValidationResult.errorMessage,
                    dateErrorMsg = dateValidationResult.errorMessage,
                    timeErrorMsg = timeValidationResult.errorMessage,
                    categoryErrorMsg = categoryValidationResult.errorMessage,
                    totalExpenseErrorMsg = totalValidationResult.errorMessage,
                )
            }

            return
        }

        viewModelScope.launch {
            createExpenseUseCase.invoke(
                merchantName = _uiState.value.merchantName,
                address = _uiState.value.address,
                dateTime = dateAndTime(_uiState.value.date, _uiState.value.time),
                category = _uiState.value.category,
                total = _uiState.value.totalAmount.toDouble()
            )
            validationEventChannel.send(ValidationEvent.Success)
        }
    }


    private fun validateMerchant(): ValidationResult {
        if (_uiState.value.merchantName.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Merchant name is required"
            )
        }

        return ValidationResult(
            successful = true
        )
    }

    private fun validateAddress(): ValidationResult {
        if (_uiState.value.address.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Address is required"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

    private fun validateDate(): ValidationResult {
        if (_uiState.value.date < 0L) {
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
        if (_uiState.value.time < 0L) {
            return ValidationResult(
                successful = false,
                errorMessage = "Please select a valid time"
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
                errorMessage = "Category is invalid"
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

    private fun validateTotalExpense(): ValidationResult {
        if (_uiState.value.totalAmount.toDouble() <= 0.0 || _uiState.value.totalAmount.toDouble().isNaN()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Total amount should be a valid positive value"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}
