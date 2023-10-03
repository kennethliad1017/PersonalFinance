package com.differentshadow.personalfinance.domain.usecase.transaction

import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepositoryBase
import com.differentshadow.personalfinance.domain.model.TransactionEntity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepo: TransactionRepositoryBase,
    private val formatDateToStringUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(id: Long): Result<TransactionEntity> {
        val transaction = withContext(ioDispatcher) {
            transactionRepo.fetchTransaction(id)
        }

        return transaction.map {
            TransactionEntity(
                id = it.id,
                merchantName = it.merchantName,
                address = it.address,
                date = formatDateToStringUseCase.invoke(it.date, "EEE d, MMM yyyy"),
                category = it.category,
                total = it.total,
                isDeleted = it.isDeleted,
                createdAt = formatDateToStringUseCase.invoke(it.createdAt, "EEE d, MMM yyyy")
            )
        }
    }
}