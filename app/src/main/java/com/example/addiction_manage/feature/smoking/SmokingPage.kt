package com.example.addiction_manage.feature.smoking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.DarkRed
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.MediumGrey

@Composable
fun SmokingPage(
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
            SmokingDialog(onDismiss = { showDialog.value = false })  // onDismiss에서 대화상자를 숨김
        }
    }
}

@Composable
fun SmokingDialog(onDismiss: () -> Unit) {

    val alcoholOptions = listOf("1~2개피", "3~5개피", "반 갑", "3/4갑", "한 갑")
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
                Text("오늘의 흡연량은 어떻게 되나요?", color = DarkRed, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(32.dp))
                Row() {
                    Text("나의 목표 흡연량   ", fontSize = 20.sp)
                    Text(
                        "5 개피",
                        color = DarkRed,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
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