package com.differentshadow.personalfinance.domain.usecase.transaction

import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepositoryBase
import com.differentshadow.personalfinance.data.source.Transaction
import com.differentshadow.personalfinance.domain.usecase.FormatStringToDateUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(
    private val transactionRepo: TransactionRepositoryBase,
    private val formatStringToDateUseCase: FormatStringToDateUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(merchantName: String, address: String, date: Long, category: String, total: Double, isDeleted: Boolean, createdAt: String) {
        withContext(ioDispatcher) {
            transactionRepo.updateTransaction(Transaction(merchantName = merchantName, address = address, date = date, category = category, total = total, isDeleted = isDeleted, createdAt = formatStringToDateUseCase.invoke(createdAt, "EEE d, MMM yyyy")))
        }
    }
}