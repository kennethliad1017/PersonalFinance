package com.differentshadow.personalfinance.domain.usecase.savinggoal

import com.differentshadow.personalfinance.data.repository.savinggoal.SavingGoalRepositoryBase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(
    private val goalRepo: SavingGoalRepositoryBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(id: Long) {
        withContext(ioDispatcher) {
            goalRepo.softDeleteGoal(id)
        }
    }
}