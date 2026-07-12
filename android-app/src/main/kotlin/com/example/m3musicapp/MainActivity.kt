package com.example.m3musicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.m3musicapp.screens.HomeScreen
import com.example.m3musicapp.screens.DataScreen
import com.example.m3musicapp.ui.theme.M3MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            M3MusicAppTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF343434)) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    label = { Text("首頁", color = Color.White) },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("data") {
                            popUpTo("data") { inclusive = true }
                        }
                    },
                    label = { Text("資料", color = Color.White) },
                    icon = {}
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF343434))
        ) {
            NavHost(navController, startDestination = "home") {
                composable("home") { HomeScreen() }
                composable("data") { DataScreen() }
            }
        }
    }
}
