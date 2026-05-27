package com.sinamtd.fiveappcompose.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottonNavigationBar(navController: NavController){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute=navBackStackEntry?.destination?.route
    NavigationBar {
        Screens.btmNavItems.forEach {screen ->
            NavigationBarItem( selected =currentRoute?.startsWith(screen.rout)==true,
                onClick = {navController.navigate(screen.rout){popUpTo (navController.graph.startDestinationId){
                    saveState=true}
                    launchSingleTop=true
                    restoreState=true
                 } },
                icon = { Icon( painter = painterResource(screen.icon), contentDescription = screen.title, modifier = Modifier.size(22.dp)) },
                label = { Text(screen.title )},
                colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Black, selectedTextColor = Color.Black, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
            )
        }
    }
}