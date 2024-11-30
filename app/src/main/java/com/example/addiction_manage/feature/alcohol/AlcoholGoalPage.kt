package com.example.addiction_manage.feature.alcohol

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.feature.smoking.CheckboxWithBorder
import com.example.addiction_manage.feature.smoking.GoalDropdown
import com.example.addiction_manage.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlcoholGoalPage(
    navController: NavController
) {
    var dailyGoal by remember { mutableStateOf("") }
    var weeklyGoal by remember { mutableStateOf("") }
    val viewModel : AlcoholGoalViewModel = hiltViewModel()
    val isNoAlcoholChecked by viewModel.isNoAlcoholChecked.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "음주 목표 설정",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 제목 및 설명
            Text(
                text = "음주 목표를 설정하세요",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "오늘과 일주일의 음주량 목표를 설정하거나\n'음주하지 않습니다'를 체크하세요.",
                fontSize = 19.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 카드 배경
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    // 하루 목표 드롭다운
//                    GoalDropdown(
//                        label = "하루 목표 설정",
//                        options = listOf("반 잔", "한 잔", "2~3 잔", "한 병"),
//                        selectedOption = dailyGoal,
//                        onOptionSelected = { dailyGoal = it }
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // 일주일 목표 드롭다운
//                    GoalDropdown(
//                        label = "일주일 목표 설정 (횟수)",
//                        options = listOf("1회", "2회", "3회", "4회 이상"),
//                        selectedOption = weeklyGoal,
//                        onOptionSelected = { weeklyGoal = it }
//                    )

                    TextField(
                        value = weeklyGoal,
                        onValueChange = { weeklyGoal = it }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.addGoal(weeklyGoal) },
                        colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("저장")
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 체크박스: 음주하지 않습니다
                    CheckboxWithBorder(
                        label = "음주하지 않습니다",
                        isChecked = isNoAlcoholChecked,
                        onCheckedChange = { viewModel.setNoAlcoholChecked(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 다음 버튼
            Button(
                onClick = {
                    navController.navigate(route = "smoking-goal")
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightRed),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "다음", fontSize = 18.sp, color = White)
            }
        }
    }
}

