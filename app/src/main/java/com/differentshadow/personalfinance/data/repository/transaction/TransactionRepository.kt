package com.differentshadow.personalfinance.data.repository.transaction

import com.differentshadow.personalfinance.data.source.FinanceDao
import com.differentshadow.personalfinance.data.source.Transaction
import com.differentshadow.personalfinance.data.source.TransactionSummary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(private val financeDao: FinanceDao): TransactionRepositoryBase {
    override suspend fun createTransaction(transaction: Transaction) {
        financeDao.createTransaction(transaction)
    }

    override suspend fun fetchTransaction(id: Long): Result<Transaction> {
        return try {
            val transaction = financeDao.fetchTransaction(id)
            if (transaction != null) {
                Result.success(transaction)
            } else {
                Result.failure(Exception("Transaction's not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun fetchTransactions(isDeleted: Boolean): Flow<List<Transaction>> {
        return financeDao.fetchTransactions(isDeleted)
    }

    override fun fetchTransactionsByInterval(
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> {
        return financeDao.fetchTransactionsByInterval(startDate, endDate)
    }

    override fun fetchTotalExpenses(startDate: Long,
                                    endDate: Long): Flow<TransactionSummary> {
        return financeDao.fetchTotalExpenses(startDate, endDate)
    }

    override fun fetchTotalExpenses(beenDeleted: Boolean): Flow<TransactionSummary> {
        return financeDao.fetchTotalExpenses(beenDeleted)
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        financeDao.updateTransaction(transaction)
    }


    override suspend fun softDeleteTransaction(id: Long) {
        financeDao.softDeleteTransaction(id)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        financeDao.deleteTransaction(transaction)
    }
}