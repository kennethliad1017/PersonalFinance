package com.differentshadow.personalfinance.domain.usecase.transaction

import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepositoryBase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentExpensesUseCase @Inject constructor(
    private val transactionnRepo: TransactionRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun invoke(startDate: Long? = null, endDate: Long? = null): Flow<Double> {
        val result = withContext(ioDispatcher) {
            if (startDate != null && endDate != null)  {
                transactionnRepo.fetchTotalExpenses(startDate, endDate)
            } else {
                transactionnRepo.fetchTotalExpenses(false)
            }
        }

        return result.map {
            it.currentExpense
        }
    }
}