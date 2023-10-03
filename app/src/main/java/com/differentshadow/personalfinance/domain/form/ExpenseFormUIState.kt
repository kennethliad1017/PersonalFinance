package com.differentshadow.personalfinance.domain.form

sealed class ExpenseFormUIState {
    object Loading: ExpenseFormUIState()
    data class Success(val data: List<TransactionForm>) : ExpenseFormUIState()
    data class Error(val message: String) : ExpenseFormUIState()
}
