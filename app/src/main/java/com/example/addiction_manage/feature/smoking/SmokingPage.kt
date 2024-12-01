package com.example.addiction_manage.feature.smoking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.feature.alcohol.AlcoholDialog2
import com.example.addiction_manage.feature.alcohol.AlcoholDialog3
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.DarkRed
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.MediumGrey
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.WhiteBlue

@Composable
fun SmokingPage(
    navigateToMyPage: () -> Unit,
    navController: NavController,
    navigateToHome:()->Unit,
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


        if (showDialog.value) {  // 상태 변수를 확인하여 대화상자를 표시
            SmokingDialog(onDismiss = { showDialog.value = false }, navigateToHome)  // onDismiss에서 대화상자를 숨김
        }
    }
}

@Composable
fun SmokingDialog(
    onDismiss: () -> Unit,
    navigateToHome: () -> Unit) {

    var count by remember { mutableStateOf(0) }  // 초기 선택값 설정

    Dialog(onDismissRequest = { onDismiss() }) {
        val showDialog = remember { mutableStateOf(true) }
        val showYesDialog = remember { mutableStateOf(false) }
        val showNoDialog = remember { mutableStateOf(false) }

        if (showYesDialog.value) {
            AlcoholDialog2(
                onDismiss = {
                    showYesDialog.value = false
                    onDismiss()  // 부모 다이얼로그를 종료
                },
                navigateToHome = navigateToHome  // 홈으로 이동 함수 전달
            )
        }

        if(showNoDialog.value){
            AlcoholDialog3 (
                onDismiss = {
                    showNoDialog.value = false
                    onDismiss()  // 부모 다이얼로그를 종료
                },
                navigateToHome = navigateToHome  // 홈으로 이동 함수 전
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            shape = RoundedCornerShape(8.dp),
            color = White
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("오늘 얼마나 흡연했나요?", color = MediumBlue, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(60.dp))
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                        // 흡연 횟수 표시
                        Text(
                            text = "$count",
                            fontSize = 30.sp,
                            color = Black,
                            modifier = Modifier.padding(top=10.dp)
                        )
                        Text(
                            text = "  개피    ",
                            fontSize = 20.sp,
                            color = MediumBlue,
                            modifier = Modifier.padding(top=15.dp)
                        )
                    Spacer(modifier = Modifier.width(10.dp))
                    // 흡연 횟수 증가 버튼
                    Button(
                        onClick = { count++ },  // 버튼 클릭 시 count 증가
                        modifier = Modifier.height(60.dp)
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
                        onDismiss()
                        navigateToHome()
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .width(150.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(width=2.dp, color = MediumBlue)
                ) {
                    Text(
                        "닫기",
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(Font(R.font.minsans))
                    )
                }
            }

            }
        }

}