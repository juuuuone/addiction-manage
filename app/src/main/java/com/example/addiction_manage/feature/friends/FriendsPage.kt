package com.example.addiction_manage.feature.friends


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.feature.alcohol.AlcoholGoalViewModel
import com.example.addiction_manage.feature.alcohol.AlcoholViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineGoalViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineViewModel
import com.example.addiction_manage.feature.calendar.BottomAppBarComponent
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.feature.mypage.checkUser
import com.example.addiction_manage.feature.smoking.SmokingGoalViewModel
import com.example.addiction_manage.feature.smoking.SmokingViewModel
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.LightBlue
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.MediumRed
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.WhiteBlue
import com.example.addiction_manage.ui.theme.WhiteRed
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FriendsPage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
    navigateToFriends:()->Unit,
    navigateToMyPage: () -> Unit,
    selectedItem: Int,
    navController: NavController,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBarComponent(
                navigateUp = { navController.navigateUp() },
                navigateToMyPage = navigateToMyPage,
            )
        },
        bottomBar = {
            BottomAppBarComponent(
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .padding(top = 40.dp)
                .background(color = White, shape = RoundedCornerShape(10.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ComparePage()
        }
    }
}

@Composable
fun ComparePage(

) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var nickname: String = currentUser?.let { checkUser(it) }.toString()
    var showDialog by remember { mutableStateOf(false) }  // 다이얼로그 표시 상태

    if (showDialog) {
        SelectFriends { showDialog = false }  // 다이얼로그 닫기
    }



    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
                Row(modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End){
                    OutlinedButton(onClick = { showDialog = true },
                        border = BorderStroke(3.dp, MediumBlue),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = MediumBlue
                        )) {
                        Text(text = stringResource(id =R.string.select_friends ),
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 20.sp
                        )

                    }
                }

            Spacer(modifier = Modifier.height(10.dp))

            Column (modifier= Modifier
                .background(color = WhiteBlue, shape = RoundedCornerShape(10.dp))
                .fillMaxWidth()
                .height(240.dp),){
                Row() {
                    Text(
                        text = "$nickname",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.bold)),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .padding(top = 16.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.status),
                        color = Black,
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.minsans)),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .padding(top = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(id = R.string.al_status),
                    color = MediumBlue,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = stringResource(id = R.string.sm_status),
                    color = MediumRed,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = stringResource(id = R.string.ca_status),
                    color = MediumBlue,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }



            Spacer(modifier = Modifier.height(50.dp))


        Column (modifier= Modifier
            .background(color = WhiteRed, shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(240.dp)){
            Row() {
                Text(
                    text = "friend",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = stringResource(id = R.string.status),
                    color = Black,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.al_status),
                color = MediumRed,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.minsans)),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(top = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.sm_status),
                color = MediumBlue,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.minsans)),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(top = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.ca_status),
                color = MediumRed,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.minsans)),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .padding(top = 16.dp)
                    .padding(bottom = 16.dp)
            )
        }
        }

    }
}


@Composable
fun SelectFriends(onDismiss: () -> Unit) {
    val friendsList = listOf("Alice", "Bob", "Charlie")  // 친구 목록 예시
    var expanded by remember { mutableStateOf(false) }  // 드롭다운 상태
    var selectedFriend by remember { mutableStateOf("") }  // 선택된 친구
    var showAddFriendDialog by remember { mutableStateOf(false) } // 친구 추가 모달창

    // AddNewFriendDialog 다이얼로그
    if (showAddFriendDialog) {
        AddNewFriendDialog(
            onDismiss = { showAddFriendDialog = false },
            onAddFriend = { email ->
                showAddFriendDialog = false
            }
        )
    }


    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),  // 모달창 크기 조절
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(2.dp, MediumBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start // 왼쪽 정렬
            ) {
                // 제목
                Text(
                    text = stringResource(id = R.string.select_friends),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 16.dp)
                )

                // 드롭다운 버튼
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    border = BorderStroke(3.dp, MediumBlue),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = White,
                        contentColor = MediumBlue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (selectedFriend.isEmpty()) stringResource(id = R.string.select_friends) else selectedFriend,
                        fontFamily = FontFamily(Font(R.font.minsans)),
                        fontSize = 18.sp
                    )
                }

                // 드롭다운 메뉴
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    friendsList.forEach { friend ->
                        DropdownMenuItem(onClick = {
                            selectedFriend = friend
                            expanded = false
                        }) {
                            Text(
                                text = friend,
                                fontFamily = FontFamily(Font(R.font.minsans)),
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 친구 추가 버튼
                Button(
                    onClick = { showAddFriendDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MediumBlue,
                        contentColor = White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.add_friends),
                        fontFamily = FontFamily(Font(R.font.minsans)),
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                // 닫기 버튼

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd // 우측 하단 정렬
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGrey,
                            contentColor = Black
                        ),
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.close),
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 18.sp
                        )
                    }
                }

            }
        }
    }
}

// 친구 추가 누르면 나오는 모달창
@Composable
fun AddNewFriendDialog(onDismiss: () -> Unit, onAddFriend: (String) -> Unit) {
    var email by remember { mutableStateOf("") } // 입력값 상태

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f), // 모달창 크기
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(2.dp, MediumBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start // 왼쪽 정렬
            ) {
                // 제목 : 상단 왼쪽
                Text(
                    text = stringResource(id = R.string.add_friends),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 16.dp)
                )

                // 입력창
                androidx.compose.material3.OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id =R.string.friends_email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.weight(1f)) // 여백 추가

                // 저장, 닫기 버튼
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onDismiss() // 다이얼로그 닫기
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGrey,
                            contentColor = Black
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id =R.string.close),
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(
                        onClick = {
                            onAddFriend(email) // 입력값 전달
                            onDismiss() // 다이얼로그 닫기
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MediumBlue,
                            contentColor = White
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id =R.string.save_button),
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}
