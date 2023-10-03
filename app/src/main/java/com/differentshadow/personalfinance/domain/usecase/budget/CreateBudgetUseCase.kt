package com.differentshadow.personalfinance.domain.usecase.budget

import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepository
import com.differentshadow.personalfinance.data.repository.budget.BudgetRepositoryBase
import com.differentshadow.personalfinance.data.source.Budget
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateBudgetUseCase @Inject constructor(
    private val budgetRepo: BudgetRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(category: String, currentExpense: Double, budgetLimit: Double, resetFrequency: String) {
        withContext(ioDispatcher) {
            budgetRepo.createBudget(
                Budget(
                    category = category,
                    currentExpense = currentExpense,
                    budgetLimit = budgetLimit,
                    resetFrequency = resetFrequency,
                    isDeleted = false,
                    createdAt = System.currentTimeMillis()
                )
            )
        }
    }
}