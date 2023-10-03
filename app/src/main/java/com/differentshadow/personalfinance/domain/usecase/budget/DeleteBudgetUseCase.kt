package com.differentshadow.personalfinance.domain.usecase.budget

import com.differentshadow.personalfinance.data.repository.budget.BudgetRepositoryBase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val budgetRepo: BudgetRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
)  {
    suspend fun invoke(id: Long) {
        withContext(ioDispatcher) {
            budgetRepo.softDeleteBudget(id)
        }
    }
}