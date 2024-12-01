package com.example.addiction_manage.feature.alcohol

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.MediumGrey
import com.example.addiction_manage.ui.theme.DarkRed

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.WhiteBlue


@Composable
fun AlcoholPage(
    navigateToMyPage: () -> Unit,
    navController: NavController,
    navigateToHome: () -> Unit
) {
    val showDialog = remember { mutableStateOf(true) }  // 대화상자를 표시할지 여부를 제어하는 상태
    val yesterday = remember { mutableStateOf(false) }
    val today = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBarComponent(
                navigateToMyPage = navigateToMyPage,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },
    ) { innerPadding ->

        if (showDialog.value) {  // 상태 변수를 확인하여 대화상자를 표시
            AlcoholDialog1(onDismiss = { showDialog.value = false },
            navigateToHome=navigateToHome)  // onDismiss에서 대화상자를 숨김
        }
    }
}

@Composable
fun AlcoholDialog1(
    onDismiss: () -> Unit,
    navigateToHome: () -> Unit
) {
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
                .fillMaxHeight(0.2f),
            shape = RoundedCornerShape(8.dp),
            color = White
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("오늘 음주를 했나요?", color = MediumBlue, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(60.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            showDialog.value = false
                            showYesDialog.value = true // '네'를 클릭했을 때 다음 다이얼로그 표시
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(150.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WhiteBlue),
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            "네",
                            color = Black,
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.minsans))
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Button(
                        onClick = {
                            showDialog.value = false
                            showNoDialog.value = true
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .width(150.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "아니요",
                            fontSize = 20.sp,
                            color = White,
                            fontFamily = FontFamily(Font(R.font.minsans))
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))


                }

            }
        }
    }
}



@Composable
fun AlcoholDialog2(
    onDismiss: () -> Unit,
    navigateToHome: () -> Unit  // 인자 추가
) {

    val alcoholOptions = listOf("1~2잔", "반 병", "한 병", "한 병 반", "두 병")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(alcoholOptions[0]) }  // 초기 선택값 설정

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            shape = RoundedCornerShape(8.dp),
            color = White
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("내일은 금주해봐요!", color = MediumBlue, fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
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


@Composable
fun AlcoholDialog3(
    onDismiss: () -> Unit,
    navigateToHome: () -> Unit  // 인자 추가
) {

    val alcoholOptions = listOf("1~2잔", "반 병", "한 병", "한 병 반", "두 병")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(alcoholOptions[0]) }  // 초기 선택값 설정

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f),
            shape = RoundedCornerShape(8.dp),
            color = White
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Text("잘하셨어요! 내일도 기대할게요!", color = MediumBlue, fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
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
