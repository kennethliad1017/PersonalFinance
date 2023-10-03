package com.differentshadow.personalfinance.data.repository.transaction

import com.differentshadow.personalfinance.data.source.Transaction
import com.differentshadow.personalfinance.data.source.TransactionSummary
import kotlinx.coroutines.flow.Flow

interface TransactionRepositoryBase {
    suspend fun createTransaction(transaction: Transaction)

    suspend fun fetchTransaction(id: Long): Result<Transaction>

    fun fetchTransactions(isDeleted: Boolean = false): Flow<List<Transaction>>

    fun fetchTransactionsByInterval(startDate: Long, endDate: Long): Flow<List<Transaction>>

    fun fetchTotalExpenses(startDate: Long, endDate: Long): Flow<TransactionSummary>

    fun fetchTotalExpenses(beenDeleted: Boolean = false): Flow<TransactionSummary>

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun softDeleteTransaction(id: Long)

    suspend fun deleteTransaction(transaction: Transaction)
}