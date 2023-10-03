package com.differentshadow.personalfinance.domain.form

sealed class BillReminderInputEvent {
    data class onTitleChanged(val value: String) : BillReminderInputEvent()
    data class onCategoryChanged(val value: String) : BillReminderInputEvent()
    data class onDateChanged(val value: Long) : BillReminderInputEvent()
    data class onTimeChanged(val value: Long) : BillReminderInputEvent()
    data class onBillAmount(val value: String) : BillReminderInputEvent()

    object onSubmit : BillReminderInputEvent()
}
