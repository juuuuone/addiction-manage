package com.example.addiction_manage.ui.auth.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.MediumBlue
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    backToMainPage: () -> Unit,
    navController: NavController
){
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()
    var name by remember{ mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember{ mutableStateOf("") }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) { // key 값이 바뀌면 이걸 먼저 수행
        when(uiState.value){
            is SignUpState.Success -> {
                navController.navigate("alcohol-goal")
            }
            is SignUpState.Error -> {
                Toast.makeText(context, "Sign Up Failed", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row {
                        Text(
                            text = "DeToxify",
                            fontFamily = FontFamily(Font(R.font.bold)),
                            fontSize = 30.sp
                        )
                        Image(
                            painter = painterResource(id = R.drawable.leaf),
                            contentDescription = "Logo",
                            modifier = Modifier.height(40.dp) // 이미지 높이 조절
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = backToMainPage) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                        )
                    }
                },
            )
        }

    ) { innerPadding ->

        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
                .padding(top=50.dp) ,

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Row(){
                Text("회원가입하고 중독을 ",fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
                Text("DeTox",fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.bold))
                )
                Text(" 해보세요!",fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {name = it},
                label = {
                    Text("닉네임",
                        fontFamily = FontFamily(Font(R.font.minsans)))
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {email = it},
                label = {
                    Text("이메일",
                        fontFamily = FontFamily(Font(R.font.minsans)))
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = {password = it},
                label = {
                    Text("비밀번호",
                        fontFamily = FontFamily(Font(R.font.minsans)))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = confirm,
                onValueChange = {confirm = it},
                label = {
                    Text("비밀번호 확인",
                        fontFamily = FontFamily(Font(R.font.minsans)))
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = password.isNotEmpty() && confirm.isNotEmpty() && confirm != password
            )
            Spacer(modifier = Modifier.height(20.dp))

            if(uiState.value == SignUpState.Loading){
                CircularProgressIndicator() // 동그라미 로딩 창
            }else{
                Button(
                    onClick = {viewModel.signUp(name, email, password)},
                    modifier = Modifier.fillMaxWidth()
                        .height(40.dp),
                    enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && password == confirm,
                    colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                ){
                    Text(text ="회원가입", fontSize=16.sp)
                }
                TextButton(
                    onClick = {navController.navigate("signin")},
                    modifier = Modifier.fillMaxWidth()
                        .height(40.dp)
                ){
                    Text(text = "이미 계정이 있다면? 로그인하기", color= MediumBlue, fontSize = 16.sp)
                }
            }
        }
    }
}