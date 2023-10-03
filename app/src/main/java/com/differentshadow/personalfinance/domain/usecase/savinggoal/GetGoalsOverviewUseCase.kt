package com.differentshadow.personalfinance.domain.usecase.savinggoal

import com.differentshadow.personalfinance.data.repository.savinggoal.SavingGoalRepositoryBase
import com.differentshadow.personalfinance.domain.model.SavingGoalEntity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGoalsOverviewUseCase @Inject constructor(
    private val goalRepo: SavingGoalRepositoryBase,
    private val formatDateToStringUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    suspend fun invoke(limit: Int = 6): Flow<List<SavingGoalEntity>> {
        val goals = withContext(ioDispatcher) {
            goalRepo.fetchGoalsOverview(limit)
        }

        return goals.map {
            it.map {goal ->
                SavingGoalEntity(
                    id = goal.id,
                    title = goal.title,
                    category = goal.category,
                    currentSaving = goal.currentSaving,
                    goalSaving = goal.goalSaving,
                    goalDate =  formatDateToStringUseCase.invoke(goal.goalDate, "EEE d, MMM yyyy"),
                    isDeleted = goal.isDeleted,
                    createdAt = formatDateToStringUseCase.invoke(goal.createdAt, "EEE d, MMM yyyy")
                )

            }
        }
    }
}