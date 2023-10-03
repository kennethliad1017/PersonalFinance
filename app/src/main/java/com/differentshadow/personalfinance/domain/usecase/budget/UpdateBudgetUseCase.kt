package com.differentshadow.personalfinance.domain.usecase.budget

import com.differentshadow.personalfinance.data.repository.budget.BudgetRepositoryBase
import com.differentshadow.personalfinance.data.source.Budget
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateBudgetUseCase @Inject constructor(
    private val budgetRepo: BudgetRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
)  {
    suspend fun invoke(budget: Budget) {
        withContext(ioDispatcher) {
            budgetRepo.updateBudget(budget)
        }
    }
}