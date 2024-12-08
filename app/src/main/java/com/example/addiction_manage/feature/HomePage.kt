package com.example.addiction_manage.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.feature.alcohol.AlcoholGoalViewModel
import com.example.addiction_manage.feature.alcohol.AlcoholViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineGoalViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineViewModel
import com.example.addiction_manage.feature.calendar.BottomAppBarComponent
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.feature.mypage.checkUser
import com.example.addiction_manage.feature.smoking.SmokingGoalViewModel
import com.example.addiction_manage.feature.smoking.SmokingViewModel
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.LightBlue
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.White
import com.google.firebase.auth.FirebaseAuth
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun HomePage(
    navigateToMyPage: () -> Unit,
    navigateToAlcohol: () -> Unit,
    navigateToCaffeine: () -> Unit,
    navigateToSmoking: () -> Unit,
    navController: NavController,
) {
    val alcoholString = stringResource(id = R.string.alcohol)
    val smokingString = stringResource(id = R.string.smoking)
    val caffeineString = stringResource(id = R.string.caffeine)

    val alcoholGoalViewModel = hiltViewModel<AlcoholGoalViewModel>()
    val smokingGoalViewModel = hiltViewModel<SmokingGoalViewModel>()
    val caffeineGoalViewModel = hiltViewModel<CaffeineGoalViewModel>()

    val hasAlcoholGoal = alcoholGoalViewModel.isAlcoholChecked.collectAsState()
    val hasSmokingGoal = smokingGoalViewModel.isSmokingChecked.collectAsState()
    val hasCaffeineGoal = caffeineGoalViewModel.isCaffeineChecked.collectAsState()

    var options by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(
        hasAlcoholGoal.value,
        hasSmokingGoal.value,
        hasCaffeineGoal.value,
    ) {
        var updatedOptions = mutableListOf<String>()

        if (!hasAlcoholGoal.value) {
            updatedOptions.add(alcoholString)
        }
        if (!hasSmokingGoal.value) {
            updatedOptions.add(smokingString)
        }
        if (!hasCaffeineGoal.value) {
            updatedOptions.add(caffeineString)
        }
        options = updatedOptions
    }

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
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(top=20.dp)
                .padding(horizontal = 8.dp)
                .background(color = MediumBlue, shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SelectingPage(
                options = options,
                alcoholString = alcoholString,
                smokingString = smokingString,
                caffeineString = caffeineString,
                navigateToAlcohol = navigateToAlcohol,
                navigateToCaffeine = navigateToCaffeine,
                navigateToSmoking = navigateToSmoking,
            )
        }
    }
}

@Composable
fun SelectingPage(
    options: List<String>,
    alcoholString: String,
    smokingString: String,
    caffeineString: String,
    navigateToAlcohol: () -> Unit,
    navigateToCaffeine: () -> Unit,
    navigateToSmoking: () -> Unit,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val nickname: String = currentUser?.let { checkUser(it) }.toString()

    val currentTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
    val currentHour = currentTime.hour
    val greeting = when {
        currentHour in 5..10 -> stringResource(id = R.string.morning)
        currentHour in 11..16 -> stringResource(id = R.string.afternoon)
        currentHour in 17..22 -> stringResource(id = R.string.dinner)
        else -> stringResource(id = R.string.evening)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "$greeting",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.light)),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(60.dp))
            Row() {
                Text(
                    text = "$nickname",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = stringResource(id = R.string.record_lifestyle),
                    color = Black,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .padding(top = 16.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.record_2),
                color = Black,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.minsans)),
                modifier = Modifier
                    .padding(start = 18.dp)
                    .padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))
            if (options.contains(alcoholString)) {
                Button(
                    onClick = navigateToAlcohol,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(600.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.record_my_alcohol),
                        color = White,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.minsans))
                    )
                }
            }
            if (options.contains(smokingString)) {
                Button(
                    onClick = navigateToSmoking,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(600.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.record_my_smoking),
                        color = White,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.minsans))
                    )
                }
            }
            if (options.contains(caffeineString)) {
                Button(
                    onClick = navigateToCaffeine,
                    modifier = Modifier
                        .padding(8.dp)
                        .width(600.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.record_my_caffeine),
                        color = White,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.minsans))
                    )
                }
            }

        }
    }
}