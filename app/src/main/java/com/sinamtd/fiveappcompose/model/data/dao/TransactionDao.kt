package com.sinamtd.fiveappcompose.model.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sinamtd.fiveappcompose.model.data.MyDataBase
import com.sinamtd.fiveappcompose.model.data.TransactionType
import com.sinamtd.fiveappcompose.model.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTrans(transactionEntity: TransactionEntity): Long

    @Delete
    suspend fun deleteTrans(transactionEntity: TransactionEntity)

    @Update
    suspend fun updateTrans(transactionEntity: TransactionEntity)


    @Query
        (" SELECT * FROM  ${MyDataBase.TABLE_NAME} ORDER BY date DESC")
         fun getTrans(): Flow<List<TransactionEntity>>

    @Query
        ("SELECT * FROM ${MyDataBase.TABLE_NAME} WHERE id = :id")
         suspend fun getTransById(id: Long): TransactionEntity
    @Query
        ("DELETE  FROM ${MyDataBase.TABLE_NAME} WHERE id = :id")
         suspend fun deleteTransById(id: Long)

    @Query
        (" SELECT SUM(amount) FROM ${MyDataBase.TABLE_NAME} WHERE type= :type AND isSettled= 0 ")
         fun getTotalByType(type: TransactionType): Flow<Long?>

    @Query
        (" SELECT * FROM ${MyDataBase.TABLE_NAME} WHERE (type= :type) AND isSettled=0 ORDER BY date DESC ")
         fun getUnsettledDebtsOrReceivables(type: TransactionType): Flow<List<TransactionEntity>>


}