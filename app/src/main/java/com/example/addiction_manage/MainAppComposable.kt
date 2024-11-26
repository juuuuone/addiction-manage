package com.example.addiction_manage

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.addiction_manage.ui.AlcoholPage
import com.example.addiction_manage.ui.CaffeinePage
import com.example.addiction_manage.ui.CalendarPage
import com.example.addiction_manage.ui.HomePage
import com.example.addiction_manage.ui.MyPage
import com.example.addiction_manage.ui.SmokingPage
import com.example.addiction_manage.ui.StartPage
import com.example.addiction_manage.ui.StatisticPage
import com.example.addiction_manage.ui.auth.signin.SignInScreen
import com.example.addiction_manage.ui.auth.signup.SignUpScreen

@Composable
fun MainApp(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        composable(route = "start"){
            StartPage(
                onLoginClick = {
                    navController.navigate(route = "signin")
                },
                onRegisterClick = {
                    navController.navigate(route = "signup")
                }
            )
        }

        composable(route = "signin"){
            SignInScreen(navController)
        }

        composable(route = "signup"){
            SignUpScreen(
                backToMainPage = {
                    navController.navigate(route = "start")
                },
                navController = navController
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
