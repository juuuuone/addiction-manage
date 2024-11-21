package com.example.addiction_manage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartPage() {
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BackgroundColor,
                    titleContentColor = White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = LightGrey, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 환영 문구
            Text(
                text = "중독 관리 어플에 오신 것을 환영합니다!\n원하시는 관리 항목을 선택해주세요.\n(중복 선택 가능)",
                fontSize = 18.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 80.dp)
            )

            // 체크박스 리스트
            CheckboxList()

            // 다음 버튼
            Button(
                onClick = { /* 다음 동작 구현 */ },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(150.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightRed)
            ) {
                Text(text = "다음", fontSize = 18.sp, color = White)
            }
        }
    }
}

@Composable
fun CheckboxList() {
    // 체크박스 상태 관리
    var isCaffeineChecked by remember { mutableStateOf(false) }
    var isAlcoholChecked by remember { mutableStateOf(false) }
    var isSmokingChecked by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        // 체크박스
        CheckboxWithBorder(
            label = "카페인",
            isChecked = isCaffeineChecked,
            onCheckedChange = { isCaffeineChecked = it }
        )
        CheckboxWithBorder(
            label = "음주",
            isChecked = isAlcoholChecked,
            onCheckedChange = { isAlcoholChecked = it }
        )
        CheckboxWithBorder(
            label = "흡연",
            isChecked = isSmokingChecked,
            onCheckedChange = { isSmokingChecked = it }
        )
    }
}

@Composable
fun CheckboxWithBorder(label: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)) // 경계 추가
            .background(color = White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Red,
                uncheckedColor = Color.Gray
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Black, fontSize = 16.sp)
    }
}

