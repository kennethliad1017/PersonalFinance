package com.differentshadow.personalfinance.domain.model

data class BillReminderEnity(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val dueDate: String,
    val isPaid: Boolean,
    val isDeleted: Boolean,
    val createdAt: String
)

data class BillReminderUIState(
    val billReminders: List<BillReminderEnity> = listOf(),
    val billReminder: BillReminderEnity? = null,
    val errorMessage: String? = null,
    val isFetching: Boolean = false,
)
