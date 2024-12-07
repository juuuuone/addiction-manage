package com.example.addiction_manage.feature

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.MediumBlue
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StartPage(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var showMyDetoxText by remember { mutableStateOf(false) }
    var showLogoAndDetoxify by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(1000)
        showMyDetoxText = true
        delay(1500)
        showLogoAndDetoxify = true
        delay(1500)
        showButtons = true
    }

    Scaffold(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(top=220.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = showMyDetoxText,
                    enter = fadeIn(animationSpec = tween(1000)),
                    exit = fadeOut(animationSpec = tween(1000))
                ) {
                    Text(
                        text = stringResource(id = R.string.mydetox),
                        fontFamily = FontFamily(Font(R.font.light)),
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                AnimatedVisibility(
                    visible = showLogoAndDetoxify,
                    enter = fadeIn(animationSpec = tween(1000)),
                    exit = fadeOut(animationSpec = tween(1000))
                ) {
                    Row {
                        Text(
                            text = stringResource(id=R.string.detoxify),
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
                }

                Spacer(Modifier.padding(40.dp))

                AnimatedVisibility(
                    visible = showButtons,
                    enter = fadeIn(animationSpec = tween(1000)),
                    exit = fadeOut(animationSpec = tween(1000))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onLoginClick() },
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                        ) {
                            Text(text = stringResource(id=R.string.login), color = Color.White, fontSize = 20.sp)
                        }

                        Button(
                            onClick = { onRegisterClick() },
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                        ) {
                            Text(text = stringResource(id=R.string.k_signin), color = Color.White, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    )
}
