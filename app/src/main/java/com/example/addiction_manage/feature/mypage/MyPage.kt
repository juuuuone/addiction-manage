package com.example.addiction_manage.feature.mypage

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.feature.smoking.SmokingGoalViewModel
import com.example.addiction_manage.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPage(
    navController: NavController
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var nickname: String = currentUser?.let { checkUser(it) }.toString()
    val viewModel: SmokingGoalViewModel = hiltViewModel()
    val smokingGoals = viewModel.goal.collectAsState().value
    val isNotSmoking = viewModel.isNoSmokingChecked
    val isLoading = viewModel.isLoading.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "마이페이지",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // 닉네임 섹션
            Text(
                text = "닉네임",
                fontSize = 24.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = nickname,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 음주 목표 섹션 -> 이 섹션들은 초기에 목표 설정했던 것을 가져와야함. 지금은 임시 데이터.
            GoalSection(
                title = "나의 음주 목표",
                goals = listOf("1주일 2회 이하", "음주량 1병 이하")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 흡연 목표 섹션
            if(isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            else if (smokingGoals.isNotEmpty() && !isNotSmoking.value) {
                GoalSection(
                    title = "나의 흡연 목표",
                    goals = listOf(smokingGoals.joinToString{it.goal} + "개피")
                )
            } else {
                GoalSection(
                    title = "나의 흡연 목표",
                    goals = listOf("목표가 없습니다.")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 카페인 목표 섹션
            GoalSection(
                title = "나의 카페인 목표",
                goals = listOf("하루 2잔 이하 (300mg)")
            )

            Spacer(modifier = Modifier.weight(1f))

            // 편집 버튼 -> 목표 설정 페이지 재사용...?
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { /* 편집 동작 */ }
                    .background(LightRed, shape = CircleShape)
                    .size(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun GoalSection(title: String, goals: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        goals.forEach { goal ->
            Text(
                text = "$goal 이하",
                fontSize = 25.sp,
                color = DarkRed,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
        }
    }
}

fun checkUser(currentUser : FirebaseUser) : String {
    return currentUser.displayName ?: "Unknown User"
}