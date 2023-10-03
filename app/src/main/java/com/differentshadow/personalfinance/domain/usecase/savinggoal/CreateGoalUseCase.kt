package com.differentshadow.personalfinance.domain.usecase.savinggoal

import com.differentshadow.personalfinance.data.repository.savinggoal.SavingGoalRepositoryBase
import com.differentshadow.personalfinance.data.source.SavingGoal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateGoalUseCase @Inject constructor(
    private val goalRepo: SavingGoalRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(title: String, category: String, goalAmount: Double, goalDate: Long) {
        withContext(ioDispatcher) {
            goalRepo.createGoal(SavingGoal(
                title = title,
                category = category,
                currentSaving = 0.0,
                goalSaving = goalAmount,
                goalDate = goalDate,
                isDeleted = false,
                createdAt = System.currentTimeMillis(),
            ))
        }
    }
}