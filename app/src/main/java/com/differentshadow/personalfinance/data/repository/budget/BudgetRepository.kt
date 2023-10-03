package com.differentshadow.personalfinance.data.repository.budget

import com.differentshadow.personalfinance.data.source.Budget
import com.differentshadow.personalfinance.data.source.FinanceDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepository @Inject constructor(private val financeDao: FinanceDao): BudgetRepositoryBase {
    override suspend fun createBudget(budget: Budget) {
        financeDao.createBudget(budget)
    }

    override fun fetchBudgets(isDeleted: Boolean): Flow<List<Budget>> {
        return financeDao.fetchBudgets(isDeleted);
    }

    override suspend fun fetchBudget(id: Long): Result<Budget> {
        return try {
            val budget = financeDao.fetchBudget(id)
            if (budget != null) {
                Result.success(budget)
            } else {
                Result.failure(Exception("Budget not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBudget(budget: Budget) {
        financeDao.updateBudget(budget)
    }

    override suspend fun softDeleteBudget(id: Long) {
        financeDao.softDeleteBudgeet(id)
    }

    override suspend fun deleteBudget(budget: Budget) {
        financeDao.deleteBudget(budget)
    }
}