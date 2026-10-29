package com.sinamtd.fiveappcompose.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.sinamtd.fiveappcompose.model.data.TransactionType
import com.sinamtd.fiveappcompose.model.data.entity.TransactionEntity
import com.sinamtd.fiveappcompose.navigation.Screens
import com.sinamtd.fiveappcompose.ui.theme.Green
import com.sinamtd.fiveappcompose.ui.theme.GreenLight
import com.sinamtd.fiveappcompose.ui.theme.Orange
import com.sinamtd.fiveappcompose.ui.theme.OrangeLight
import com.sinamtd.fiveappcompose.ui.theme.Purple40
import com.sinamtd.fiveappcompose.ui.theme.Purple80
import com.sinamtd.fiveappcompose.ui.theme.PurpleLight
import com.sinamtd.fiveappcompose.ui.theme.Red
import com.sinamtd.fiveappcompose.ui.theme.RedLight
import com.sinamtd.fiveappcompose.viewmodel.TransViewModel
import java.text.NumberFormat

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






    Column(modifier = Modifier.fillMaxSize()
        .background(Brush.verticalGradient(Pair(0.1f,Color(0xFFE9E3F5)),Pair(0.9f, Color.White)))) {



        Spacer(Modifier.height(35.dp))
        Card(modifier = Modifier.fillMaxWidth().height(220.dp).padding(all = 10.dp)
            , colors = CardDefaults.cardColors(containerColor = Color(0xFF33048A))
            , shape = RoundedCornerShape(16.dp)
            , elevation = CardDefaults.cardElevation(12.dp)) {
              Column(modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalAlignment =Alignment.CenterHorizontally ) {
              Text(text = ": موجودی فعلی ", modifier = Modifier.fillMaxWidth().padding(end = 12.dp), textAlign = TextAlign.End, fontSize = 12.sp, color = Color.LightGray)
              Spacer(Modifier.height(6.dp))
              Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
              ){
                  Text(text =  " تومان", fontSize = 25.sp, color = Color.White, fontWeight = FontWeight.Bold)
                  Text(text =  formatPrice(balance), modifier = Modifier.padding(start = 3.dp), fontWeight = FontWeight.Bold, fontSize = 25.sp, color = Color.White)}
                  Spacer(Modifier.height(30.dp))
                  Column(modifier = Modifier.fillMaxWidth().background(color = Purple40, shape = RoundedCornerShape(6.dp))) {
              Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                  Text(text = ": مجموع بدهی ها ", modifier = Modifier.padding(end = 14.dp), fontSize = 10.sp, color = Color.LightGray)
                  Text(text = ": مجموع طلب ها ", modifier = Modifier.padding(end = 14.dp), fontSize = 10.sp, color = Color.LightGray)
              }
                  Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                      Row(verticalAlignment = Alignment.CenterVertically
                      ){
                          Text(text =  " تومان", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                          Text(text =  formatPrice(debt), modifier = Modifier.padding(start = 2.dp), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                          Icon(Icons.Outlined.Warning,"",
                              modifier = Modifier.width(25.dp).height(25.dp).background(color = OrangeLight, shape = RoundedCornerShape(4.dp)).padding(4.dp), tint =Color.White)}
                      Row(verticalAlignment = Alignment.CenterVertically
                      ){
                          Text(text =  " تومان", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                          Text(text =  formatPrice(receivable1), modifier = Modifier.padding(start = 2.dp), fontWeight = FontWeight.Bold, fontSize =14.sp, color = Color.White)}
                      Icon(Icons.Outlined.HourglassTop,"",
                          modifier = Modifier.width(25.dp).height(25.dp).background(color = Purple80, shape = RoundedCornerShape(4.dp)).padding(4.dp), tint =Color.White)
                  }}
          }
        }

        Spacer(Modifier.height(80.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton (onClick = { navController.navigate(Screens.History.rout){popUpTo(navController.graph.startDestinationId){saveState=true}
                launchSingleTop=true
                restoreState=true}}){Text("مشاهده ی همه ", modifier = Modifier.padding(start = 12.dp), textAlign = TextAlign.Start, fontSize = 14.sp
                , color = Color(0xFF5612DE))  }
            Text("تراکنش های اخیر", modifier = Modifier.fillMaxWidth().padding(end = 14.dp), fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.End)
        }
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
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                items(transactions.size){
                    TransactionItem(transactions[it])
                    Spacer(Modifier.width(14.dp))
                }}
        }}

        LaunchedEffect(Unit) {
            message.collect {
                Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            }
        }



    }




    

}
   fun formatPrice(amount: Long):String{
    val formater= NumberFormat.getNumberInstance(java.util.Locale.US)
    return "  ${formater.format(amount)}   "
}


@Composable
fun TransactionItem(transactionEntity: TransactionEntity) {
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
                        text = when (transactionEntity.type) {
                            TransactionType.DEBT -> "نوع تراکنش : بدهی"
                            TransactionType.RECEIVABLE -> "نوع تراکنش : طلب"
                            TransactionType.INCOME -> " نوع تراکنش : در امد "
                            TransactionType.EXPENSE -> "نوع تراکنش : هزینه "
                        },
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
                Icon(when (transactionEntity.type) {
                    TransactionType.DEBT -> Icons.Filled.Warning
                    TransactionType.RECEIVABLE -> Icons.Filled.HourglassTop
                    TransactionType.INCOME -> Icons.Filled.MonetizationOn
                    TransactionType.EXPENSE -> Icons.Filled.ShoppingCart
                },"", modifier = Modifier.width(35.dp).height(35.dp).background(color = when (transactionEntity.type) {
                    TransactionType.DEBT -> OrangeLight
                    TransactionType.RECEIVABLE -> Purple80
                    TransactionType.INCOME -> GreenLight
                    TransactionType.EXPENSE -> RedLight
                },
                    shape = RoundedCornerShape(4.dp)).padding(4.dp), tint =Color.White)
                val descriptionText = if (transactionEntity.description.isEmpty()) {
                    "توضیحات : بدون توضیح"
                } else {
                    "توضیحات : ${transactionEntity.description}"
                }
                Text(
                    text = descriptionText,
                    modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                    style = TextStyle(textDirection = TextDirection.Rtl),
                    fontSize = 12.sp,
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
                        color = when (transactionEntity.type) {
                            TransactionType.DEBT -> Orange
                            TransactionType.RECEIVABLE -> PurpleLight
                            TransactionType.INCOME -> Green
                            TransactionType.EXPENSE -> Red
                        }
                    )
            )
        }
    }
}