package com.sinamtd.fiveappcompose.repository

import android.util.Log
import com.sinamtd.fiveappcompose.model.data.TransactionType
import com.sinamtd.fiveappcompose.model.data.dao.TransactionDao
import com.sinamtd.fiveappcompose.model.data.entity.TransactionEntity
import com.sinamtd.fiveappcompose.utils.PersianDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransRepository @Inject constructor(private val transactionDao: TransactionDao,
    private val persian: PersianDate) {
    fun getHour(): String{
        val hour=persian.hour
        val minute=persian.min

        return buildString {
        append(hour)
        append(" : ")
        append(minute)
    }
    }
    fun getTime(): String{

        return "${persian.year}/${persian.month}/${persian.day}"
    }
    fun getMonth(): Int{
        val year=persian.year
        val month=persian.month

        val yearMonth=year*100+month
        return yearMonth

    }

    fun getAllTrans()=transactionDao.getTrans()

    suspend fun insertTrans(category: String,amount: Long,type: TransactionType,desc: String,date: Long)=
        try {
            val trans= TransactionEntity(type=type, category = category, amount = amount, description = desc, date = date, hour = getHour(), dateShow = getTime(), monthYear = getMonth())
            val result=transactionDao.insertTrans(trans)
            result>0
        }
            catch (e:Exception){
            Log.i("error",e.message.toString())
            false
        }

    suspend fun updateTrans(id: Long,category: String,amount: Long,type: TransactionType,desc: String,date: Long,hour: String,dateShow: String,month: Int)=
        try {
            val trans= TransactionEntity(id = id, type=type, category = category, amount = amount, description = desc, date = date, hour = hour, dateShow = dateShow, monthYear = month)
            transactionDao.updateTrans(trans)
            true

        }
        catch (e: Exception){
            Log.i("error",e.message.toString())
            false
        }

    suspend fun deleteTransById(id: Long)=
        try {
            transactionDao.deleteTransById(id)
            true

        }
        catch (e: Exception){
            Log.i("error",e.message.toString())
            false
        }

    suspend fun getTransById(id: Long)= transactionDao.getTransById(id)

    fun totalByType(type: TransactionType): Flow<Long?> = transactionDao.getTotalByType(type)

    suspend fun settledDebtsOrReceivables(debtsOrReceivables: TransactionEntity){
         require(debtsOrReceivables.type== TransactionType.DEBT || debtsOrReceivables.type== TransactionType.RECEIVABLE)

        val  settlementType= when(debtsOrReceivables.type){
            TransactionType.DEBT -> TransactionType.EXPENSE
            TransactionType.RECEIVABLE-> TransactionType.INCOME
            else -> return
        }




           val  settlementTrans= TransactionEntity(
            amount = debtsOrReceivables.amount
            , type = settlementType
            , category = debtsOrReceivables.category,
            description =if (debtsOrReceivables.type== TransactionType.DEBT)"تسویه  بدهی ${debtsOrReceivables.description}  " else " تسویه  طلب ${debtsOrReceivables.description}",
            date = System.currentTimeMillis(),
            hour = getHour(),
            dateShow = getTime(),
            monthYear =getMonth() ,
            isSettled = false)



            transactionDao.insertTrans(settlementTrans)
            val update = debtsOrReceivables.copy(isSettled = true)
            transactionDao.updateTrans(update)


    }





}