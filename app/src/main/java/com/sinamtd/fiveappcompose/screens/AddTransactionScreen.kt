package com.sinamtd.fiveappcompose.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sinamtd.fiveappcompose.model.data.TransactionType
import com.sinamtd.fiveappcompose.navigation.Screens
import com.sinamtd.fiveappcompose.viewmodel.TransViewModel

@Composable
fun AddTransactionScreen(navController: NavController, id: String, viewModel: TransViewModel= hiltViewModel()){
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(0L) }
    var dateShow by remember { mutableStateOf("") }
    var month by remember { mutableStateOf(0) }
    val message= viewModel.message
    var amountError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }
    var selectType by remember { mutableStateOf(TransactionType.INCOME) }
    val context= LocalContext.current
    var amountErrorMessage by remember { mutableStateOf("") }
    var categoryErrorMessage by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {if (id.toInt()>0){viewModel.getByID(id = id.toLong()){
        amount=it.amount.toString()
        category=it.category
        desc=it.description
        selectType=it.type
        hour=it.hour
        date=it.date
        dateShow=it.dateShow
        month=it.monthYear


    }
    } }
    fun errorBlanc(): Boolean{
        amountError=amount.isBlank()
        amountErrorMessage=if (amountError) "مبلغ نمیتواند خالی باشد" else ""


        categoryError=category.isBlank()
        categoryErrorMessage=if (categoryError) "دسته بندی نمیتواند خالی باشد" else ""

        return !amountError && !categoryError
    }
    fun saveTrans(){
        if (!errorBlanc()) return

        val amount=amount.toLong()
        viewModel.insert(category,amount,selectType,desc,System.currentTimeMillis())
        navController.popBackStack(Screens.Main.rout, inclusive = false)}
    fun updateTrans(){
        if (!errorBlanc()) return

        val amount=amount.toLong()
        viewModel.update(id.toLong(),category,amount,selectType,desc,date,hour,dateShow,month)
        navController.popBackStack(Screens.History.rout, inclusive = false)

        }
    fun getType(type: TransactionType): String{
        return when(type){
            TransactionType.DEBT -> "بدهی"
            TransactionType.EXPENSE -> "هزینه"
            TransactionType.INCOME -> "درامد"
            TransactionType.RECEIVABLE -> "طلب"

        }

    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(Pair(0.1f,Color(0xFFE9E3F5)),Pair(0.9f,Color.White)))) {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(90.dp)
                    .background(
                        Color(0xFF33048A),
                        shape = RoundedCornerShape(bottomEnd = 22.dp, bottomStart = 22.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    "تراکنش جدید",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(Modifier.height(12.dp))
                Text(
                ": نوع تراکنش ",
                modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TransactionType.entries.forEach { type->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = getType(type), fontSize = 12.sp, color = Color.Black)
                        Spacer(Modifier.width(4.dp))
                        RadioButton(selected = selectType==type, onClick = {selectType=type}, colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF33048A)))
                    }
                }


            }
            Spacer(Modifier.height(12.dp))
            Text(
                ":دسته بندی تراکنش ",
                modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            TextField(value = category, onValueChange = {input->
                category=input
                if (categoryError){categoryError=false}}, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).height(60.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)),
                shape = RoundedCornerShape( 15.dp),
                textStyle = TextStyle(textAlign = TextAlign.End),
                isError = categoryError,
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                    , errorIndicatorColor = Color.Red, errorContainerColor = Color.White,),
                placeholder =
                    { Text (text = "... خوراک, پوشاک, خانه, ماشین", modifier = Modifier.fillMaxWidth().padding(end = 8.dp), fontSize = 13.sp, textAlign = TextAlign.End, color = Color.Gray)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
            if (categoryError){
                Text(categoryErrorMessage, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(top = 3.dp, end = 18.dp), color = Color.Red, textAlign = TextAlign.End)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                ":مقدار تراکنش  (به تومان) ",
                modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            TextField(value = amount, onValueChange = { value ->
                amount=value.filter { it.isDigit() }
                if (amountError){amountError=false}}, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)),
                shape = RoundedCornerShape( 15.dp),
                textStyle = TextStyle(textAlign = TextAlign.End),
                isError =amountError,
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White
                    , errorIndicatorColor = Color.Red, errorContainerColor = Color.White,),
                placeholder =
                    { Text (text = "15000000", modifier = Modifier.fillMaxWidth().padding(end = 8.dp), fontSize = 13.sp, textAlign = TextAlign.End, color = Color.Gray)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
                if (amountError){
                    Text(amountErrorMessage, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(top = 3.dp, end = 18.dp), color = Color.Red, textAlign = TextAlign.End)
                }

            Spacer(Modifier.height(12.dp))
            Text(
                ": توضیحات تراکنش (اختیاری)  ",
                modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            TextField(value = desc, onValueChange = {input->
                desc = input}, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).height(90.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(15.dp)),
                shape = RoundedCornerShape( 15.dp),
                textStyle = TextStyle(textAlign = TextAlign.End),
                isError =false,
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, errorIndicatorColor = Color.Red),
                placeholder =
                    { Text (text = "...هزینه ی تجهیزات و دستمزد برای مکانیک", modifier = Modifier.fillMaxWidth().padding(end = 8.dp), fontSize = 13.sp, textAlign = TextAlign.End, color = Color.Gray)},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))

            LaunchedEffect(Unit) {
                message.collect {
                    Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd){
            if (id.toLong()>0) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround) {
                    Button(
                        onClick = {navController.popBackStack(Screens.History.rout, inclusive = false)},
                        modifier = Modifier.weight(0.5f).padding(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF33048A),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            "منصرف شدن",
                            modifier = Modifier.padding(vertical = 7.dp),
                            fontSize = 16.sp
                            , color = Color.White
                        )
                    }
                    Button(
                    onClick = { updateTrans() },
                    modifier = Modifier.weight(0.5f).padding(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF33048A),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        "بروزرسانی تراکنش",
                        modifier = Modifier.padding(vertical = 7.dp),
                        fontSize = 16.sp
                    )
                }

            }}
            else{  Button(
                onClick = {saveTrans()},
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF33048A),
                    contentColor = Color.White
                )
            ) {
                Text(
                    "ثبت تراکنش",
                    modifier = Modifier.padding(vertical = 7.dp),
                    fontSize = 16.sp
                )
            }}
        }


    }

}


