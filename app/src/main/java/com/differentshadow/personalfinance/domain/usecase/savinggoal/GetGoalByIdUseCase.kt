package com.differentshadow.personalfinance.domain.usecase.savinggoal

import com.differentshadow.personalfinance.data.repository.savinggoal.SavingGoalRepositoryBase
import com.differentshadow.personalfinance.domain.model.SavingGoalEntity
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetGoalByIdUseCase @Inject constructor(
    private val goalRepo: SavingGoalRepositoryBase,
    private val formatDateToStringUseCase: FormatDateToStringUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun invoke(id: Long): Result<SavingGoalEntity> {
        val savingGoal = withContext(ioDispatcher) {
            goalRepo.fetchGoal(id)
        }

        return savingGoal.map {
            SavingGoalEntity(
                id = it.id,
                title = it.title,
                category = it.category,
                currentSaving = it.currentSaving,
                goalSaving = it.goalSaving,
                goalDate = formatDateToStringUseCase.invoke(it.goalDate, "EEE d, MMM yyyy"),
                isDeleted = it.isDeleted,
                createdAt = formatDateToStringUseCase.invoke(it.createdAt, "EEE d, MMM yyyy")
            )
        }
    }
}