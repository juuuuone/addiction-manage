package com.example.addiction_manage.feature

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.MediumBlue

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StartPage(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Scaffold(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text="나의 모든 중독을 DeTox하다",
                    fontFamily= FontFamily(Font(R.font.light)),
                    fontSize = 24.sp
                )
                Spacer(modifier=Modifier.height(60.dp))
                Row {
                    androidx.compose.material3.Text(
                        text = "DeToxify",
                        fontFamily = FontFamily(Font(R.font.bold)),
                        //modifier = Modifier.height(70.dp),
                        fontSize = 40.sp
                    )
                    Image(
                        painter = painterResource(id = R.drawable.leaf),
                        contentDescription = "Logo",
                        modifier = Modifier.height(50.dp) // 이미지 높이 조절
                    )
                }

                Spacer(Modifier.padding(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { onLoginClick() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                    ) {
                        Text(text = "로그인", color = Color.White,
                            fontSize = 20.sp)
                    }

                    Button(
                        onClick = { onRegisterClick() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),

                        ) {
                        Text(text = "회원가입", color = Color.White,
                            fontSize = 20.sp)
                    }
                }
            }
        }
    )
}