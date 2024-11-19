package com.example.addiction_manage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.Black
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSettingPage(selectedItems: List<String>) {
    // MainActivity.kt에서 확인할 때는 우선 GoalSettingPage(selectedItems = listOf("음주", "카페인")) 식으로 호출
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "중독 관리 어플",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {/*뒤로가기*/ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundColor,
                    titleContentColor = White
                )
            )
        }
    ) { innerPadding ->
        if (selectedItems.size == 1) { // 선택된 항목이 1개 일때는 화면 가운데에 배치
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoalCard(selectedItems.first())
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* 다음 동작 구현 */ },
                    modifier = Modifier
                        .width(150.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightRed)
                ) {
                    Text(text = "다음", fontSize = 18.sp, color = White)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(selectedItems) { item ->
                    GoalCard(item)
                }

                item {
                    Button(
                        onClick = { /* 다음 동작 */ },
                        modifier = Modifier
                            .width(150.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LightRed)
                    ) {
                        Text(text = "다음", fontSize = 18.sp, color = White)
                    }
                }
            }
        }
    }
}

@Composable
fun GoalCard(item: String) {
    val options = when (item) {
        "음주" -> listOf("1~2잔", "반 병", "한 병", "한 병 반", "두 병")
        "카페인" -> listOf("반 잔", "한 잔", "두 잔", "세 잔")
        "흡연" -> listOf("1~2개피", "3~5개피", "반 갑", "3/4갑", "한 갑")
        else -> emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(color = LightGrey, shape = RoundedCornerShape(10.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$item 목표를 설정해주세요",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        }

        GoalDropdown(label = "하루", options = options)
        GoalDropdown(label = "일주일", options = options)
        GoalDropdown(label = "한달", options = options)
    }
}


@Composable
fun GoalDropdown(label: String, options: List<String>) {
    var selectedOption by remember { mutableStateOf(options.firstOrNull() ?: "") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = LightGrey, shape = RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // 경계 추가
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedOption.ifEmpty { "선택하세요" }, color = Black)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Black
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedOption = option
                            expanded = false
                        },
                        text = { Text(option, color = Black) }
                    )
                }
            }
        }
    }
}



