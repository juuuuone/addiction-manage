package com.example.addiction_manage.feature.caffeine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.addiction_manage.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaffeineGoalPage(
    navController: NavController
) {
    var selectedOption by remember { mutableStateOf("") }

    val viewModel: CaffeineGoalViewModel = hiltViewModel()
    val isCaffeineChecked by viewModel.isCaffeineChecked.collectAsState()
    var caffeineDayGoal by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val currentUserGoal = viewModel.getCurrentUserGoal()
    val newGoal = remember { mutableStateOf(currentUserGoal?.goal ?: "") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "카페인 목표 설정",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor),
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
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
                text = "카페인 목표를 설정하세요",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "하루 목표 섭취 잔 수를 입력하거나\n'카페인을 섭취하지 않습니다'를 체크하세요.",
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
                    TextField(
                        value = newGoal.value,
                        onValueChange = { newGoal.value = it }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.addGoal(newGoal.value)
                                  showDialog.value = true},
                        colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                        shape = RoundedCornerShape(8.dp)
                        ) {
                        Text("저장")
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // 체크박스
                    CheckboxWithBorder(
                        label = "카페인을 섭취하지 않습니다",
                        isChecked = isCaffeineChecked,
                        onCheckedChange = { viewModel.setNoCaffeineChecked(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 다음 버튼
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("signin") { inclusive = true }
                        popUpTo("signup") { inclusive = true }
                        popUpTo("alcohol-goal") { inclusive = true }
                        popUpTo("smoking-goal") { inclusive = true }
                        popUpTo("caffeine-goal") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "다음", fontSize = 18.sp, color = White)
            }


            if (showDialog.value) {
                com.example.addiction_manage.feature.alcohol.showSaveDialog(
                    onDismiss = { showDialog.value = false }
                )
            }
        }
    }
}
@Composable
fun showSaveDialog(onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("확인")
            }
        },
        title = { Text("알림") },
        text = { Text("저장되었습니다!") }
    )

}
