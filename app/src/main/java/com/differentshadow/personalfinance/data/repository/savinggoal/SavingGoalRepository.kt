package com.differentshadow.personalfinance.data.repository.savinggoal

import com.differentshadow.personalfinance.data.source.FinanceDao
import com.differentshadow.personalfinance.data.source.SavingGoal
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavingGoalRepository @Inject constructor(private val financeDao: FinanceDao): SavingGoalRepositoryBase {
    override suspend fun createGoal(savingGoal: SavingGoal) {
        financeDao.createSavingGoal(savingGoal)
    }

    override fun fetchGoals(isDeleted: Boolean): Flow<List<SavingGoal>> {
        return financeDao.fetchGoals(isDeleted)
    }

    override fun fetchGoalsOverview(limit: Int): Flow<List<SavingGoal>> {
        return financeDao.fetchGoalsOverview(limit)
    }

    override suspend fun fetchGoal(id: Long): Result<SavingGoal> {
        return try {
            val goal = financeDao.fetchGoal(id)
            if (goal != null) {
                Result.success(goal)
            } else {
                Result.failure(Exception("Goal's not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateGoal(goal: SavingGoal) {
        financeDao.updateGoal(goal)
    }

    override suspend fun softDeleteGoal(id: Long) {
        financeDao.softDeleteGoal(id)
    }

    override suspend fun deleteGoal(goal: SavingGoal) {
        financeDao.deleteGoal(goal)
    }
}