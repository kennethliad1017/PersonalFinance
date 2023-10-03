package com.differentshadow.personalfinance.domain.usecase.transaction

import android.util.Log
import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepositoryBase
import com.differentshadow.personalfinance.domain.model.TransactionEntity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTransactionByIntervalUseCase @Inject constructor(
    private val transactionRepo: TransactionRepositoryBase,
    private val formatDateToStringUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default) {

    suspend fun invoke(startDate: Long, endDate: Long): Flow<List<TransactionEntity>> {
        val transactions = withContext(ioDispatcher) {
            transactionRepo.fetchTransactionsByInterval(startDate = startDate, endDate = endDate)
        }


        return transactions.map {
            it.map {transaction ->
                TransactionEntity(
                    id = transaction.id,
                    merchantName = transaction.merchantName,
                    address = transaction.address,
                    date = formatDateToStringUseCase.invoke(transaction.date, "EEE d, MMM yyyy"),
                    category = transaction.category,
                    total = transaction.total,
                    isDeleted = transaction.isDeleted,
                    createdAt = formatDateToStringUseCase.invoke(transaction.createdAt, "EEE d, MMM yyyy")
                )
            }
        }
    }
}