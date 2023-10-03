package com.differentshadow.personalfinance.data.repository.billreminder

import com.differentshadow.personalfinance.data.source.BillReminder
import com.differentshadow.personalfinance.data.source.FinanceDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillReminderRepository @Inject constructor(private val financeDao: FinanceDao): BillReminderRepositoryBase {
    override suspend fun createBillReminder(billReminder: BillReminder) {
        financeDao.createBillReminder(billReminder);
    }

    override fun fetchBillReminders(
        isDeleted: Boolean,
        isPaid: Boolean
    ): Flow<List<BillReminder>> {
        return financeDao.fetchBillReminders(isDeleted, isPaid)
    }

    override suspend fun fetchBillReminder(id: Long): Result<BillReminder> {
        return try {
            val task = financeDao.fetchBillReminder(id)
            if (task != null) {
                Result.success(task)
            } else {
                Result.failure(Exception("bill reminder not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBillReminder(billReminder: BillReminder) {
        financeDao.updateBillReminder(billReminder);
    }

    override suspend fun updateBillReminderById(id: Long, beenPaid: Boolean) {
        financeDao.updateBillReminderById(id, beenPaid)
    }

    override suspend fun softDeleteBillReminder(id: Long) {
        financeDao.softDeleteBillReminder(id)
    }

    override suspend fun deleteBillReminder(billReminder: BillReminder) {
        financeDao.deleteBillReminder(billReminder)
    }
}