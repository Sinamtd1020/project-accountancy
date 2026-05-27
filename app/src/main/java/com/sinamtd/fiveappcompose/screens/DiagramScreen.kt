package com.sinamtd.fiveappcompose.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sinamtd.fiveappcompose.utils.TimeRange
import com.sinamtd.fiveappcompose.viewmodel.MonthlyData
import com.sinamtd.fiveappcompose.viewmodel.TransViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagramScreen(viewModel: TransViewModel= hiltViewModel()){


    val chartData by viewModel.chartData.collectAsState()
    val selectedRange by viewModel.selectedTimeRange.collectAsState()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(50.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(bottomEnd =12.dp, bottomStart = 12.dp)), contentAlignment = Alignment.CenterEnd){
                Text("نمودار تراکنش ها", modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            }
            Spacer(Modifier.height(10.dp))
            // رادیو باتن‌ها
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TimeRangeButton(
                    text = "۳ روز",
                    isSelected = selectedRange == TimeRange.DAY,
                    onClick = { viewModel.setTimeRange(TimeRange.DAY) }
                )
                TimeRangeButton(
                    text = "۳ هفته",
                    isSelected = selectedRange == TimeRange.WEEK,
                    onClick = { viewModel.setTimeRange(TimeRange.WEEK) }
                )
                TimeRangeButton(
                    text = "۳ ماه",
                    isSelected = selectedRange == TimeRange.MONTH,
                    onClick = { viewModel.setTimeRange(TimeRange.MONTH) }
                )
            }

            // نمایش نمودار
            if (chartData.isNotEmpty()) {
                SimpleBarChart(monthlyData = chartData)
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("هیچ داده‌ای برای این بازه وجود ندارد.")
                }
            }
        }
    }


@Composable
fun TimeRangeButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Color.Gray))
        Text(text = text, modifier = Modifier.padding(start = 4.dp))
    }
}






@Composable
fun SimpleBarChart(
    monthlyData: List<MonthlyData>,
    modifier: Modifier = Modifier,
    showValues: Boolean = true,
    valueTextSize: Float = 12f,
    maxBarHeightPercent: Float = 0.7f   // درصد ارتفاع ستون‌ها (بقیه فضا برای متن پایین نیست چون متن خارج شده)
) {
    if (monthlyData.isEmpty()) return

    val maxValue = monthlyData.flatMap { listOf(it.income, it.expense) }
        .maxOrNull()?.toFloat() ?: 1f
    if (maxValue == 0f) {
       return
    }

    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(color = Color.Black, fontSize = valueTextSize.sp)

    Column {
        // نمودار (بدون متن پایین)
        Canvas(modifier = modifier
            .fillMaxWidth()
            .height(300.dp)) {
            val chartHeight = size.height  // دیگر نیازی به جدا کردن فضا نیست
            val maxBarHeight = chartHeight * maxBarHeightPercent

            val groupCount = monthlyData.size
            val groupWidth = size.width / groupCount
            val barWidth = groupWidth * 0.35f
            val innerSpacing = groupWidth * 0.1f
            val outerSpacing = groupWidth * 0.2f

            monthlyData.forEachIndexed { index, data ->
                val groupStartX = index * groupWidth + outerSpacing / 2
                val incomeHeight = (data.income.toFloat() / maxValue) * maxBarHeight
                val expenseHeight = (data.expense.toFloat() / maxValue) * maxBarHeight

                // ستون درآمد
                drawRect(
                    color = Color(0xFF4CAF50),
                    topLeft = Offset(groupStartX, chartHeight - incomeHeight),
                    size = Size(barWidth, incomeHeight)
                )
                // ستون هزینه
                drawRect(
                    color = Color(0xFFF44336),
                    topLeft = Offset(groupStartX + barWidth + innerSpacing, chartHeight - expenseHeight),
                    size = Size(barWidth, expenseHeight)
                )

                // نمایش اعداد بالای ستون‌ها (بدون تغییر)
                if (showValues && data.income >= 0) {
                    val valueText = formatNumber(data.income)
                    val textLayout = textMeasurer.measure(valueText, textStyle)
                    val textX = groupStartX + barWidth / 2 - textLayout.size.width / 2
                    var textY = chartHeight - incomeHeight - textLayout.size.height - 2f
                    if (textY < 0) textY = 2f
                    drawText(textLayout, topLeft = Offset(textX, textY))
                }
                if (showValues && data.expense >= 0) {
                    val valueText = formatNumber(data.expense)
                    val textLayout = textMeasurer.measure(valueText, textStyle)
                    val textX = groupStartX + barWidth + innerSpacing + barWidth / 2 - textLayout.size.width / 2
                    var textY = chartHeight - expenseHeight - textLayout.size.height - 2f
                    if (textY < 0) textY = 2f
                    drawText(textLayout, topLeft = Offset(textX, textY))
                }
            }
        }

        // متن‌های پایین نمودار (امروز، دیروز، ... یا نام ماه‌ها)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            monthlyData.forEach { data ->
                Text(
                    text = data.month,
                    fontSize = 12.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
// تابع کمکی برای فرمت اعداد (مثل 1,500,000)
fun formatNumber(amount: Double): String {
return if (amount==0.0)"0"
else NumberFormat.getNumberInstance(Locale.US).format(amount.roundToInt())
}

