package com.sinamtd.fiveappcompose.viewmodel

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sinamtd.fiveappcompose.model.data.TransactionType
import com.sinamtd.fiveappcompose.model.data.entity.TransactionEntity
import com.sinamtd.fiveappcompose.repository.TransRepository
import com.sinamtd.fiveappcompose.utils.PersianDate
import com.sinamtd.fiveappcompose.utils.TimeRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransViewModel @Inject constructor(private val transRepository: TransRepository,
    private val nowPersian: PersianDate): ViewModel() {


    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message
    private val _trans = MutableStateFlow(listOf<TransactionEntity>())
    val trans: StateFlow<List<TransactionEntity>> = _trans


    val totalDebt: Flow<Long> =
        transRepository.totalByType(TransactionType.DEBT).map { it ?: 0L }


    val totalReceivables: Flow<Long> =
        transRepository.totalByType(TransactionType.RECEIVABLE).map { it ?: 0L }


    val totalIncome: Flow<Long> =
        transRepository.totalByType(TransactionType.INCOME).map { it ?: 0L }
    val totalExpense: Flow<Long> =
        transRepository.totalByType(TransactionType.EXPENSE).map { it ?: 0L }

    val totalMoney: Flow<Long> = combine(totalIncome, totalExpense) { income, expense ->
        income - expense
    }


    init {
        getAll()


    }

    fun getAll() {
        viewModelScope.launch {
            transRepository.getAllTrans().collectLatest {
                _trans.value = it
            }
        }
    }


    fun deleteByID(id: Long) {
        viewModelScope.launch {
            val result=transRepository.deleteTransById(id)
            _message.emit(if (result) "تراکنش با موفقیت حذف شد"  else "حذف کردن تراکنش باخطا مواجه شد")
        }
    }

    fun getByID(id: Long , onResult:(trans:TransactionEntity) -> Unit) {
        viewModelScope.launch {
            val result=transRepository.getTransById(id)
            result?.let {
                onResult(it)
            }
        }
    }

    fun update(id: Long,category: String,amount: Long,type: TransactionType,desc: String,date: Long,hour: String,dateShow: String,month: Int) {
        viewModelScope.launch {

            val result=transRepository.updateTrans(id,category,amount,type,desc,date,hour,dateShow,month)
            _message.emit(if (result) "تراکنش با موفقیت بروزرسانی شد"  else "بروزرسانی با خطا مواجه شد")
        }
    }

    fun insert(category: String, amount: Long, type: TransactionType, desc: String, date:Long) {
        viewModelScope.launch {
            val result=transRepository.insertTrans(category, amount, type, desc, date)
            _message.emit(if (result) "تراکنش با موفقیت اضافه شد"  else "اضافه کردن تراکنش باخطا مواجه شد")
        }
    }

    fun settledDebtsOrReceivables(debtsOrReceivables: TransactionEntity){
        viewModelScope.launch {
            transRepository.settledDebtsOrReceivables(debtsOrReceivables)
        }
    }





    // بازه زمانی انتخاب شده (پیش‌فرض ماه)
    private val _selectedTimeRange = MutableStateFlow(TimeRange.MONTH)
    val selectedTimeRange: StateFlow<TimeRange> = _selectedTimeRange.asStateFlow()


    fun setTimeRange(range: TimeRange) {
        _selectedTimeRange.value = range
    }

    // داده‌های نمودار بر اساس بازه انتخابی
    val chartData: StateFlow<List<MonthlyData>> = combine(
        trans,
        _selectedTimeRange
    ) { transactions, range ->
        val startDate = getStartDateForRange(range)
        val filtered = transactions.filter { it.date >= startDate }
        when (range) {
            TimeRange.DAY -> groupByDayForLast3Days(filtered)
            TimeRange.WEEK -> groupByWeekForLast3Weeks(filtered)
            TimeRange.MONTH -> groupByMonthForLast3Months(filtered)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )



    // تعیین startDate بر اساس بازه (سه روز، سه هفته، سه ماه)
    private fun getStartDateForRange(range: TimeRange): Long {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply { timeInMillis = now }
        return when (range) {
            TimeRange.DAY -> {
                calendar.add(Calendar.DAY_OF_YEAR, -3)
                calendar.timeInMillis
            }
            TimeRange.WEEK -> {
                calendar.add(Calendar.WEEK_OF_YEAR, -3)
                calendar.timeInMillis
            }
            TimeRange.MONTH -> {
                calendar.add(Calendar.MONTH, -3)
                calendar.timeInMillis
            }
        }
    }

    // ================== گروه‌بندی سه روز (با برچسب امروز، دیروز، پریروز) ==================
    private fun groupByDayForLast3Days(transactions: List<TransactionEntity>): List<MonthlyData> {
        val now = System.currentTimeMillis()
        val result = mutableListOf<MonthlyData>()
        for (i in 0..2) {
            val targetDay = now - (i * 24 * 60 * 60 * 1000L)
            val startOfDay = getStartOfDay(targetDay)
            val endOfDay = getEndOfDay(targetDay)
            val dayTransactions = transactions.filter { it.date in startOfDay..endOfDay }
            val label = when (i) {
                0 -> "امروز"
                1 -> "دیروز"
                2 -> "پریروز"
                else -> ""
            }
            result.add(
                MonthlyData(
                    month = label,
                    income = dayTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }.toDouble(),
                    expense = dayTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }.toDouble()
                )
            )
        }
        return result
    }

    // ================== گروه‌بندی سه هفته ==================
    private fun groupByWeekForLast3Weeks(transactions: List<TransactionEntity>): List<MonthlyData> {
        val now = System.currentTimeMillis()
        val result = mutableListOf<MonthlyData>()
        for (i in 0..2) {
            val targetWeek = now - (i * 7 * 24 * 60 * 60 * 1000L)
            val startOfWeek = getStartOfWeek(targetWeek)
            val endOfWeek = getEndOfWeek(targetWeek)
            val weekTransactions = transactions.filter { it.date in startOfWeek..endOfWeek }
            val label = when (i) {
                0 -> "این هفته"
                1 -> "هفته گذشته"
                2 -> "دو هفته پیش"
                else -> ""
            }
            result.add(
                MonthlyData(
                    month = label,
                    income = weekTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }.toDouble(),
                    expense = weekTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }.toDouble()
                )
            )
        }
        return result
    }


    private fun getLastThreeMonths(): List<Int> {
        var currentYear = nowPersian.year
        var currentMonth = nowPersian.month
        val result = mutableListOf<Int>()
        for (i in 0..2) {
            result.add(currentYear * 100 + currentMonth)
            currentMonth--
            if (currentMonth == 0) {
                currentMonth = 12
                currentYear--
            }
        }
        return result
    }

    private fun groupByMonthForLast3Months(transactions: List<TransactionEntity>): List<MonthlyData> {
        val targetMonths = getLastThreeMonths()
        val grouped = transactions.groupBy { it.monthYear }
        return targetMonths.map { key ->
            val list = grouped[key] ?: emptyList()
            val year = key / 100
            val month = key % 100
            MonthlyData(
                month = "${PersianDate.getMonthName(month)} $year",
                income = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }.toDouble(),
                expense = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }.toDouble()
            )
        }
    }

    // توابع کمکی برای شروع و پایان روز
    private fun getStartOfDay(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfDay(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }

    // شروع هفته (شنبه برای ایران)
    private fun getStartOfWeek(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        cal.firstDayOfWeek = Calendar.SATURDAY
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getEndOfWeek(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply { timeInMillis = timestamp }
        cal.firstDayOfWeek = Calendar.SATURDAY
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek + 6)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.timeInMillis
    }


}



data class MonthlyData(val month: String, val income: Double, val expense: Double)








