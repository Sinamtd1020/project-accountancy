package com.sinamtd.fiveappcompose.model.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sinamtd.fiveappcompose.model.data.MyDataBase
import com.sinamtd.fiveappcompose.model.data.TransactionType

@Entity(tableName = MyDataBase.TABLE_NAME)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0,
    val type: TransactionType,
    val category: String,
    val amount: Long,
    val description: String ="",
    val date: Long,
    val dateShow: String,
    val monthYear: Int=0,
    val hour: String,
    val isSettled: Boolean =false
)
