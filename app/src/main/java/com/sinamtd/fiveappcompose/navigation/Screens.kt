package com.sinamtd.fiveappcompose.navigation

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.painter.Painter
import com.sinamtd.fiveappcompose.R
import com.sinamtd.fiveappcompose.screens.MainScreen
import kotlin.collections.forEachIndexed

sealed class Screens(val rout: String,val title: String,val icon: Int) {
    object Diagram: Screens("diagram","نمودار تراکنش ها",R.drawable.line)

    object History: Screens("history","  تاریخچه تراکنش ها",R.drawable.order)
    object Main: Screens( "main","صقحه ی اصلی",R.drawable.home2)

    object AddTrans: Screens("addTrans","تراکنش جدید",R.drawable.pencil)





    fun routeWithArgs(vararg args: String)=
        buildString {
            append(rout)
            args.forEachIndexed { index, _ ->
                append("?$index={$index}")
            }
        }

    fun paramsWithArgs(vararg args: String)=

        buildString {
            append(rout)
            args.forEachIndexed { index, value ->
                append("?$index=$value")
            }
        }

    companion object{
        val btmNavItems=listOf(History, Diagram, AddTrans,Main )
    }


}