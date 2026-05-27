package com.sinamtd.fiveappcompose.utils

import androidx.room.TypeConverter
import com.sinamtd.fiveappcompose.model.data.TransactionType

class Convertor {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): Int{
        return type.ordinal
    }

    @TypeConverter
    fun toTransactionType(value: Int): TransactionType{
        return TransactionType.entries[value]
    }
}