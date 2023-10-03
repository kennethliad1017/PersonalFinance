package com.differentshadow.personalfinance.domain.model

data class TransactionEntity(
    val id: Long,
    val merchantName: String,
    val address: String,
    val date: String,
    val category: String,
    val total: Double,
    val isDeleted: Boolean,
    val createdAt: String,
)

data class TransactionsUIState(
    val transactions: Map<String, List<TransactionEntity>> = mapOf(),
    val errorMessage: String? = null,
    val isFetching: Boolean = false,
)

data class TransactionUIState(
    val transaction: TransactionEntity? = null,
    val errorMessage: String? = null,
    val isFetching: Boolean = false,
)
