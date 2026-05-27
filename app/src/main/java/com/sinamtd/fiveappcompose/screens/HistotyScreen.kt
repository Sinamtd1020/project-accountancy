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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.sinamtd.fiveappcompose.viewmodel.TransViewModel
import kotlin.text.isNotEmpty

@Composable
fun HistoryScreen(navController: NavController,viewModel: TransViewModel= hiltViewModel()){
    val tran  by viewModel.trans.collectAsState()
    val message= viewModel.message
    val context= LocalContext.current
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

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(bottomEnd =12.dp, bottomStart = 12.dp)), contentAlignment = Alignment.CenterEnd){
            Text("تاریخچه ی همه ی تراکنش ها", modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        Spacer(Modifier.height(15.dp))
        Text("تاریخچه ی درامد ها", modifier = Modifier.fillMaxWidth().padding(end = 14.dp), textAlign = TextAlign.End)
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            CustomTextField(incomeSearch) }
        Spacer(Modifier.height(10.dp))
        CustomLazy(navController = navController, state = state,viewModel,transDelete,incomeFilter)
        Spacer(Modifier.height(15.dp))
        Text("تاریخچه ی هزینه ها", modifier = Modifier.fillMaxWidth().padding(end = 10.dp), textAlign = TextAlign.End)
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            CustomTextField(expenseSearch)
            }
        Spacer(Modifier.height(10.dp))

        CustomLazy(navController,state,viewModel,transDelete,expenseFilter)
        Spacer(Modifier.height(15.dp))
        Text("تاریخچه ی طلب ها", modifier = Modifier.fillMaxWidth().padding(end = 10.dp), textAlign = TextAlign.End)
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
          CustomTextField(receivableSearch)
        }
        Spacer(Modifier.height(10.dp))

        CustomLazySettled(navController,state,stateSettled,viewModel,transDelete,transSettled,receivableFilter)
        Spacer(Modifier.height(15.dp))
        Text("تاریخچه ی بدهی ها", modifier = Modifier.fillMaxWidth().padding(end = 10.dp), textAlign = TextAlign.End)
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { CustomTextField(debtSearch)}
        Spacer(Modifier.height(10.dp))

        CustomLazySettled(navController,state,stateSettled,viewModel,transDelete,transSettled,debtFilter)
        LaunchedEffect(Unit) {
            message.collect {
                Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(Modifier.height(10.dp))
        Text("تاریخچه ی بدهی و طلب های تسویه شده", modifier = Modifier.fillMaxWidth().padding(end = 10.dp), textAlign = TextAlign.End)
        Spacer(Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            CustomTextField(settledSearch)
           }
        Spacer(Modifier.height(10.dp))

        LazyRow(modifier = Modifier.fillMaxWidth().padding(8.dp), reverseLayout = true) {
            items(settledDebt.size){
                val debt =settledDebt[it]
                TransactionSettledItem(debt)
                Spacer(Modifier.width(16.dp))

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
fun CustomLazy(
    navController: NavController,
    state: MutableState<Boolean>,
    viewModel: TransViewModel,
    transaction: MutableState<TransactionEntity?>,
    Fillter: List<TransactionEntity>,
){
    LazyRow(modifier = Modifier.fillMaxWidth().padding(8.dp), reverseLayout = true) {
        items(Fillter.size){
            val transactions=Fillter[it]
            TransactionItems (transactions, deleteAction = {transaction.value=transactions
                state.value=true}, updateAction = {navController.navigate(Screens.AddTrans.paramsWithArgs(transactions.id.toString()))},)
            Spacer(Modifier.width(16.dp))
            if (state.value){
                CustomAlertDialog(state,transaction,viewModel)
            }

        }
    }
}

@Composable
fun CustomLazySettled(
    navController: NavController,
    state: MutableState<Boolean>,
    stateSettled: MutableState<Boolean>,
    viewModel: TransViewModel,
    transaction: MutableState<TransactionEntity?>,
    transactionSettled: MutableState<TransactionEntity?>,
    Fillter: List<TransactionEntity>,
){
    LazyRow(modifier = Modifier.fillMaxWidth().padding(8.dp), reverseLayout = true) {
        items(Fillter.size){
            val transactions=Fillter[it]
            TransactionItemsDebt  (transactions, deleteAction = {transaction.value=transactions
                state.value=true}
                , updateAction = {navController.navigate(Screens.AddTrans.paramsWithArgs(transactions.id.toString()))}, settled = {transactionSettled.value=transactions
                    stateSettled.value=true})
            Spacer(Modifier.width(16.dp))
            if (state.value){
                CustomAlertDialog(state,transaction,viewModel)
            }
            if (stateSettled.value){ CustomAlertDialogSettled(stateSettled,transactionSettled,viewModel)}
        }
    }
}
@Composable
fun TransactionItems (transactionEntity: TransactionEntity,deleteAction :()-> Unit, updateAction: ()-> Unit) {
    Card(modifier = Modifier.width(200.dp) ,
        colors = CardDefaults.cardColors
            (containerColor = if (transactionEntity.type== TransactionType.INCOME)Color(0xFFE8F5E0)
        else{Color(0xFFFFEBEE)}),
        elevation = CardDefaults.cardElevation(12.dp) ) {
        Text(text = if(transactionEntity.type== TransactionType.INCOME){
                  " نوع تراکنش : در امد "}
            else  {"نوع تراکنش : هزینه "}
       , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, top = 10.dp), fontSize = 14.sp, textAlign = TextAlign.End, color =Color.Black )
        Spacer(Modifier.height(5.dp))
        if (transactionEntity.category.isNotEmpty()){
            Text(text = " بابت :  ${transactionEntity.category}", modifier = Modifier.fillMaxWidth().padding(end = 10.dp), fontSize = 14.sp, textAlign = TextAlign.End, color =Color.Black, overflow = TextOverflow.Ellipsis, maxLines = 1 )}
        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
            Text(text = if (transactionEntity.type== TransactionType.RECEIVABLE||transactionEntity.type== TransactionType.INCOME)"تومان + "
            else " تومان - "
                , modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, textAlign = TextAlign.Start, color =Color.Black)
            Text(text =  formatPrice(transactionEntity.amount)
                , modifier = Modifier.padding(start = 1.dp), fontSize = 12.sp, textAlign = TextAlign.Start, color =Color.Black )}
        Spacer(Modifier.height(5.dp))
        if (transactionEntity.description.isEmpty()){
            Text(text =   "توضیحات : بدون توضیح"
                , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp), style = TextStyle(textDirection = TextDirection.Rtl), fontSize = 12.sp,maxLines = 1,overflow = TextOverflow.Ellipsis,fontFamily = FontFamily.SansSerif, color =Color.Black, softWrap = false
            )
        }
        else{
            Text(text =   "توضیحات : ${transactionEntity.description}"
                , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp), style = TextStyle(textDirection = TextDirection.Rtl), fontSize = 12.sp,maxLines = 1,overflow = TextOverflow.Ellipsis,fontFamily = FontFamily.SansSerif, color =Color.Black, softWrap = false
            )}

        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = transactionEntity.dateShow,
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp),
                fontSize = 10.sp,
                color = Color.Black
            )
            Text(
                text = transactionEntity.hour,
                modifier = Modifier.fillMaxWidth().padding(end = 10.dp, bottom = 5.dp),
                fontSize = 10.sp,
                textAlign = TextAlign.End,
                color = Color.Black
            )
        }
        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
         Icon(painter = painterResource(R.drawable.delete),"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 10.dp).size(18.dp).clickable{deleteAction()})
            Icon(painter = painterResource(R.drawable.pencil),"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 10.dp).size(18.dp).clickable{updateAction()})
        }


    }
}

@Composable
fun TransactionItemsDebt (transactionEntity: TransactionEntity,deleteAction :()-> Unit, updateAction: ()-> Unit,settled:()-> Unit) {
    Card(modifier = Modifier.width(200.dp) ,
        colors = CardDefaults.cardColors
            (containerColor = if (transactionEntity.type== TransactionType.RECEIVABLE)Color(0xFFE8F5E0)
        else{Color(0xFFFFEBEE)}),
        elevation = CardDefaults.cardElevation(12.dp) ) {
        Text(text = if(transactionEntity.type== TransactionType.DEBT){
              "نوع تراکنش : بدهی"}
            else   "نوع تراکنش : طلب"
        , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, top = 10.dp), fontSize = 14.sp, textAlign = TextAlign.End, color =Color.Black )
        Spacer(Modifier.height(5.dp))
        if (transactionEntity.category.isNotEmpty()){
            Text(text = " بابت :  ${transactionEntity.category}", modifier = Modifier.fillMaxWidth().padding(end = 10.dp), fontSize = 14.sp, textAlign = TextAlign.End, color =Color.Black, overflow = TextOverflow.Ellipsis, maxLines = 1 )}
        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
            Text(text = if (transactionEntity.type== TransactionType.RECEIVABLE||transactionEntity.type== TransactionType.INCOME)"تومان + "
            else " تومان - "
                , modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, textAlign = TextAlign.Start, color =Color.Black )
            Text(text =  formatPrice(transactionEntity.amount)
                , modifier = Modifier.padding(start = 1.dp), fontSize = 12.sp, textAlign = TextAlign.Start, color =Color.Black )}
        Spacer(Modifier.height(5.dp))
        if (transactionEntity.description.isEmpty()){
            Text(text =   "توضیحات : بدون توضیح"
                , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp), style = TextStyle(textDirection = TextDirection.Rtl), fontSize = 12.sp,maxLines = 1,overflow = TextOverflow.Ellipsis,fontFamily = FontFamily.SansSerif, color =Color.Black, softWrap = false
            )
        }
        else{
            Text(text =   "توضیحات : ${transactionEntity.description}"
                , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp), style = TextStyle(textDirection = TextDirection.Rtl), fontSize = 12.sp,maxLines = 1,overflow = TextOverflow.Ellipsis,fontFamily = FontFamily.SansSerif, color =Color.Black, softWrap = false
            )}

        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = transactionEntity.dateShow,
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp),
                fontSize = 10.sp,
                color = Color.Black
            )
            Text(
                text = transactionEntity.hour,
                modifier = Modifier.fillMaxWidth().padding(end = 10.dp, bottom = 5.dp),
                fontSize = 10.sp,
                textAlign = TextAlign.End,
                color = Color.Black
            )
        }
        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
            Icon(painter = painterResource(R.drawable.delete),"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 10.dp).size(18.dp).clickable{deleteAction()})
            Icon(painter = painterResource(R.drawable.pencil),"", tint = Color.DarkGray, modifier = Modifier.padding(start = 10.dp, bottom = 10.dp).size(18.dp).clickable{updateAction()})
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                TextButton(onClick = { settled() }) {Text(if (transactionEntity.type== TransactionType.DEBT) "تسویه بدهی "  else {"تسویه طلب"}, color = Color.Black, fontSize = 12.sp, modifier = Modifier.padding(end = 6.dp)) }
            }
        }


    }
}

@Composable
fun TransactionSettledItem(transactionEntity: TransactionEntity){
    Card(modifier = Modifier.width(200.dp) ,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(12.dp) ) {
        Text(text = when(transactionEntity.type){
            TransactionType.DEBT -> "نوع تراکنش : بدهی"
            TransactionType.RECEIVABLE -> "نوع تراکنش : طلب"
            TransactionType.INCOME -> " نوع تراکنش : در امد "
            TransactionType.EXPENSE -> "نوع تراکنش : هزینه "
        }, modifier = Modifier.fillMaxWidth().padding(end = 10.dp, top = 10.dp), fontSize = 14.sp, textAlign = TextAlign.End, color =Color.Black )
        Spacer(Modifier.height(5.dp))
        if (transactionEntity.category.isNotEmpty()){
            Text(text = "بابت :  ${transactionEntity.category}", modifier = Modifier.fillMaxWidth().padding(end = 10.dp), fontSize = 14.sp, overflow = TextOverflow.Ellipsis,maxLines = 1, textAlign = TextAlign.End, color =Color.Black )}
        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
            Text(text = if (transactionEntity.type== TransactionType.RECEIVABLE||transactionEntity.type== TransactionType.INCOME)"تومان + "
            else " تومان - "
                , modifier = Modifier.padding(start = 6.dp), fontSize = 12.sp, textAlign = TextAlign.Start, color =Color.Black )
            Text(text =  formatPrice(transactionEntity.amount)
                , modifier = Modifier.padding(start = 1.dp), fontSize = 12.sp, textAlign = TextAlign.Start, color =Color.Black )}
        Spacer(Modifier.height(5.dp))
        if (transactionEntity.description.isEmpty()){
            Text(text =   "توضیحات : بدون توضیح"
                , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp), style = TextStyle(textDirection = TextDirection.Rtl), fontSize = 12.sp,maxLines = 1,overflow = TextOverflow.Ellipsis,fontFamily = FontFamily.SansSerif, color =Color.Black, softWrap = false
            )
        }
        else{
            Text(text =   "توضیحات : ${transactionEntity.description}"
                , modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp), style = TextStyle(textDirection = TextDirection.Rtl), fontSize = 12.sp,maxLines =2,overflow = TextOverflow.Ellipsis,fontFamily = FontFamily.SansSerif, color =Color.Black, softWrap = false
            )}

        Spacer(Modifier.height(5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = transactionEntity.dateShow,
                modifier = Modifier.padding(start = 10.dp, bottom = 5.dp),
                fontSize = 10.sp,
                color = Color.Black
            )
            Text(
                text = transactionEntity.hour,
                modifier = Modifier.fillMaxWidth().padding(end = 10.dp, bottom = 5.dp),
                fontSize = 10.sp,
                textAlign = TextAlign.End,
                color = Color.Black
            )
        }
    }
}