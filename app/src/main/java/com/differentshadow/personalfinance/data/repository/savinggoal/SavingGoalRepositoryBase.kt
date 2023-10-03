package com.differentshadow.personalfinance.data.repository.savinggoal

import com.differentshadow.personalfinance.data.source.SavingGoal
import kotlinx.coroutines.flow.Flow

interface SavingGoalRepositoryBase {

    suspend fun createGoal(savingGoal: SavingGoal)

    fun fetchGoals(isDeleted: Boolean = false): Flow<List<SavingGoal>>

    fun fetchGoalsOverview(limit: Int = 6): Flow<List<SavingGoal>>

    suspend fun fetchGoal(id: Long): Result<SavingGoal>

    suspend fun  updateGoal(goal: SavingGoal)

    suspend fun softDeleteGoal(id: Long)

    suspend fun deleteGoal(goal: SavingGoal)
}