package com.differentshadow.personalfinance.domain.form

import com.differentshadow.personalfinance.utils.Categories

data class TransactionForm(
    val merchantName: String = "",
    val merchantErrorMsg: String? = null,
    val address: String = "",
    val addressErrorMsg: String? = null,
    val date: Long,
    val dateErrorMsg: String? = null,
    val time: Long,
    val timeErrorMsg: String? = null,
    val category: String = Categories.Grocery.name,
    val categoryErrorMsg: String? = null,
    val totalAmount: String = "0.0",
    val totalExpenseErrorMsg: String? = null,
)
