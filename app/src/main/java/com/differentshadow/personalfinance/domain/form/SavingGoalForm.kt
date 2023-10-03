package com.differentshadow.personalfinance.domain.form

data class SavingGoalForm(
    val title: String = "",
    val titleErrorMsg: String? = null,
    val category: String = "Grocery",
    val categoryErrorMsg: String? = null,
    val goalAmount: String = "0.0",
    val goalAmountErrorMsg: String? = null,
    val goalDate: Long = System.currentTimeMillis(),
    val goalDateErrorMsg: String? = null,
)
