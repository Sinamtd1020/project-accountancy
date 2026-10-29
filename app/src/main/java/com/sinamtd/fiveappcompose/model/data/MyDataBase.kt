package com.sinamtd.fiveappcompose.model.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sinamtd.fiveappcompose.model.data.dao.TransactionDao
import com.sinamtd.fiveappcompose.model.data.entity.TransactionEntity
import com.sinamtd.fiveappcompose.utils.Convertor

@Database(entities = [TransactionEntity::class],
          version = MyDataBase.VERSION,
          exportSchema = false)
@TypeConverters(Convertor::class)


    abstract class MyDataBase: RoomDatabase() {
        companion object {

            const val DB_NAME = "transaction_db"
            const val TABLE_NAME = "trans"
            const val VERSION = 1
        }
    abstract fun transactionDao(): TransactionDao
    }
