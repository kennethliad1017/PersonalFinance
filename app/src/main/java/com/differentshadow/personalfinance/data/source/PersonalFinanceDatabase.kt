package com.differentshadow.personalfinance.data.source

import android.content.Context
//import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Budget::class, SavingGoal::class, BillReminder::class, Transaction::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class PersonalFinanceDatabase: RoomDatabase() {
    abstract fun financeDao(): FinanceDao

    companion object {

        @Volatile
        private var INSTANCE: PersonalFinanceDatabase? = null

        fun getDatabase(context: Context): PersonalFinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, PersonalFinanceDatabase::class.java, "finance_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}