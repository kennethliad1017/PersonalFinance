package com.differentshadow.personalfinance.data.source

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "merchant_name") val merchantName: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "total") val total: Double,
    @ColumnInfo(name = "is_deleted") val isDeleted: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis(),)