package com.sinamtd.fiveappcompose.utils




import java.util.Calendar
import java.util.GregorianCalendar

object JalaliCalendar {

    fun toJalali(timestamp: Long): Triple<Int, Int, Int> {
        val cal = GregorianCalendar.getInstance().apply { timeInMillis = timestamp }
        var gy = cal.get(Calendar.YEAR)
        val gm = cal.get(Calendar.MONTH) + 1
        val gd = cal.get(Calendar.DAY_OF_MONTH)

        // روزهای ماه‌های میلادی
        val gDayInMonth = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
        var gy2 = if (gm > 2) gy + 1 else gy
        var days = 355666 + (365 * gy) + (gy2 / 4) - (gy2 / 100) + (gy2 / 400) + gDayInMonth[gm - 1] + gd - 1
        var jy = -1
        var jm = -1
        var jd = -1

        for (jyTest in 1300..1500) {
            val daysInYear = if (isLeapYear(jyTest)) 366 else 365
            if (days < daysInYear) {
                jy = jyTest
                break
            }
            days -= daysInYear
        }
        if (jy == -1) return Triple(1300, 1, 1)

        val jMonthDays = if (isLeapYear(jy))
            intArrayOf(0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30)
        else
            intArrayOf(0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

        for (jmTest in 1..12) {
            if (days < jMonthDays[jmTest]) {
                jm = jmTest
                jd = days + 1
                break
            }
            days -= jMonthDays[jmTest]
        }
        return Triple(jy, jm, jd)
    }

    fun isLeapYear(jalaliYear: Int): Boolean {
        val rem = (jalaliYear - 1348) % 33
        return rem in setOf(1, 5, 9, 13, 17, 22, 26, 30)
    }

    fun getJalaliYearMonth(timestamp: Long): Pair<Int, Int> {
        val (y, m, _) = toJalali(timestamp)
        return Pair(y, m)
    }

    fun getJalaliMonthName(month: Int): String = when (month) {
        1 -> "فروردین"
        2 -> "اردیبهشت"
        3 -> "خرداد"
        4 -> "تیر"
        5 -> "مرداد"
        6 -> "شهریور"
        7 -> "مهر"
        8 -> "آبان"
        9 -> "آذر"
        10 -> "دی"
        11 -> "بهمن"
        12 -> "اسفند"
        else -> ""
    }

    fun formatJalaliDate(timestamp: Long, separator: String = "/"): String {
        val (y, m, d) = toJalali(timestamp)
        return "$y${separator}${m.toString().padStart(2, '0')}${separator}${d.toString().padStart(2, '0')}"
    }
}