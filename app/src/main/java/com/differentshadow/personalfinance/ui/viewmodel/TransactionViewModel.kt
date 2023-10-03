package com.differentshadow.personalfinance.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.differentshadow.personalfinance.domain.model.TransactionEntity
import com.differentshadow.personalfinance.domain.model.TransactionUIState
import com.differentshadow.personalfinance.domain.model.TransactionsUIState
import com.differentshadow.personalfinance.domain.usecase.transaction.CreateTransactionUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.DeleteTransactionUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionByIntervalUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionsUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.RemoveTransactionUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.UpdateTransactionUseCase
import com.differentshadow.personalfinance.utils.toDateString
import com.differentshadow.personalfinance.utils.toLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    private val getTransactionByIntervalUseCase: GetTransactionByIntervalUseCase,
    private val getTransactionByIdUseCase: GetTransactionByIdUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val removeTransactionUseCase: RemoveTransactionUseCase,
) : ViewModel() {
    private val _uiState = MutableLiveData(TransactionsUIState())
    val uiState: LiveData<TransactionsUIState> = _uiState


    private val _transactionUIState = MutableLiveData(TransactionUIState())
    val transactionUIState: LiveData<TransactionUIState> = _transactionUIState


    fun getTransactionId(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val transactionResult = getTransactionByIdUseCase.invoke(id)

                transactionResult.onSuccess {
                    _transactionUIState.postValue(
                        TransactionUIState(
                            transaction = it, isFetching = false
                        )
                    )
                }

                transactionResult.onFailure {
                    _transactionUIState.postValue(
                        TransactionUIState(
                            errorMessage = it.message, isFetching = false
                        )
                    )
                }
            }
        }
    }

    fun getTransactions(selectedDate: LocalDate = LocalDate.now()) {
        _uiState.value = _uiState.value?.copy(isFetching = true)
        viewModelScope.launch {
            getTransactionsUseCase.invoke(false, selectedDate).collect {
                _uiState.postValue(_uiState.value?.copy(transactions = it))
            }
        }
    }
}
