package com.differentshadow.personalfinance.data.repository.billreminder

import com.differentshadow.personalfinance.data.source.BillReminder
import kotlinx.coroutines.flow.Flow

interface BillReminderRepositoryBase {
    suspend fun createBillReminder(billReminder: BillReminder)

    fun fetchBillReminders(isDeleted: Boolean = false, isPaid: Boolean): Flow<List<BillReminder>>

    suspend fun fetchBillReminder(id: Long): Result<BillReminder>

    suspend fun updateBillReminder(billReminder: BillReminder)

    suspend fun updateBillReminderById(id: Long, beenPaid: Boolean)

    suspend fun softDeleteBillReminder(id: Long)

    suspend fun deleteBillReminder(billReminder: BillReminder)
}