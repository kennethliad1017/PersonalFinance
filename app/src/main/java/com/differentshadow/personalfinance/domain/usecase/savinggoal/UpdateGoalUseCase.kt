package com.differentshadow.personalfinance.domain.usecase.savinggoal

import com.differentshadow.personalfinance.data.repository.savinggoal.SavingGoalRepositoryBase
import com.differentshadow.personalfinance.data.source.SavingGoal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateGoalUseCase @Inject constructor(
    private val goalRepo: SavingGoalRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(goal: SavingGoal) {
        withContext(ioDispatcher) {
            goalRepo.updateGoal(goal)
        }
    }
}