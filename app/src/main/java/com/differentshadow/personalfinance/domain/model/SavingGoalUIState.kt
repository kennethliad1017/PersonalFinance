package com.differentshadow.personalfinance.domain.model


data class SavingGoalEntity(
    val id: Long,
    val title: String,
    val category: String,
    val currentSaving: Double,
    val goalSaving: Double,
    val goalDate: String,
    val isDeleted: Boolean,
    val createdAt: String,
)

data class SavingGoalUIState (
    val savingGoals: List<SavingGoalEntity> = listOf(),
    val savingGoal: SavingGoalEntity? = null,
    val errorMessage: String? = null,
    val isFetching: Boolean = false,
)