package com.example.addiction_manage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.addiction_manage.feature.alcohol.AlcoholPage
import com.example.addiction_manage.feature.caffeine.CaffeinePage
import com.example.addiction_manage.feature.calendar.CalendarPage
import com.example.addiction_manage.feature.graph.GraphPage
import com.example.addiction_manage.feature.HomePage
import com.example.addiction_manage.feature.mypage.MyPage
import com.example.addiction_manage.feature.smoking.SmokingPage
import com.example.addiction_manage.feature.StartPage
import com.example.addiction_manage.feature.alcohol.AlcoholGoalPage
import com.example.addiction_manage.feature.caffeine.CaffeineGoalPage
import com.example.addiction_manage.feature.model.SmokingGoal
import com.example.addiction_manage.feature.smoking.SmokingGoalPage
import com.example.addiction_manage.feature.statistic.StatisticPage
import com.google.firebase.auth.FirebaseAuth
import com.example.addiction_manage.ui.auth.signin.SignInScreen
import com.example.addiction_manage.ui.auth.signup.SignUpScreen

@Composable
fun MainApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val start = if (currentUser != null) "home" else "signin"

        NavHost(
            navController = navController,
            startDestination = start // 로그인 스킵
        ) {
            composable(route = "start") {
                StartPage(
                    onLoginClick = {
                        navController.navigate(route = "signin")
                    },
                    onRegisterClick = {
                        navController.navigate(route = "signup")
                    }
                )
            }

            composable(route = "signin") {
                SignInScreen(navController)
            }

            composable(route = "signup") {
                SignUpScreen(
                    backToMainPage = {
                        navController.navigate(route = "start")
                    },
                    navController = navController
                )
            }
            composable(route = "home") {
                HomePage(
                    navigateToCalendar = { navController.navigate(route = "calendar") },
                    navigateToHome = { navController.navigate(route = "home") },
                    navigateToStatistic = { navController.navigate(route = "statistic") },
                    navigateToGraph = { navController.navigate(route = "graph") },
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navigateToAlcohol = { navController.navigate(route = "alcohol") },
                    navigateToCaffeine = { navController.navigate(route = "caffeine") },
                    navigateToSmoking = { navController.navigate(route = "smoking") },
                    navController = navController
                )
            }
            composable(route = "calendar") {
                CalendarPage(
                    navigateToCalendar = { navController.navigate(route = "calendar") },
                    navigateToHome = { navController.navigate(route = "home") },
                    navigateToStatistic = { navController.navigate(route = "statistic") },
                    navigateToGraph = { navController.navigate(route = "graph") },
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController
                )
            }
            composable(route = "statistic") {
                StatisticPage(
                    navigateToCalendar = { navController.navigate(route = "calendar") },
                    navigateToHome = { navController.navigate(route = "home") },
                    navigateToStatistic = { navController.navigate(route = "statistic") },
                    navigateToGraph = { navController.navigate(route = "graph") },
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController
                )
            }
            composable(route = "graph") {
                GraphPage(
                    navigateToCalendar = { navController.navigate(route = "calendar") },
                    navigateToHome = { navController.navigate(route = "home") },
                    navigateToStatistic = { navController.navigate(route = "statistic") },
                    navigateToGraph = { navController.navigate(route = "graph") },
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController,
                )
            }

            composable(route = "mypage") {
                MyPage(
                    navController = navController
                )
            }
            composable(route = "alcohol") {
                AlcoholPage(
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController,
                )
            }
            composable(route = "caffeine") {
                CaffeinePage(
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController
                )
            }
            composable(route = "smoking") {
                SmokingPage(
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController
                )
            }

            composable(route = "alcohol-goal") {
                AlcoholGoalPage(
                    navController = navController,
                )
            }
            composable(route = "caffeine-goal") {
                CaffeineGoalPage(
                    navController = navController
                )
            }
            composable(route = "smoking-goal") {
                SmokingGoalPage(
                    navController = navController
                )
            }
        }
    }
}