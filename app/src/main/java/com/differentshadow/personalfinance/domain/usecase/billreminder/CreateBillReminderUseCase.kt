package com.differentshadow.personalfinance.domain.usecase.billreminder

import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepositoryBase
import com.differentshadow.personalfinance.data.source.BillReminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class CreateBillReminderUseCase @Inject constructor(
    private val billReminderRepo: BillReminderRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke (title: String, category: String, amount: Double, duedate: Long) {
        withContext(ioDispatcher) {
            billReminderRepo.createBillReminder(BillReminder(
                title = title,
                category = category,
                amount =  amount,
                dueDate = duedate,
                isPaid =  false,
                isDeleted = false,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
            ))
        }
    }
}