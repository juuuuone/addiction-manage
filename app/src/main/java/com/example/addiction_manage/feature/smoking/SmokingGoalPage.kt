package com.example.addiction_manage.feature.smoking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.addiction_manage.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmokingGoalPage(
    navController: NavController
) {
    var selectedOption by remember { mutableStateOf("") }
    val viewModel: SmokingGoalViewModel = hiltViewModel()
    val isSmokingChecked by viewModel.isSmokingChecked.collectAsState()
    var smokingDayGoal by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val currentUserGoal = viewModel.getCurrentUserGoal()
    val newGoal = remember { mutableStateOf(currentUserGoal?.goal ?: "") }
    //폰트
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
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
                text = stringResource(id= R.string.set_smoking_goal),
                text = "흡연 목표를 설정하세요",
                color = Black,
                fontSize = 24.sp,
                fontFamily = boldFontFamily,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id= R.string.write_smoking_goal),
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
                    // 드롭다운 메뉴
//                    GoalDropdown(
//                        label = "하루 목표 설정",
//                        options = listOf("1~2개피", "3~5개피", "반 갑", "한 갑"),
//                        selectedOption = selectedOption,
//                        onOptionSelected = { selectedOption = it }
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

                    // 체크박스
                    CheckboxWithBorder(
                        label = stringResource(id = R.string.not_smoking),
                        isChecked = isSmokingChecked,
                        onCheckedChange = { viewModel.setNoSmokingChecked(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 다음 버튼
            Button(
                onClick = {
                    navController.navigate(route = "caffeine-goal")
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = stringResource(id=R.string.next_button), fontFamily = lightFontFamily, fontSize = 20.sp)
            }
        }

        if (showDialog.value) {
            ShowSaveDialog(
                onDismiss = { showDialog.value = false }
            )
        }
    }
}

@Composable
fun GoalDropdown(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp),
            color = Black
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp)
                .clickable { expanded = true }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedOption.ifEmpty { "선택하세요" },
                    color = Black,
                    fontSize = 16.sp
                )
                Text(
                    text = "∨",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(option, color = Black)
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun CheckboxWithBorder(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = LightRed,
                uncheckedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Black, fontSize = 16.sp)
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
