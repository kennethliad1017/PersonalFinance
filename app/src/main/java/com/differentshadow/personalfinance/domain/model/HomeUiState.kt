package com.differentshadow.personalfinance.domain.model

import java.math.BigDecimal

data class HomeUiState(
    val currentExpenses: BigDecimal = BigDecimal(0.0),
    val overviewGoals: List<SavingGoalEntity> = listOf(),
    val isFetching: Boolean = false,
)
