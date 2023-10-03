package com.differentshadow.personalfinance.domain.usecase.billreminder

import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepositoryBase
import com.differentshadow.personalfinance.domain.model.BillReminderEnity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBillReminderByIdUseCase @Inject constructor(
    private val billReminderRepo: BillReminderRepositoryBase,
    private val formatDateUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(id: Long): Result<BillReminderEnity> {

        return try {
            val billReminder =  withContext(ioDispatcher) {
                billReminderRepo.fetchBillReminder(id)
            }

            val billReminderEnity = billReminder.map {
                BillReminderEnity(
                    id = it.id,
                    title = it.title,
                    amount = it.amount,
                    category = it.category,
                    dueDate = formatDateUseCase.invoke(it.dueDate, "EEE d, MMM yyyy"),
                    isPaid = it.isPaid,
                    isDeleted = it.isDeleted,
                    createdAt = formatDateUseCase.invoke(it.createdAt, "EEE d, MMM yyyy")
                )
            }

            if (billReminder != null) {
                billReminderEnity
            } else {
                Result.failure(Exception("Bill Reminder doesn't exists"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}