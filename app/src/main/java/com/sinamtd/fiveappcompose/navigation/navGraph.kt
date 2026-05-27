package com.sinamtd.fiveappcompose.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sinamtd.fiveappcompose.screens.AddTransactionScreen
import com.sinamtd.fiveappcompose.screens.DiagramScreen
import com.sinamtd.fiveappcompose.screens.HistoryScreen
import com.sinamtd.fiveappcompose.screens.MainScreen
import kotlin.collections.listOf


@Composable
fun SetUpNavigation(navController: NavHostController, paddingValues: PaddingValues){

    NavHost(navController = navController, startDestination = Screens.Main.rout, modifier = Modifier.padding(paddingValues)) {


        composable(route = Screens.Main.rout) { MainScreen(navController = navController) }

        composable(route = Screens.AddTrans.routeWithArgs("0:id"),
            arguments = listOf(navArgument("0"){
                type= NavType.StringType
                defaultValue="0"
            })
        ) {
            val id = it.arguments?.getString("0")?:"0"
            AddTransactionScreen(navController = navController, id = id)

        }

        composable(route = Screens.History.rout){ HistoryScreen(navController=navController) }

        composable(route = Screens.Diagram.rout){ DiagramScreen() }
    }

}