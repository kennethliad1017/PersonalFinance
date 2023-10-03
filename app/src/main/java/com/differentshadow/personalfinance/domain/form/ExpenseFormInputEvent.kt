package com.differentshadow.personalfinance.domain.form

sealed class ExpenseFormInputEvent {
    data class onMerchantChanged(val value: String): ExpenseFormInputEvent()
    data class onMerchantAddressChanged(val value: String): ExpenseFormInputEvent()
    data class onDateChanged(val value: Long): ExpenseFormInputEvent()
    data class onTimeChanged(val value: Long): ExpenseFormInputEvent()
    data class onCategoryChanged(val value: String): ExpenseFormInputEvent()
    data class onTotalExpenseChanged(val value: String): ExpenseFormInputEvent()

    object onSubmit: ExpenseFormInputEvent()
}
