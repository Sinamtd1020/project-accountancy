package com.sinamtd.fiveappcompose.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sinamtd.fiveappcompose.R
import com.sinamtd.fiveappcompose.model.data.TransactionType
import com.sinamtd.fiveappcompose.model.data.entity.TransactionEntity
import com.sinamtd.fiveappcompose.navigation.Screens
import com.sinamtd.fiveappcompose.ui.theme.Blue
import com.sinamtd.fiveappcompose.ui.theme.Green
import com.sinamtd.fiveappcompose.ui.theme.GreenLight
import com.sinamtd.fiveappcompose.ui.theme.Orange
import com.sinamtd.fiveappcompose.ui.theme.OrangeLight
import com.sinamtd.fiveappcompose.ui.theme.Purple80
import com.sinamtd.fiveappcompose.ui.theme.PurpleLight
import com.sinamtd.fiveappcompose.ui.theme.Red
import com.sinamtd.fiveappcompose.ui.theme.RedLight
import com.sinamtd.fiveappcompose.viewmodel.TransViewModel
import kotlinx.coroutines.launch
import kotlin.text.isNotEmpty

@Composable
fun HistoryScreen(navController: NavController,viewModel: TransViewModel= hiltViewModel()){
    val tran  by viewModel.trans.collectAsState()
    val message= viewModel.message
    val context= LocalContext.current
    val scope= rememberCoroutineScope()
    val trans=tran.filter { !it.isSettled }
    val trans2=tran.filter { it.isSettled }
    val incomeSearch = remember { mutableStateOf("") }
    val expenseSearch = remember { mutableStateOf("") }
    val debtSearch = remember { mutableStateOf("") }
    val receivableSearch = remember { mutableStateOf("") }
    val settledSearch = remember { mutableStateOf("") }
    var state = remember { mutableStateOf(false) }
    var stateSettled = remember { mutableStateOf(false) }
    var transDelete = remember { mutableStateOf<TransactionEntity?>(null) }
    var transSettled = remember { mutableStateOf<TransactionEntity?>(null) }


        val incomeFilter= if (incomeSearch.value.isBlank()) {trans.filter { it.type== TransactionType.INCOME }}
        else{trans.filter {  it.type== TransactionType.INCOME &&
             it.category.contains(incomeSearch.value, ignoreCase = true) }}
        val expenseFilter= if (expenseSearch.value.isBlank()) {trans.filter { it.type== TransactionType.EXPENSE }}
        else{trans.filter {  it.type== TransactionType.EXPENSE &&
            it.category.contains(expenseSearch.value, ignoreCase = true) }}
        val receivableFilter= if (receivableSearch.value.isBlank()) {trans.filter { it.type== TransactionType.RECEIVABLE }}
        else{trans.filter {  it.type== TransactionType.RECEIVABLE &&
               it.category.contains(receivableSearch.value, ignoreCase = true) }}
        val debtFilter= if (debtSearch.value.isBlank()) {trans.filter { it.type== TransactionType.DEBT }}
        else{trans.filter {  it.type== TransactionType.DEBT
                && it.category.contains(debtSearch.value, ignoreCase = true) }}
        val settledDebt= if (settledSearch.value.isBlank()) {trans2}
        else{trans2.filter { it.category.contains(settledSearch.value, ignoreCase = true) }}

        Column(modifier = Modifier.fillMaxSize()
            .background
                (Brush.verticalGradient(Pair(0.1f,Color(0xFFE9E3F5)),Pair(0.9f, Color.White))).verticalScroll(rememberScrollState())) {
        val tabTitles=listOf("درامد ها"  , "هزینه ها" ,"بدهی ها" ,"طلب ها" ,"تسویه شده ها" )
            var selectTab by remember { mutableStateOf(0) }
            Surface(modifier = Modifier.fillMaxWidth().padding(all = 18.dp), shape = RoundedCornerShape(12.dp))
            { TabRow(selectTab, indicator = {}) {
                tabTitles.forEachIndexed { index, title  ->
                    val isSelected = selectTab==index
                    Tab(selected = isSelected
                        , onClick = { selectTab = index},
                        modifier = Modifier.background(if (isSelected) Color(0xFF33048A) else Color(0xFFE9EAEC))
                        , selectedContentColor = Color.White, unselectedContentColor = Color.Gray, text = {Text(title)},)
                }
            }}
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
            when(selectTab){
                0 -> CustomTextField(incomeSearch)

                1 -> CustomTextField(expenseSearch)

                2 -> CustomTextField(debtSearch)

                3 -> CustomTextField(receivableSearch)

                4 -> CustomTextField(settledSearch)

            }}

            Spacer(Modifier.height(15.dp))
            when(selectTab){
                     0 -> LazyColumn  (modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                         items(incomeFilter.size){
                             val transactions=incomeFilter[it]
                             TransactionItems (transactions, deleteAction = {  transDelete.value =transactions
                                 state.value=true}, updateAction = {navController.navigate(Screens.AddTrans.paramsWithArgs(transactions.id.toString()))},)
                             Spacer(Modifier.width(16.dp))
                             if (state.value){
                                 CustomAlertDialog(state,transDelete,viewModel)
                             }

                         }
                     }

                     1 -> LazyColumn  (modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                         items(expenseFilter.size){
                             val transactions=expenseFilter[it]
                             TransactionItems (transactions, deleteAction = {  transDelete.value =transactions
                                 state.value=true}, updateAction = {navController.navigate(Screens.AddTrans.paramsWithArgs(transactions.id.toString()))},)
                             Spacer(Modifier.width(16.dp))
                             if (state.value){
                                 CustomAlertDialog(state,transDelete,viewModel)
                             }

                         }
                     }

                     2 ->  LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                         items(debtFilter.size){
                             val transactions=debtFilter[it]
                             TransactionItemsDebt  (transactions, deleteAction = {transDelete.value=transactions
                                 state.value=true}
                                 , updateAction = {navController.navigate(Screens.AddTrans.paramsWithArgs(transactions.id.toString()))}, settled = {transSettled.value=transactions
                                     stateSettled.value=true})
                             Spacer(Modifier.width(16.dp))
                             if (state.value){
                                 CustomAlertDialog(state,transDelete,viewModel)
                             }
                             if (stateSettled.value){ CustomAlertDialogSettled(stateSettled,transSettled,viewModel)}
                         }
                     }

                     3 -> LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                         items(receivableFilter.size){
                             val transactions=receivableFilter[it]
                             TransactionItemsDebt  (transactions, deleteAction = {transDelete.value=transactions
                                 state.value=true}
                                 , updateAction = {navController.navigate(Screens.AddTrans.paramsWithArgs(transactions.id.toString()))}, settled = {transSettled.value=transactions
                                     stateSettled.value=true})
                             Spacer(Modifier.width(16.dp))
                             if (state.value){
                                 CustomAlertDialog(state,transDelete,viewModel)
                             }
                             if (stateSettled.value){ CustomAlertDialogSettled(stateSettled,transSettled,viewModel)}
                         }
                     }

                     4 -> LazyColumn (modifier = Modifier.fillMaxWidth().weight(1f).padding(8.dp)) {
                         items(settledDebt.size){
                             val debt =settledDebt[it]
                             TransactionSettledItem(debt)


                         }
                     }



            }






        LaunchedEffect(Unit) {
            message.collect {
                Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            }
        }

    }

}
@Composable
fun CustomTextField(state: MutableState<String>){
    TextField(value = state.value, onValueChange = {state.value=it}, modifier = Modifier.width(250.dp).padding(end = 8.dp)
        .border(width = 1.dp, color = Color.LightGray,shape = RoundedCornerShape(15.dp)),
        shape = RoundedCornerShape(15.dp),
        textStyle = TextStyle(textAlign = TextAlign.End),
        colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
        ),
        placeholder  ={
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                Text (text = "... خوراک, پوشاک, خانه, ماشین",
                    modifier = Modifier.padding(end = 8.dp), fontSize = 12.sp, textAlign = TextAlign.Right, color = Color.Gray)
                Icon(painter = painterResource(R.drawable.search),"", tint = Color.Gray, modifier = Modifier.size(20.dp))
            }}
    )

}

@Composable
fun CustomAlertDialog(state: MutableState<Boolean>,transaction: MutableState<TransactionEntity?>,viewModel: TransViewModel){
    AlertDialog(onDismissRequest = {state.value=false},{Box(modifier = Modifier.width(200.dp), contentAlignment = Alignment.CenterStart)
    {Text("لغو ", modifier = Modifier.clickable{state.value=false}, color = Color.Black)}},
        dismissButton = {Box(modifier = Modifier.width(50.dp), contentAlignment = Alignment.CenterStart)
        {Text("حذف ", modifier = Modifier.clickable{transaction.value?.let { viewModel.deleteByID(it.id) }
            state.value=false
            transaction.value=null }
            , color = Color.Black)}},
        title = {Text("حذف تراکنش", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, color = Color.Black)},
        text = {Text("ایا از حذف تراکنش مطمئن هستید؟",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, color = Color.Black)},
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.White)
}

@Composable
fun CustomAlertDialogSettled(state: MutableState<Boolean>,transaction: MutableState<TransactionEntity?>,viewModel: TransViewModel){
    AlertDialog(onDismissRequest = {state.value=false},{Box(modifier = Modifier.width(200.dp), contentAlignment = Alignment.CenterStart)
    {Text("لغو ", modifier = Modifier.clickable{state.value=false}, color = Color.Black)}},
        dismissButton = {Box(modifier = Modifier.width(50.dp), contentAlignment = Alignment.CenterStart)
        {Text("تسویه ", modifier = Modifier.clickable{transaction.value?.let { viewModel.settledDebtsOrReceivables(it) }
            state.value=false
            transaction.value=null }
            , color = Color.Black)}},
        title = {Text(" تسویه تراکنش", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, color = Color.Black)},
        text = {Text("ایا از تسویه تراکنش مطمئن هستید؟",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End, color = Color.Black)},
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.White)
}


@Composable
fun TransactionItems (transactionEntity: TransactionEntity,deleteAction :()-> Unit, updateAction: ()-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // ---- محتوای اصلی (کل کارت به جز نوار) ----
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 12.dp) // فاصله از دو طرف
            ) {
                // سطر اول: نوع تراکنش و مبلغ
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = if (transactionEntity.type == TransactionType.RECEIVABLE || transactionEntity.type == TransactionType.INCOME) "تومان + "
                            else " تومان - ",
                            modifier = Modifier.padding(start = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        )
                        Text(
                            text = formatPrice(transactionEntity.amount),
                            modifier = Modifier,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = if(transactionEntity.type== TransactionType.INCOME) "نوع تراکنش : در امد"
                        else  "نوع تراکنش : هزینه",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 6.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // دسته‌بندی
                if (transactionEntity.category.isNotEmpty()) {
                    Text(
                        text = "بابت :  ${transactionEntity.category}",
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // توضیحات

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(8.dp))
                    Icon(if (transactionEntity.type== TransactionType.INCOME)Icons.Outlined.MonetizationOn
                        else Icons.Outlined.ShoppingCart,"", modifier = Modifier.width(35.dp).height(35.dp).padding(bottom = 3.dp)
                            .background(color =if (transactionEntity.type== TransactionType.INCOME)GreenLight
                                else RedLight,
                        shape = RoundedCornerShape(4.dp)).padding(all = 2.dp), tint =Color.White)
                    val descriptionText = if (transactionEntity.description.isEmpty()) {
                        "توضیحات : بدون توضیح"
                    } else {
                        "توضیحات : ${transactionEntity.description}"
                    }
                    Text(
                        text = descriptionText,
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp, start = 4.dp),
                        style = TextStyle(textDirection = TextDirection.Rtl),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.DarkGray,
                        softWrap = false
                    )}

                Spacer(modifier = Modifier.height(5.dp))// تاریخ و ساعت
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                        Icon(Icons.Outlined.Delete,"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 4.dp).size(22.dp).clickable{deleteAction()})
                        Icon(Icons.Outlined.Create,"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 4.dp).size(22.dp).clickable{updateAction()})

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                    Text(
                        text = transactionEntity.dateShow,
                        Modifier.padding(end = 5.dp, bottom = 7.dp),
                        fontSize = 10.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = transactionEntity.hour,
                        Modifier.padding(end = 8.dp, bottom = 7.dp),
                        fontSize = 10.sp,
                        color = Color.DarkGray
                    )}

                }

            }

            // ---- نوار رنگی در سمت راست (با استفاده از Alignment.CenterEnd) ----
            Box(
                modifier = Modifier
                    .fillMaxHeight()          // از بالا تا پایین
                    .width(6.dp)              // عرض ۵ دی‌پی
                    .align(Alignment.CenterEnd) // قرارگیری در سمت راست (مستقل از RTL)
                    .background(
                        color = if (transactionEntity.type== TransactionType.INCOME) Green
                        else Red
                    )
            )
        }
    }
}

@Composable
fun TransactionItemsDebt (transactionEntity: TransactionEntity,deleteAction :()-> Unit, updateAction: ()-> Unit,settled:()-> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // ---- محتوای اصلی (کل کارت به جز نوار) ----
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 12.dp) // فاصله از دو طرف
            ) {
                // سطر اول: نوع تراکنش و مبلغ
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = if (transactionEntity.type == TransactionType.RECEIVABLE || transactionEntity.type == TransactionType.INCOME) "تومان + "
                            else " تومان - ",
                            modifier = Modifier.padding(start = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        )
                        Text(
                            text = formatPrice(transactionEntity.amount),
                            modifier = Modifier,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = if(transactionEntity.type== TransactionType.RECEIVABLE) "نوع تراکنش : طلب"
                        else  "نوع تراکنش : بدهی",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 6.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // دسته‌بندی
                if (transactionEntity.category.isNotEmpty()) {
                    Text(
                        text = "بابت :  ${transactionEntity.category}",
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // توضیحات

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(8.dp))
                    Icon(if (transactionEntity.type== TransactionType.RECEIVABLE)Icons.Outlined.HourglassTop
                    else Icons.Outlined.Warning,"", modifier = Modifier.width(35.dp).height(35.dp).padding(bottom = 3.dp)
                        .background(color =if (transactionEntity.type== TransactionType.RECEIVABLE)Purple80
                        else OrangeLight,
                            shape = RoundedCornerShape(4.dp)).padding(all = 2.dp), tint =Color.White)
                    val descriptionText = if (transactionEntity.description.isEmpty()) {
                        "توضیحات : بدون توضیح"
                    } else {
                        "توضیحات : ${transactionEntity.description}"
                    }
                    Text(
                        text = descriptionText,
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp, start = 4.dp),
                        style = TextStyle(textDirection = TextDirection.Rtl),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.DarkGray,
                        softWrap = false
                    )}

                Spacer(modifier = Modifier.height(5.dp))// تاریخ و ساعت
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(onClick = { settled() }) {Text(if (transactionEntity.type== TransactionType.DEBT) "تسویه بدهی "  else {"تسویه طلب"}, color = Color.Black, fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp)) }

                    Icon(Icons.Outlined.Delete,"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 4.dp).size(22.dp).clickable{deleteAction()})
                    Icon(Icons.Outlined.Create,"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 4.dp).size(22.dp).clickable{updateAction()})

                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End){
                        Text(
                            text = transactionEntity.dateShow,
                            Modifier.padding(end = 5.dp, bottom = 7.dp),
                            fontSize = 10.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = transactionEntity.hour,
                            Modifier.padding(end = 8.dp, bottom = 7.dp),
                            fontSize = 10.sp,
                            color = Color.DarkGray
                        )}

                }

            }

            // ---- نوار رنگی در سمت راست (با استفاده از Alignment.CenterEnd) ----
            Box(
                modifier = Modifier
                    .fillMaxHeight()          // از بالا تا پایین
                    .width(6.dp)              // عرض ۵ دی‌پی
                    .align(Alignment.CenterEnd) // قرارگیری در سمت راست (مستقل از RTL)
                    .background(
                        color = if (transactionEntity.type== TransactionType.RECEIVABLE) Blue
                        else Orange
                    )
            )
        }
    }
}

@Composable
fun TransactionSettledItem(transactionEntity: TransactionEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // ---- محتوای اصلی (کل کارت به جز نوار) ----
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 12.dp) // فاصله از دو طرف
            ) {
                // سطر اول: نوع تراکنش و مبلغ
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Text(
                            text = if (transactionEntity.type == TransactionType.RECEIVABLE || transactionEntity.type == TransactionType.INCOME) "تومان + "
                            else " تومان - ",
                            modifier = Modifier.padding(start = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        )
                        Text(
                            text = formatPrice(transactionEntity.amount),
                            modifier = Modifier,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        )
                    }

                    Text(
                        text = if(transactionEntity.type== TransactionType.RECEIVABLE) "نوع تراکنش : طلب تسویه شده"
                        else  "نوع تراکنش : بدهی تسویه شده",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 6.dp),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                // دسته‌بندی
                if (transactionEntity.category.isNotEmpty()) {
                    Text(
                        text = "بابت :  ${transactionEntity.category}",
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // توضیحات

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Outlined.CheckCircle,"", modifier = Modifier.width(35.dp).height(35.dp).background(color =Green ,
                        shape = RoundedCornerShape(4.dp)).padding(vertical = 4.dp, horizontal = 2.dp), tint =Color.White)
                    val descriptionText = if (transactionEntity.description.isEmpty()) {
                        "توضیحات : بدون توضیح"
                    } else {
                        "توضیحات : ${transactionEntity.description}"
                    }
                    Text(
                        text = descriptionText,
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp, start = 4.dp),
                        style = TextStyle(textDirection = TextDirection.Rtl),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.SansSerif,
                        color = Color.DarkGray,
                        softWrap = false
                    )}

                Spacer(modifier = Modifier.height(5.dp))// تاریخ و ساعت
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = transactionEntity.dateShow,
                        Modifier.padding(end = 5.dp, bottom = 7.dp),
                        fontSize = 10.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = transactionEntity.hour,
                        Modifier.padding(end = 8.dp, bottom = 7.dp),
                        fontSize = 10.sp,
                        color = Color.DarkGray
                    )
                }
            }

            // ---- نوار رنگی در سمت راست (با استفاده از Alignment.CenterEnd) ----
            Box(
                modifier = Modifier
                    .fillMaxHeight()          // از بالا تا پایین
                    .width(6.dp)              // عرض ۵ دی‌پی
                    .align(Alignment.CenterEnd) // قرارگیری در سمت راست (مستقل از RTL)
                    .background(
                        color = Color.Gray
                    )
            )
        }
    }
}

