package com.differentshadow.personalfinance.domain.model

data class NotificationUIState(
    val billReminderNotify: List<BillReminderEnity> = listOf(),
    val errorMessage: String? = null,
    val isFetching: Boolean = false,
)
