package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.domain.model.BillReminderUIState
import com.differentshadow.personalfinance.domain.usecase.billreminder.CreateBillReminderUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.DeleteBillReminderUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.GetBillReminderByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.GetBillRemindersUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.RemoveBillReminderUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.UpdateBillReminderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BillReminderViewModel @Inject constructor(
    private val createBillReminderUseCase: CreateBillReminderUseCase,
    private val getBillReminderByIdUseCase: GetBillReminderByIdUseCase,
    private val getBillRemindersUseCase: GetBillRemindersUseCase,
    private val updateBillReminderUseCase: UpdateBillReminderUseCase,
    private val softDeleteBillReminderUseCase: DeleteBillReminderUseCase,
    private val deleteBillReminderUsecase: RemoveBillReminderUseCase,
): ViewModel() {
    private val _uiState = MutableLiveData(BillReminderUIState())
    val uiState: LiveData<BillReminderUIState> = _uiState

    init {
        _uiState.value = _uiState.value?.copy(isFetching = true)
        viewModelScope.launch {
            getBillRemindersUseCase(false).collect {
                _uiState.postValue(_uiState.value?.copy(billReminders = it, isFetching = false))
            }
        }
    }


    fun createBillReminder(title: String, category: String, amount: Double, dueDate: Long) {
        viewModelScope.launch {
            createBillReminderUseCase.invoke(title = title, category = category, amount = amount, duedate = dueDate)
        }
    }

    fun getBillReminder(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isFetching = true)
            withContext(Dispatchers.IO) {
                val billReminderResult = getBillReminderByIdUseCase.invoke(id)

                billReminderResult.onSuccess {
                    _uiState.postValue(_uiState.value?.copy(billReminder = it, isFetching = false))
                }

                billReminderResult.onFailure {
                    _uiState.postValue(_uiState.value?.copy(errorMessage = it.message, isFetching = false))
                }
            }
        }
    }

    fun clearErrorMessage() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }
}