package com.differentshadow.personalfinance.domain.form

data class BillReminderForm(
    val title: String = "",
    val titleErrorMsg: String? = null,
    val category: String = "Grocery",
    val categoryErrorMsg: String? = null,
    val date: Long,
    val dateErrorMsg: String? = null,
    val time: Long,
    val timeErrorMsg: String? = null,
    val billAmount: String = "0",
    val billAmountErrorMsg: String? = null,
)