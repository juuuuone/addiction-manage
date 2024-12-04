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
fun CaffeineGoalPage(
    navController: NavController
) {
    var selectedOption by remember { mutableStateOf("") }

    val viewModel: CaffeineGoalViewModel = hiltViewModel()
    val isCaffeineChecked by viewModel.isCaffeineChecked.collectAsState()
    var caffeineDayGoal by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val currentUserGoal = viewModel.getCurrentUserGoal()
    // 폰트
    val boldFontFamily = FontFamily(Font(R.font.bold))
    val lightFontFamily = FontFamily(Font(R.font.light))

    val newGoal = remember { mutableStateOf("로딩 중...") }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(currentUserGoal) {
        isLoading.value = true
        val goal = currentUserGoal?.goal
        newGoal.value = goal ?: ""
        isLoading.value = false
    }

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
                text = stringResource(id=R.string.set_caffeine_goal),
                color = Black,
                fontSize = 24.sp,
                fontFamily = boldFontFamily,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id=R.string.write_caffeine_goal),
                fontSize = 19.sp,
                fontFamily = lightFontFamily,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
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
                        value = if (isLoading.value) "로딩중 ..." else newGoal.value,
                        onValueChange = { newGoal.value = it },
                        enabled = true // 텍스트 필드는 항상 활성화
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.addGoal(newGoal.value)
                                  showDialog.value = true},
                        colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text= stringResource(id=R.string.save_button),
                            fontFamily = lightFontFamily
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    // 체크박스
                    CheckboxWithBorder(
                        label = stringResource(id=R.string.not_caffeine),
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
