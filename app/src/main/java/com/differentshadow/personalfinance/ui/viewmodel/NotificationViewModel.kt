package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.domain.model.NotificationUIState
import com.differentshadow.personalfinance.domain.usecase.billreminder.GetBillRemindersUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.UpdateBillReminderByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getBillRemindersUseCase: GetBillRemindersUseCase,
    private val updateBillReminderByIdUseCase: UpdateBillReminderByIdUseCase,
): ViewModel() {
    private val _uiState = MutableLiveData(NotificationUIState())
    val uiState: LiveData<NotificationUIState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value?.copy(isFetching = true)
            getBillRemindersUseCase.invoke(isPaid = false).collect {
                _uiState.postValue(_uiState.value?.copy(billReminderNotify = it, isFetching = false))
            }
        }
    }

    fun billReminderBeenPaid(id: Long) {
        updateBillReminder(id)
    }


    private fun updateBillReminder(id: Long) {
        viewModelScope.launch {
            updateBillReminderByIdUseCase.invoke(id)
        }
    }

}