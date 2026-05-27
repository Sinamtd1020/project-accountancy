package com.sinamtd.fiveappcompose.screens

import android.graphics.Paint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onVisibilityChangedNode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sinamtd.fiveappcompose.model.data.TransactionType
import com.sinamtd.fiveappcompose.model.data.entity.TransactionEntity
import com.sinamtd.fiveappcompose.navigation.Screens
import com.sinamtd.fiveappcompose.utils.JalaliCalendar
import com.sinamtd.fiveappcompose.viewmodel.TransViewModel
import java.nio.file.WatchEvent
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun MainScreen(navController: NavController, viewModel:TransViewModel = hiltViewModel()){

    val transaction by viewModel.trans.collectAsState()
    val transaction2=transaction.filter { !it.isSettled }
    val transactions =transaction2.take(5)
    val message= viewModel.message
    val context= LocalContext.current
    val balance by viewModel.totalMoney.collectAsState(initial = 0L)
    val debt by viewModel.totalDebt.collectAsState(initial = 0L)
    val receivable1 by viewModel.totalReceivables.collectAsState(initial = 0L)






    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Box(modifier = Modifier.fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(bottomEnd =12.dp, bottomStart = 12.dp)), contentAlignment = Alignment.CenterEnd){
            Text("مدیریت حساب شخصی", modifier = Modifier.padding(end = 10.dp, bottom = 10.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }


        Spacer(Modifier.height(35.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).align(alignment = Alignment.CenterHorizontally)
            , colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            , shape = RoundedCornerShape(12.dp)
            , elevation = CardDefaults.cardElevation(12.dp)) {
              Column(modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalAlignment =Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center ) {
              Text(text = ": موجودی فعلی ", fontSize = 20.sp, color = Color(0xFF616161))
              Spacer(Modifier.height(10.dp))
              Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
              ){
                  Text(text =  " تومان", fontSize = 20.sp, color = Color(0xFF212121))
                  Text(text =  formatPrice(balance), fontSize = 20.sp, color = Color(0xFF212121))}
          }
        }
        Spacer(Modifier.height(25.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Card(modifier = Modifier.weight(0.5f).padding(horizontal = 8.dp)
                , colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E0))
                , shape = RoundedCornerShape(12.dp)
                , elevation = CardDefaults.cardElevation(12.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalAlignment =Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center ) {
                    Text(text = ":  مجموع طلب ها ", fontSize = 20.sp, color = Color(0xFF2E7D32))
                    Spacer(Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ){
                        Text(text =  " تومان", fontSize = 20.sp, color = Color(0xFF388E3C))
                        Text(text =  formatPrice(receivable1), fontSize = 20.sp, color = (Color(0xFF388E3C)))}
                }
            }
            Card(modifier = Modifier.weight(0.5f).padding(horizontal = 8.dp)
                , colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                , shape = RoundedCornerShape(12.dp)
                , elevation = CardDefaults.cardElevation(12.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalAlignment =Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center ) {
                    Text(text = ":  مجموع بدهی ها ", fontSize = 20.sp,  color = Color(0xFFC62828))
                    Spacer(Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ){
                        Text(text =  " تومان", fontSize = 20.sp, color = Color(0xFFD32F2F))
                        Text(text =  formatPrice(debt), fontSize = 20.sp, color = ( Color(0xFFD32F2F)))}
                }
            }
            Color(0xFF388E3C)
            Color(0xFF2E7D32)
            Color(0xFFD32F2F)

        }


        Spacer(Modifier.height(60.dp))

        Text("تراکنش های اخیر", modifier = Modifier.fillMaxWidth().padding(end = 12.dp), fontSize = 20.sp, textAlign = TextAlign.End)
        Spacer(Modifier.height(15.dp))
        if (transactions.isEmpty()){
            Card(modifier = Modifier.padding(horizontal = 8.dp)
                , colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                , shape = RoundedCornerShape(12.dp)
                , elevation = CardDefaults.cardElevation(12.dp)){
                Text("هیچ تراکنشی ثبت نشده", fontSize = 20.sp, color =Color(0xFF616161), modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), textAlign = TextAlign.Center )
            }

        }
        else{
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
            LazyRow(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.End, reverseLayout = true) {
            items(transactions.size){
                TransactionItem(transactions[it])
                Spacer(Modifier.width(14.dp))
            }
        }}
            TextButton (onClick = { navController.navigate(Screens.History.rout){popUpTo(navController.graph.startDestinationId){saveState=true}
            launchSingleTop=true
            restoreState=true}}){Text("مشاهده ی همه ی تراکنش ها", modifier = Modifier.padding(start = 12.dp), fontSize = 14.sp, color = Color(0xFF616161))  }}
        LaunchedEffect(Unit) {
            message.collect {
                Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            }
        }



    }




    

}
   fun formatPrice(amount: Long):String{
    val formater= NumberFormat.getNumberInstance(java.util.Locale.US)
    return "${formater.format(amount)}  "
}



   @Composable
   fun TransactionItem(transactionEntity: TransactionEntity){

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
        Text(text = "بابت :  ${transactionEntity.category}", modifier = Modifier.fillMaxWidth().padding(end = 10.dp, start = 10.dp), fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.End, color =Color.Black )}
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
    }
    }
