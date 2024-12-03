package com.example.addiction_manage.feature.alcohol

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.feature.smoking.CheckboxWithBorder
import com.example.addiction_manage.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlcoholGoalPage(
    navController: NavController
) {
    val viewModel : AlcoholGoalViewModel = hiltViewModel()
    val isNoAlcoholChecked by viewModel.isAlcoholChecked.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val currentUserGoal = viewModel.getCurrentUserGoal()
    val newGoal = remember { mutableStateOf(currentUserGoal?.goal ?: "") }

    val boldFontFamily = FontFamily(Font(R.font.bold))
    val lightFontFamily = FontFamily(Font(R.font.light))

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.set_goal),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
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
                text = stringResource(id=R.string.set_alcohol_goal),
                color = Black,
                fontSize = 24.sp,
                fontFamily = boldFontFamily,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(id=R.string.write_alcohol_goal),
                fontSize = 19.sp,
                color = Color.DarkGray,
                fontFamily = lightFontFamily,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(35.dp))

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
                        value = newGoal.value,
                        onValueChange = { newGoal.value = it }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            viewModel.addGoal(newGoal.value)
                            showDialog.value = true
                                  },
                        colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                          text = stringResource(id=R.string.save_button),
                          fontFamily = lightFontFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 체크박스: 음주하지 않습니다
                    CheckboxWithBorder(
                        label = stringResource(id=R.string.not_alcohol),
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
                colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(id=R.string.next_button), fontFamily = lightFontFamily, fontSize = 20.sp)
            }

            if (showDialog.value) {
                ShowSaveDialog(
                    onDismiss = { showDialog.value = false }
                )
            }
        }
    }
}

@Composable
fun ShowSaveDialog(onDismiss: () -> Unit) {

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(id=R.string.check_button))
            }
        },
        title = { Text(stringResource(id = R.string.notify)) },
        text = { Text(stringResource(id=R.string.is_saving)) }
    )
}
