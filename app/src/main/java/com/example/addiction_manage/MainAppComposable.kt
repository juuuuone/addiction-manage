package com.example.addiction_manage

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.addiction_manage.feature.alcohol.AlcoholPage
import com.example.addiction_manage.feature.caffeine.CaffeinePage
import com.example.addiction_manage.feature.calendar.CalendarPage
import com.example.addiction_manage.feature.HomePage
import com.example.addiction_manage.feature.mypage.MyPage
import com.example.addiction_manage.feature.smoking.SmokingPage
import com.example.addiction_manage.feature.StartPage
import com.example.addiction_manage.feature.alcohol.AlcoholGoalPage
import com.example.addiction_manage.feature.caffeine.CaffeineGoalPage
import com.example.addiction_manage.feature.friends.FriendsPage
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
        val start = if (currentUser != null) "home" else "start"
        var selectedItem by rememberSaveable { mutableStateOf(1) }

        NavHost(
            navController = navController,
            startDestination = //"start"
        start
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
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navigateToAlcohol = { navController.navigate(route = "alcohol") },
                    navigateToCaffeine = { navController.navigate(route = "caffeine") },
                    navigateToSmoking = { navController.navigate(route = "smoking") },
                    navController = navController
                )
            }
            composable(route = "calendar") {
                CalendarPage(
                    navigateToCalendar = {
                        selectedItem = 0
                        navController.navigate(route = "calendar")
                    },
                    navigateToHome = {
                        selectedItem = 1
                        navController.navigate(route = "home")
                    },
                    navigateToStatistic = {
                        selectedItem = 2
                        navController.navigate(route = "statistic")
                    },
                    navigateToFriends = {
                        selectedItem = 3
                        navController.navigate(route = "friends")
                    },
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    selectedItem = selectedItem,
                    navController = navController
                )
            }
            composable(route = "statistic") {
                StatisticPage(
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController
                )
            }
            composable(route = "friends") {
                FriendsPage(
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController
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
                    navigateToHome = { navController.navigate(route = "home") },
                )
            }
            composable(route = "caffeine") {
                CaffeinePage(
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController,
                    navigateToHome = { navController.navigate(route = "home") },
                )
            }
            composable(route = "smoking") {
                SmokingPage(
                    navigateToMyPage = { navController.navigate(route = "mypage") },
                    navController = navController,
                    navigateToHome = { navController.navigate(route = "home") }
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

