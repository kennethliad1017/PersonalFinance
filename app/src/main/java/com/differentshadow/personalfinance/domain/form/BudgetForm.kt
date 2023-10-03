package com.differentshadow.personalfinance.domain.form

import com.differentshadow.personalfinance.utils.BudgetResetFreq

data class BudgetForm(
    val category: String = "Grocery",
    val currentExpense: String = "0.0",
    val currentExpenseErrorMsg: String? = null,
    val categoryErrorMsg: String? = null,
    val budgetLimit: String = "0.0",
    val budgetLimitErrorMsg: String? = null,
    val resetFrequent: String = BudgetResetFreq.EveryMonth.title,
    val resetFreqErrorMsg: String? = null,
)
