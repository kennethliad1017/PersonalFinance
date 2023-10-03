package com.differentshadow.personalfinance.domain.model

import java.math.BigDecimal

data class AnalyticsUIState(
    val analytics: List<TransactionEntity> = listOf(),
    val errorMessage: String? = null,
    val isFetching: Boolean = false,
)



data class Expense(val date: Long, val amount: BigDecimal)