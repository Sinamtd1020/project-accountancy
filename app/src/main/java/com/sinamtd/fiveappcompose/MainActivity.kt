package com.sinamtd.fiveappcompose

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.sinamtd.fiveappcompose.navigation.BottonNavigationBar
import com.sinamtd.fiveappcompose.navigation.SetUpNavigation
import com.sinamtd.fiveappcompose.ui.theme.FiveAppComposeTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        WindowCompat.setDecorFitsSystemWindows(window, false)


        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = android.graphics.Color.LTGRAY

        }

        windowInsetsController.isAppearanceLightStatusBars = true



        windowInsetsController.isAppearanceLightNavigationBars = true

        setContent {

           FiveAppComposeTheme() {
               val navController=rememberNavController()
               Scaffold(bottomBar = { BottonNavigationBar(navController) }) { paddingValues ->
                   SetUpNavigation(navController,paddingValues)
               }
           }
        }
    }
}

