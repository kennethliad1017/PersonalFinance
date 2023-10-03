package com.differentshadow.personalfinance.domain.usecase.transaction

import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepositoryBase
import com.differentshadow.personalfinance.domain.model.TransactionEntity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import com.differentshadow.personalfinance.utils.toDate
import com.differentshadow.personalfinance.utils.toDateString
import com.differentshadow.personalfinance.utils.toLong
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepo: TransactionRepositoryBase,
    private val formatDateToStringUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(beenDeleted: Boolean = false, selectedDate: LocalDate): Flow<Map<String, List<TransactionEntity>>> {
        val transactions = withContext(ioDispatcher) {
            transactionRepo.fetchTransactions(beenDeleted)
        }



        return transactions.map {
            val items: Map<String, List<TransactionEntity>> = mapOf()


            it.forEach {transaction ->
                items.plus(when {
                    transaction.date >= LocalDateTime.of(selectedDate, LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate, LocalTime.MAX).toLong() -> {
                        "Today" to TransactionEntity(
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

                    transaction.date >= LocalDateTime.of(selectedDate.minusDays(1), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate.minusDays(1), LocalTime.MAX).toLong() -> {
                        "Yesterday" to TransactionEntity(
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

                    transaction.date >= LocalDateTime.of(selectedDate.minusDays(2), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate.minusDays(2), LocalTime.MAX).toLong() -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE") to TransactionEntity(
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

                    transaction.date >= LocalDateTime.of(selectedDate.minusDays(3), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate.minusDays(3), LocalTime.MAX).toLong() -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE") to TransactionEntity(
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

                    transaction.date >= LocalDateTime.of(selectedDate.minusDays(4), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate.minusDays(4), LocalTime.MAX).toLong() -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE") to TransactionEntity(
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

                    transaction.date >= LocalDateTime.of(selectedDate.minusDays(5), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate.minusDays(5), LocalTime.MAX).toLong() -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE") to TransactionEntity(
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

                    transaction.date >= LocalDateTime.of(selectedDate.minusDays(6), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate.minusDays(6), LocalTime.MAX).toLong() -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE") to TransactionEntity(
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

                    transaction.date >= LocalDateTime.of(selectedDate.minusDays(7), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(selectedDate.minusDays(7), LocalTime.MAX).toLong() -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE") to TransactionEntity(
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



                    transaction.date >= LocalDateTime.of(transaction.date.toDate(), LocalTime.MIN) .toLong()&& transaction.date <= LocalDateTime.of(transaction.date.toDate(), LocalTime.MAX).toLong() -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE d, MMM") to TransactionEntity(
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

                    else -> {
                        formatDateToStringUseCase.invoke(transaction.date, "EEE d, MMM yyyy") to TransactionEntity(
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
                })
            }

            items
        }
    }
}