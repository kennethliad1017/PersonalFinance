package com.differentshadow.personalfinance.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun  createTransaction(transaction: Transaction)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createBudget(budget: Budget)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createSavingGoal(goal: SavingGoal)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createBillReminder(billReminder: BillReminder)

    @Query("SELECT * FROM budget_table WHERE is_deleted = :isDeleted")
    fun fetchBudgets(isDeleted: Boolean = false): Flow<List<Budget>>

    @Query("SELECT * FROM budget_table WHERE id = :id")
    suspend fun  fetchBudget(id: Long): Budget?

    @Query("SElECT * FROM saving_goal_table WHERE is_deleted = :isDeleted")
    fun fetchGoals(isDeleted: Boolean = false): Flow<List<SavingGoal>>

    @Query("SELECT * FROM saving_goal_table WHERE id = :id")
    suspend fun fetchGoal(id: Long): SavingGoal?

    @Query("SELECT * FROM saving_goal_table ORDER BY id DESC LIMIT :limit")
    fun fetchGoalsOverview(limit: Int): Flow<List<SavingGoal>>

    @Query("SELECT * FROM transaction_table WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun fetchTransactionsByInterval(startDate: Long, endDate: Long): Flow<List<Transaction>>

    @Query("SELECT * FROM transaction_table WHERE is_deleted= :beenDeleted ORDER BY date DESC")
    fun fetchTransactions(beenDeleted: Boolean): Flow<List<Transaction>>

    @Query("SELECT SUM(total) as currentExpense FROM transaction_table WHERE date BETWEEN :startDate AND :endDate")
    fun fetchTotalExpenses(startDate: Long, endDate: Long): Flow<TransactionSummary>

    @Query("SELECT SUM(total) as currentExpense FROM transaction_table WHERE is_deleted = :beenDeleted")
    fun fetchTotalExpenses(beenDeleted: Boolean = false): Flow<TransactionSummary>

    @Query("SELECT * FROM transaction_table WHERE id = :id")
    suspend fun fetchTransaction(id: Long): Transaction?

    @Query("SELECT * FROM bill_reminder_table WHERE is_paid = :isPaid AND is_deleted = :isDeleted ORDER BY due_date DESC")
    fun fetchBillReminders(isDeleted: Boolean = false, isPaid: Boolean): Flow<List<BillReminder>>

    @Query("SELECT * FROM bill_reminder_table WHERE id = :id")
    suspend fun fetchBillReminder(id: Long): BillReminder?

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Update
    suspend fun updateGoal(savingGoal: SavingGoal)

    @Update
    suspend fun updateBillReminder(billReminder: BillReminder)

    @Query("UPDATE bill_reminder_table SET is_paid = :beenPaid WHERE id = :id")
    suspend fun updateBillReminderById(id: Long, beenPaid: Boolean)

    @Query("UPDATE budget_table SET is_deleted = 1 WHERE id = :itemId")
    suspend fun softDeleteBudgeet(itemId: Long)

    @Query("UPDATE transaction_table SET is_deleted = 1 WHERE id = :itemId")
    suspend fun softDeleteTransaction(itemId: Long)

    @Query("UPDATE saving_goal_table SET is_deleted = 1 WHERE id = :itemId")
    suspend fun softDeleteGoal(itemId: Long)

    @Query("UPDATE bill_reminder_table SET is_deleted = 1 WHERE id = :itemId")
    suspend fun softDeleteBillReminder(itemId: Long)


    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteGoal(goal: SavingGoal)

    @Delete
    suspend fun deleteBillReminder(billReminder: BillReminder)
}