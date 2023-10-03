package com.differentshadow.personalfinance.domain.usecase.transaction

import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepositoryBase
import com.differentshadow.personalfinance.data.source.Transaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val transactionRepo: TransactionRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(merchantName: String, address: String, dateTime: Long, category: String, total: Double ) {
        withContext(ioDispatcher) {
            transactionRepo.createTransaction(Transaction(merchantName = merchantName, address = address, date = dateTime, category = category, total = total, isDeleted = false, createdAt = System.currentTimeMillis()))
        }
    }
}