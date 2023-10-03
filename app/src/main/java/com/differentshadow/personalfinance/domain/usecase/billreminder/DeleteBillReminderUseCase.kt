package com.differentshadow.personalfinance.domain.usecase.billreminder

import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepository
import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepositoryBase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteBillReminderUseCase @Inject constructor(
    private val billReminderRepo: BillReminderRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke (id: Long) {
        withContext(ioDispatcher) {
            billReminderRepo.softDeleteBillReminder(id)
        }
    }
}