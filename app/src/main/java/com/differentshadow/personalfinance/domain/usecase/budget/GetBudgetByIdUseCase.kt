package com.differentshadow.personalfinance.domain.usecase.budget

import com.differentshadow.personalfinance.data.repository.budget.BudgetRepositoryBase
import com.differentshadow.personalfinance.domain.model.BudgetEntity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetBudgetByIdUseCase @Inject constructor(
    private val budgetRepo: BudgetRepositoryBase,
    private val formatDateUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
)  {

    suspend fun invoke(id: Long): Result<BudgetEntity> {
        return try {
            val budget = withContext(ioDispatcher) {
                budgetRepo.fetchBudget(id)
            }

            if (budget != null) {
                val budgetEntity = budget.map {
                    BudgetEntity(
                        id = it.id,
                        category = it.category,
                        budgetLimit = it.budgetLimit,
                        isDeleted = it.isDeleted,
                        resetFrequency = it.resetFrequency,
                        currentExpense = 0.0,
                        createdAt = formatDateUseCase.invoke(it.createdAt, "EEE d, MMM yyyy")
                    )
                }

                budgetEntity
            } else {
                Result.failure(Exception("Budget doesn't exist!"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}