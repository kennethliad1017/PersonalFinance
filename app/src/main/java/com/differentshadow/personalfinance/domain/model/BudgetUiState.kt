package com.differentshadow.personalfinance.domain.model


data class BudgetEntity(
    val id: Long,
    val category: String,
    val currentExpense: Double,
    val budgetLimit: Double,
    val isDeleted: Boolean,
    val resetFrequency: String,
    val createdAt: String,
)

data class BudgetUiState(
    val budgets: List<BudgetEntity> = listOf(),
    val budgetEntity: BudgetEntity? = null,
    val errorMessage: String? = null,
    val isFetching: Boolean = false,
)