package com.example.addiction_manage.feature.smoking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.WhiteBlue
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SmokingPage(
    navigateToMyPage: () -> Unit,
    navController: NavController,
    navigateToHome: () -> Unit,
) {
    val viewModel = hiltViewModel<SmokingViewModel>()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: return
    var count by remember { mutableIntStateOf(0) }
    LaunchedEffect(key1 = true) {
        viewModel.listenForSmokingRecords(userId)
    }
    val smokingRecords = viewModel.smokingRecords.collectAsState()
    val todayRecord = viewModel.getTodaySmokingRecord(smokingRecords.value)
    LaunchedEffect(key1 = todayRecord) {
        count = todayRecord?.cigarettes ?: 0
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBarComponent(
                navigateToMyPage = navigateToMyPage,
                navigateUp = { navController.navigateUp() }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SmokingRecording(
                count = count,
                upCount = { count++ },
                downCount = { if (count > 0) count-- else count = 0 },
                navigateToHome = navigateToHome,
            )
        }
    }
}

@Composable
fun SmokingRecording(
    count: Int,
    upCount: () -> Unit,
    downCount: () -> Unit,
    navigateToHome: () -> Unit,
) {
    val viewModel = hiltViewModel<SmokingViewModel>()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp),
        shape = RoundedCornerShape(8.dp),
        color = White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("오늘 담배를 얼마나 폈나요?", color = MediumBlue, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "오늘",
                fontSize = 30.sp,
                color = MediumBlue,
                modifier = Modifier.padding(top = 15.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 흡연 횟수 표시
                Button(
                    onClick = downCount,  // 버튼 클릭 시 count 증가
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WhiteBlue),
                    shape = CircleShape,
                    contentPadding = PaddingValues()  // 내부 패딩 제거
                ) {
                    Box(
                        contentAlignment = Alignment.Center,  // 내용을 중앙에 정렬
                        modifier = Modifier.fillMaxSize()  // Box를 버튼 크기만큼 채움
                    ) {
                        Text(
                            text = "-",
                            fontSize = 24.sp,
                            color = Black
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                Text(
                    text = "$count",
                    fontSize = 30.sp,
                    color = Black,
                    modifier = Modifier.padding(top = 10.dp)
                )
                Text(
                    text = " 개피",
                    fontSize = 20.sp,
                    color = MediumBlue,
                    modifier = Modifier.padding(top = 15.dp)
                )
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                // 흡연 횟수 증가 버튼
                Button(
                    onClick = upCount,  // 버튼 클릭 시 count 증가
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WhiteBlue),
                    shape = CircleShape,
                    contentPadding = PaddingValues()  // 내부 패딩 제거
                ) {
                    Box(
                        contentAlignment = Alignment.Center,  // 내용을 중앙에 정렬
                        modifier = Modifier.fillMaxSize()  // Box를 버튼 크기만큼 채움
                    ) {
                        Text(
                            text = "+",
                            fontSize = 24.sp,
                            color = Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            OutlinedButton(
                onClick = {
                    viewModel.addSmokingRecord(count)
                    navigateToHome()
                },
                modifier = Modifier
                    .padding(4.dp)
                    .width(150.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = White),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(width = 2.dp, color = MediumBlue)
            ) {
                Text(
                    "기록하기",
                    fontSize = 20.sp,
                    color = Black,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
            }
        }
    }
}
