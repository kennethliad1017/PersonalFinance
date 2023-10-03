package com.differentshadow.personalfinance.data.repository.budget

import com.differentshadow.personalfinance.data.source.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepositoryBase {

    suspend fun createBudget(budget: Budget)

    fun fetchBudgets(isDeleted: Boolean = false): Flow<List<Budget>>



    suspend fun fetchBudget(id: Long): Result<Budget>

    suspend fun updateBudget(budget: Budget)

    suspend fun softDeleteBudget(id: Long)

    suspend fun deleteBudget(budget: Budget)
}