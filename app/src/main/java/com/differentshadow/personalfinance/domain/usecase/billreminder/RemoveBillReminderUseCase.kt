package com.differentshadow.personalfinance.domain.usecase.billreminder

import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepository
import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepositoryBase
import com.differentshadow.personalfinance.data.source.BillReminder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveBillReminderUseCase @Inject constructor(
    private val billReminderRepo: BillReminderRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(billReminder: BillReminder) {
        withContext(ioDispatcher) {
            billReminderRepo.deleteBillReminder(billReminder)
        }
    }
}