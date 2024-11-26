package com.example.addiction_manage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.MediumGrey

@Composable
fun HomePage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
    navigateToMyPage: () -> Unit,
    navigateToAlcohol: () -> Unit,
    navigateToCaffeine: () -> Unit,
    navigateToSmoking: () -> Unit,
    navController: NavController,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBarComponent(
                navigateUp = { navController.navigateUp() },
                navigateToMyPage = navigateToMyPage,
            )
        },
        bottomBar = {
            BottomAppBarComponent(
                navigateToCalendar = navigateToCalendar,
                navigateToHome = navigateToHome,
                navigateToStatistic = navigateToStatistic,
                isHomePage = true,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .padding(top = 150.dp)
                .background(color = LightGrey, shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SelectingPage(
                navigateToAlcohol = navigateToAlcohol,
                navigateToCaffeine = navigateToCaffeine,
                navigateToSmoking = navigateToSmoking,
            )
        }
    }
}

@Composable
fun SelectingPage(
    navigateToAlcohol: () -> Unit,
    navigateToCaffeine: () -> Unit,
    navigateToSmoking: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        color = MediumGrey
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = navigateToAlcohol,
                modifier = Modifier
                    .padding(8.dp)
                    .width(600.dp)
                    .height(70.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
                shape = RectangleShape
            ) {
                Text("나의 음주 기록하기", color = LightRed, fontSize = 24.sp)
            }
            Button(
                onClick = navigateToSmoking,
                modifier = Modifier
                    .padding(8.dp)
                    .width(500.dp)
                    .height(70.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
                shape = RectangleShape
            ) {
                Text("나의 흡연 기록하기", color = LightRed, fontSize = 24.sp)
            }
            Button(
                onClick = navigateToCaffeine,
                modifier = Modifier
                    .padding(8.dp)
                    .width(600.dp)
                    .height(70.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
                shape = RectangleShape
            ) {
                Text("나의 카페인 기록하기", color = LightRed, fontSize = 24.sp)
            }
        }
    }
}