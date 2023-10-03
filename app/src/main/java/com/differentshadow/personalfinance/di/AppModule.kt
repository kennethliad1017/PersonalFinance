package com.differentshadow.personalfinance.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.differentshadow.personalfinance.UserPreferences
import com.differentshadow.personalfinance.data.UserPreferencesSerializer
import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepository
import com.differentshadow.personalfinance.data.repository.billreminder.BillReminderRepositoryBase
import com.differentshadow.personalfinance.data.repository.budget.BudgetRepository
import com.differentshadow.personalfinance.data.repository.budget.BudgetRepositoryBase
import com.differentshadow.personalfinance.data.repository.savinggoal.SavingGoalRepository
import com.differentshadow.personalfinance.data.repository.savinggoal.SavingGoalRepositoryBase
import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepository
import com.differentshadow.personalfinance.data.repository.transaction.TransactionRepositoryBase
import com.differentshadow.personalfinance.data.source.PersonalFinanceDatabase
import com.differentshadow.personalfinance.domain.usecase.FormatDateToStringUseCase
import com.differentshadow.personalfinance.domain.usecase.FormatStringToDateUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.CreateBillReminderUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.DeleteBillReminderUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.GetBillReminderByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.GetBillRemindersUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.RemoveBillReminderUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.UpdateBillReminderByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.billreminder.UpdateBillReminderUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.CreateBudgetUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.DeleteBudgetUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.GetBudgetByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.GetBudgetsUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.RemoveBudgetUseCase
import com.differentshadow.personalfinance.domain.usecase.budget.UpdateBudgetUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.CreateGoalUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.DeleteGoalUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.GetGoalByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.GetGoalsOverviewUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.GetGoalsUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.RemoveGoalUseCase
import com.differentshadow.personalfinance.domain.usecase.savinggoal.UpdateGoalUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.CreateTransactionUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.DeleteTransactionUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetCurrentExpensesUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetLatestTransactionsUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionByIdUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionByIntervalUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.GetTransactionsUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.RemoveTransactionUseCase
import com.differentshadow.personalfinance.domain.usecase.transaction.UpdateTransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalRepository

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class IoDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MainDispatcher

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DefaultDispatcher


    @Singleton
    @Provides
    fun provideFormatDateUseCase() = FormatDateToStringUseCase()


    @Singleton
    @Provides
    fun providePersonalFinanceDatabase(@ApplicationContext context: Context) =
        PersonalFinanceDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideBudgetRepository(financeDatabase: PersonalFinanceDatabase): BudgetRepositoryBase =
        BudgetRepository(financeDatabase.financeDao())

    @Provides
    @Singleton
    fun provideBillReminderRepository(financeDatabase: PersonalFinanceDatabase): BillReminderRepositoryBase =
        BillReminderRepository(financeDatabase.financeDao())

    @Provides
    @Singleton
    fun provideSavingGoalRepository(financeDatabase: PersonalFinanceDatabase): SavingGoalRepositoryBase =
        SavingGoalRepository(financeDatabase.financeDao())

    @Provides
    @Singleton
    fun provideTransactionRepository(financeDatabase: PersonalFinanceDatabase): TransactionRepositoryBase =
        TransactionRepository(financeDatabase.financeDao())


    @Provides
    @Singleton
    fun provideCreateBillReminderUseCase(
        repository: BillReminderRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = CreateBillReminderUseCase(repository, ioDispatcher)


    @Provides
    @Singleton
    fun provideGetBillRemindersUseCase(
        repository: BillReminderRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetBillRemindersUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetBillReminderByIdUseCase(
        repository: BillReminderRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetBillReminderByIdUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideUpdateBillReminderUseCase(
        repository: BillReminderRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = UpdateBillReminderUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideUpdateBillReminderByIdUseCase(
        repository: BillReminderRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = UpdateBillReminderByIdUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideDeleteBillReminderUseCase(
        repository: BillReminderRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = DeleteBillReminderUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideRemoveBillReminderUseCase(
        repository: BillReminderRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = RemoveBillReminderUseCase(repository, ioDispatcher)


    @Provides
    @Singleton
    fun providesCreateBudgetUseCase(
        repository: BudgetRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = CreateBudgetUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetBudgetsUseCase(
        repository: BudgetRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetBudgetsUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetBudgetByIdUseCase(
        repository: BudgetRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetBudgetByIdUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideUpdateBudgetUseCase(
        repository: BudgetRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = UpdateBudgetUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideDeleteBudgetUseCase(
        repository: BudgetRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = DeleteBudgetUseCase(repository, ioDispatcher)


    @Provides
    @Singleton
    fun provideRemoveBudgetUseCase(
        repository: BudgetRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = RemoveBudgetUseCase(repository, ioDispatcher)


    @Provides
    @Singleton
    fun provideCreateSavingGoalUseCase(
        repository: SavingGoalRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = CreateGoalUseCase(repository, ioDispatcher)


    @Provides
    @Singleton
    fun provideGetSavingGoalsUseCase(
        repository: SavingGoalRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetGoalsUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetSavingGoalsOverviewUseCase(
        repository: SavingGoalRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetGoalsOverviewUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetSavingGoalByIdUseCase(
        repository: SavingGoalRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetGoalByIdUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetCurrentExpensesUseCase(
        repository: TransactionRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = GetCurrentExpensesUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideUpdateSavingGoalUseCase(
        repository: SavingGoalRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = UpdateGoalUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideDeleteSavingGoalUseCase(
        repository: SavingGoalRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = DeleteGoalUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideRemoveSavingGoalUseCase(
        repository: SavingGoalRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = RemoveGoalUseCase(repository, ioDispatcher)


    @Provides
    @Singleton
    fun providesCreateTransactionUseCase(
        repository: TransactionRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = CreateTransactionUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetTransactionsUseCase(
        repository: TransactionRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetTransactionsUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetLatestTransactionsUseCase(
        repository: TransactionRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetLatestTransactionsUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideGetTransactionByIdUseCase(
        repository: TransactionRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetTransactionByIdUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideTransactionsByInterval(
        repository: TransactionRepositoryBase,
        formatDateToStringUseCase: FormatDateToStringUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = GetTransactionByIntervalUseCase(repository, formatDateToStringUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideUpdateTransactionUseCase(
        repository: TransactionRepositoryBase,
        formatStringToDateUseCase: FormatStringToDateUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = UpdateTransactionUseCase(repository, formatStringToDateUseCase, ioDispatcher)

    @Provides
    @Singleton
    fun provideDeleteTransactionUseCase(
        repository: TransactionRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = DeleteTransactionUseCase(repository, ioDispatcher)


    @Provides
    @Singleton
    fun provideRemoveTransactionUseCase(
        repository: TransactionRepositoryBase,
        ioDispatcher: CoroutineDispatcher
    ) = RemoveTransactionUseCase(repository, ioDispatcher)

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        ioDispatcher: CoroutineDispatcher,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        ) {
            context.dataStoreFile("user_preferences.pb")
        }


    @Singleton
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
}