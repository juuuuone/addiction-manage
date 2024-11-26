package com.example.addiction_manage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.addiction_manage.ui.AlcoholGoalPage
import com.example.addiction_manage.ui.AlcoholPage
import com.example.addiction_manage.ui.CaffeineGoalPage
import com.example.addiction_manage.ui.CaffeinePage
import com.example.addiction_manage.ui.CalendarPage
import com.example.addiction_manage.ui.HomePage
import com.example.addiction_manage.ui.LoginPage
import com.example.addiction_manage.ui.MainPage
import com.example.addiction_manage.ui.RegisterPage
import com.example.addiction_manage.ui.SetNamePage
import com.example.addiction_manage.ui.MyPage
import com.example.addiction_manage.ui.SetNamePage
import com.example.addiction_manage.ui.SmokingGoalPage
import com.example.addiction_manage.ui.StatisticPage
import com.example.addiction_manage.ui.SmokingPage
import com.example.addiction_manage.ui.StartButton
import com.example.addiction_manage.ui.StartPage
import com.example.addiction_manage.ui.theme.Addiction_manageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Addiction_manageTheme {
                val navController = rememberNavController()
                NavHost(
                   navController = navController,
                    startDestination = "start"
                ) {
                    composable(route = "start"){
                        StartPage(
                            onLoginClick = {
                                navController.navigate(route = "login")
                            },
                            onRegisterClick = {
                            navController.navigate(route = "register")
                        }
                        )
                    }

                    composable(route = "login"){
                        LoginPage(navController)
                    }

                    composable(route = "register"){
                        RegisterPage(
                            backToLoginPage = {
                                navController.navigate(route = "start")
                            }
                        )
                    }
                    
                    composable(route = "Home") {
                        HomePage(
                            navigateToCalendar = { navController.navigate(route = "Calendar") },
                            navigateToHome = { navController.navigate(route = "Home") },
                            navigateToStatistic = { navController.navigate(route = "Statistic") },
                            navigateToMyPage = { navController.navigate(route = "MyPage") },
                            navigateToAlcohol = { navController.navigate(route = "Alcohol") },
                            navigateToCaffeine = { navController.navigate(route = "Caffeine") },
                            navigateToSmoking = { navController.navigate(route = "Smoking") },
                            navController = navController
                        )
                    }
                    composable(route = "Calendar") {
                        CalendarPage(
                            navigateToCalendar = { navController.navigate(route = "Calendar") },
                            navigateToHome = { navController.navigate(route = "Home") },
                            navigateToStatistic = { navController.navigate(route = "Statistic") },
                            navigateToMyPage = { navController.navigate(route = "MyPage") },
                            navController = navController
                        )
                    }
                    composable(route = "Statistic") {
                        StatisticPage(
                            navigateToCalendar = { navController.navigate(route = "Calendar") },
                            navigateToHome = { navController.navigate(route = "Home") },
                            navigateToStatistic = { navController.navigate(route = "Statistic") },
                            navigateToMyPage = { navController.navigate(route = "MyPage") },
                            navController = navController
                        )
                    }
                    composable(route = "MyPage") {
                        MyPage(
                            navController = navController
                        )
                    }
                    composable(route = "Alcohol") {
                        AlcoholPage(
                            navigateToMyPage = { navController.navigate(route = "MyPage") },
                            navController = navController,
                        )
                    }
                    composable(route = "Caffeine") {
                        CaffeinePage(
                            navigateToMyPage = { navController.navigate(route = "MyPage") },
                            navController = navController
                        )
                    }
                    composable(route = "Smoking") {
                        SmokingPage(
                            navigateToMyPage = { navController.navigate(route = "MyPage") },
                            navController = navController
                        )
                    }
            }
        }
    }
}
