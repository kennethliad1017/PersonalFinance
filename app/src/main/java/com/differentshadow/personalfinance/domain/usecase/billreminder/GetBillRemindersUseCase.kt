package com.differentshadow.personalfinance.domain.usecase.billreminder

import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepositoryBase
import com.differentshadow.personalfinance.domain.model.BillReminderEnity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBillRemindersUseCase  @Inject constructor(
    private val billReminderRepo: BillReminderRepositoryBase,
    private val formatDateToStringUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
){
    suspend operator fun invoke(isPaid: Boolean): Flow<List<BillReminderEnity>> {
        val billReminders = withContext(ioDispatcher) {
            billReminderRepo.fetchBillReminders(isDeleted = false, isPaid)

        }

        val billReminderList = billReminders.map { billreminders ->
            billreminders.map {
                BillReminderEnity(
                    id = it.id,
                    title = it.title,
                    amount = it.amount,
                    category = it.category,
                    dueDate = formatDateToStringUseCase.invoke(it.dueDate, "EEE d, MMM yyyy"),
                    isPaid = it.isPaid,
                    isDeleted = it.isDeleted,
                    createdAt = formatDateToStringUseCase.invoke(it.createdAt, "EEE d, MMM yyyy")
                )
            }
        }

        return billReminderList
    }
}