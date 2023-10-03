package com.differentshadow.personalfinance.domain.usecase.budget

import com.differentshadow.personalfinance.data.repository.budget.BudgetRepositoryBase
import com.differentshadow.personalfinance.domain.model.BudgetEntity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBudgetsUseCase @Inject constructor(
    private val budgetRepo: BudgetRepositoryBase,
    private val formatDateToStringUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
)  {
    suspend fun invoke(beenDeleted: Boolean): Flow<List<BudgetEntity>> {
        val budgets = withContext(ioDispatcher) {
            budgetRepo.fetchBudgets(beenDeleted)
        }

        return budgets.map {
            it.map { budget ->
                BudgetEntity(
                    id = budget.id,
                    category = budget.category,
                    budgetLimit = budget.budgetLimit,
                    isDeleted = budget.isDeleted,
                    resetFrequency = budget.resetFrequency,
                    currentExpense = 0.0,
                    createdAt = formatDateToStringUseCase.invoke(budget.createdAt, "EEE d, MMM yyyy")
                )

            }
        }
    }
}