package com.example.addiction_manage.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetNamePage() {
    var nickname by remember { mutableStateOf("") } // 닉네임 입력 상태

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = LightGrey, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단 문구
            WelcomeText()

            // 닉네임 입력창
            NicknameInputField(
                nickname = nickname,
                onNicknameChange = { nickname = it }
            )

            // 시작하기 버튼
            StartButton(
                nickname = nickname
            )
        }
    }
}



@Composable
fun WelcomeText() {
    Text(
        text = "초기 목표 설정이 완료되었습니다!\n닉네임을 설정하고 중독 관리를 시작해보세요!",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Black,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 24.dp)
    )
}

@Composable
fun NicknameInputField(
    nickname: String,
    onNicknameChange: (String) -> Unit
) {
    OutlinedTextField(
        value = nickname,
        onValueChange = onNicknameChange,
        label = { Text("닉네임 입력", color = Black) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 24.dp)
    )
}

@Composable
fun StartButton( // 메인화면으로 가게 네비게이션
    nickname: String
) {
    Button(
        onClick = {
            println("닉네임 설정: $nickname") // 콘솔 출력
        },
        enabled = nickname.isNotBlank(), // 닉네임이 입력 됐을 때만 활성화
        modifier = Modifier
            .width(150.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = LightRed)
    ) {
        Text(
            text = "시작하기",
            fontSize = 18.sp,
            color = White
        )
    }
}





