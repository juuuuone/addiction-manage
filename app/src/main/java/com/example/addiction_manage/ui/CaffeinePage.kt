package com.example.addiction_manage.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.DarkRed
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.MediumGrey
import java.time.YearMonth

@Composable
fun CaffeinePage(
    navigateToMyPage: () -> Unit,
    navController: NavController
) {
    val showDialog = remember { mutableStateOf(true) }  // 대화상자를 표시할지 여부를 제어하는 상태

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBarComponent(
                navigateToMyPage = navigateToMyPage,
                navigateUp = { navController.navigateUp() }
            )
        },
    ) { innerPadding ->

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.8f)
//                .padding(innerPadding)
//                .padding(horizontal = 8.dp)
//                .padding(top = 150.dp)
//                .background(color = LightGrey, shape = RoundedCornerShape(10.dp)),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            val currentMonth = YearMonth.now()
//            Text(text = currentMonth.toString(), fontSize = 32.sp)
//            SimpleCalendar()
//        }

        if (showDialog.value) {  // 상태 변수를 확인하여 대화상자를 표시
            CaffeineDialog1(onDismiss = { showDialog.value = false })  // onDismiss에서 대화상자를 숨김
        }
    }
}

@Composable
fun CaffeineDialog1(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        val showDialog = remember { mutableStateOf(true) }
        val showNextDialog = remember { mutableStateOf(false) }

        if (showNextDialog.value) {
            CaffeineDialog2 {
                showNextDialog.value = false
                onDismiss() // 부모 다이얼로그를 종료
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            shape = RoundedCornerShape(8.dp),
            color = MediumGrey
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("오늘 어떤 음료를 마셨나요?", color = DarkRed, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(48.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = {
                            showDialog.value = false
                            showNextDialog.value = true // 다음 대화상자로 이동 플래그 설정
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(70.dp)
                            .height(70.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LightGrey),
                        shape = CircleShape
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.coffee),
                            contentDescription = "커피",
                            modifier = Modifier.size(200.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(40.dp))

                    Button(
                        onClick = {
                            showDialog.value = false
                            showNextDialog.value = true
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(70.dp)
                            .height(70.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LightGrey),
                        shape = CircleShape
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.energy),
                            contentDescription = "에너지드링크",
                            modifier = Modifier.size(150.dp)
                        )
                    }

                }

                Row(
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                ) {
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(text = "커피", color = LightRed, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(54.dp))
                    Text(text = "에너지드링크", color = LightRed, fontSize = 18.sp)
                }

            }
        }
    }
}


@Composable
fun CaffeineDialog2(onDismiss: () -> Unit) {

    val alcoholOptions = listOf("반 잔", "한 잔", "두 잔", "세 잔")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(alcoholOptions[0]) }  // 초기 선택값 설정

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            shape = RoundedCornerShape(8.dp),
            color = MediumGrey
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("오늘의 섭취량은 어떻게 되나요?", color = DarkRed, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Row() {
                    Text("나의 목표 섭취량   ", fontSize = 18.sp)
                    Text(
                        "한 잔",
                        color = DarkRed,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text("(300mg)", color = DarkRed, fontSize = 18.sp)

                }

                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { expanded = true },
                    modifier = Modifier.width(250.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightGrey),
                    shape = RectangleShape
                ) {
                    Text(
                        "                             ∨",
                        color = Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                DropdownMenu(
                    modifier = Modifier.width(250.dp),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(x = 27.dp, y = (-270).dp)
                ) {
                    alcoholOptions.forEach { label ->
                        DropdownMenuItem(onClick = {
                            selectedOption = label
                            expanded = false
                        }) {
                            Text(text = label)
                        }
                    }
                }
            }
        }
    }
}