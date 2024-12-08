package com.example.addiction_manage.feature.friends


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
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
import com.example.addiction_manage.ui.theme.getColorBasedOnAlcoholWin
import com.example.addiction_manage.ui.theme.getColorBasedOnMyScore
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FriendsPage(
    navigateToMyPage: () -> Unit,
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
    val nickname = currentUser?.let { checkUser(it) }.toString()
    var showDialog by remember { mutableStateOf(false) }

    val friendDataViewModel = hiltViewModel<FriendDataViewModel>()
    val alcoholViewModel = hiltViewModel<AlcoholViewModel>()
    val smokingViewModel = hiltViewModel<SmokingViewModel>()
    val caffeineViewModel = hiltViewModel<CaffeineViewModel>()

    var myAlcohol by remember { mutableStateOf(false) }
    var mySmoking by remember { mutableStateOf(0) }
    var myCaffeine by remember { mutableStateOf(0) }
    var friendAlcohol by remember { mutableStateOf(false) }
    var friendSmoking by remember { mutableStateOf(0) }
    var friendCaffeine by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = true) {
        friendDataViewModel.listenForUsers()
        alcoholViewModel.listenForAlcoholRecords()
        smokingViewModel.listenForSmokingRecords()
        caffeineViewModel.listenForCaffeineRecords()
    }
    val users = friendDataViewModel.users.collectAsState()
    val alcoholRecords = alcoholViewModel.alcoholRecords.collectAsState()
    val smokingRecords = smokingViewModel.smokingRecords.collectAsState()
    val caffeineRecords = caffeineViewModel.caffeineRecords.collectAsState()
    LaunchedEffect(
        key1 = alcoholRecords.value,
        key2 = smokingRecords.value,
        key3 = caffeineRecords.value
    ) {
        myAlcohol =
            alcoholViewModel.getTodayAlcoholRecord(alcoholRecords.value)?.doDrink ?: false
        mySmoking =
            smokingViewModel.getTodaySmokingRecord(smokingRecords.value)?.cigarettes ?: 0
        myCaffeine =
            caffeineViewModel.getTodayCaffeineRecord(caffeineRecords.value)?.drinks ?: 0
    }

    var friendNickname by remember { mutableStateOf("") }
    val friendsList = friendDataViewModel.getAllFriendsNickname(users.value) ?: emptyList()
    val friendId = friendDataViewModel.getFriendId(users.value, friendNickname)
    LaunchedEffect(users.value) {
        friendNickname = friendDataViewModel.getDefaultFriendNickname(users.value)
    }
    LaunchedEffect(friendNickname) {
        alcoholViewModel.listenForFriendAlcoholRecords(friendId)
        smokingViewModel.listenForFriendSmokingRecords(friendId)
        caffeineViewModel.listenForFriendCaffeineRecords(friendId)
        friendDataViewModel.setDefaultFriend(users.value, friendId)
    }
    val friendAlcoholRecords = alcoholViewModel.friendAlcoholRecords.collectAsState()
    val friendSmokingRecords = smokingViewModel.friendSmokingRecords.collectAsState()
    val friendCaffeineRecords = caffeineViewModel.friendCaffeineRecords.collectAsState()
    LaunchedEffect(
        key1 = friendAlcoholRecords.value,
        key2 = friendSmokingRecords.value,
        key3 = friendCaffeineRecords.value
    ) {
        friendAlcohol =
            alcoholViewModel.getTodayAlcoholRecord(friendAlcoholRecords.value)?.doDrink ?: false
        friendSmoking =
            smokingViewModel.getTodaySmokingRecord(friendSmokingRecords.value)?.cigarettes ?: 0
        friendCaffeine =
            caffeineViewModel.getTodayCaffeineRecord(friendCaffeineRecords.value)?.drinks ?: 0
    }

    if (showDialog) {
        SelectFriends(
            onDismiss = { showDialog = false },
            setFriendNickname = {
                friendNickname = it
            },
            friendNickname = friendNickname,
            friendsList = friendsList,
            addFriend = {
                friendDataViewModel.addFriend(users.value, it)
                friendDataViewModel.listenForUsers()
            }
        )
    }

    var friendScore=0
    var myScore=0

    var myAlcoholWin: Boolean? = null
    var mySmokingWin: Boolean? = null
    var myCaffeineWin: Boolean? = null
    var friendAlcoholWin: Boolean? = null
    var friendSmokingWin: Boolean? = null
    var friendCaffeineWin: Boolean? = null

    if(myAlcohol && !friendAlcohol){
        //난 술마시고 친구는 안마심
        friendScore++
        friendAlcoholWin=true
        myAlcoholWin=false
    }
    else if(!myAlcohol && friendAlcohol){
        myScore++
        myAlcoholWin=true
        friendAlcoholWin=false
    }

    if(mySmoking>friendSmoking){
        friendScore++
        friendSmokingWin=true
        mySmokingWin=false
    }
    else if(mySmoking<friendSmoking){
        myScore++
        mySmokingWin=true
        friendSmokingWin=false
    }

    if(myCaffeine>friendCaffeine){
        friendScore++
        friendCaffeineWin=true
        myCaffeineWin=false
    }
    else if(myCaffeine<friendCaffeine){
        myScore++
        myCaffeineWin=true
        friendCaffeineWin=false
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        shape = RoundedCornerShape(8.dp),
        color = BackgroundColor
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { showDialog = true },
                    border = BorderStroke(3.dp, MediumBlue),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = White,
                        contentColor = MediumBlue
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.select_friends),
                        fontFamily = FontFamily(Font(R.font.minsans)),
                        fontSize = 20.sp
                    )

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .background(color = getColorBasedOnMyScore(myScore>friendScore), shape = RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .height(240.dp),
            ) {
                Row() {
                    Text(
                        text = nickname,
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
                    text = "🍺 : " + if (myAlcohol) stringResource(
                        id = R.string.alcohol
                    ) else stringResource(id = R.string.no_alcohol),
                    color = getColorBasedOnAlcoholWin(myAlcoholWin),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = "🚬 : $mySmoking" + stringResource(
                        id = R.string.gp
                    ),
                    color = getColorBasedOnAlcoholWin(mySmokingWin),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = "☕ : $myCaffeine" + stringResource(
                        id = R.string.cup
                    ),
                    color = getColorBasedOnAlcoholWin(myCaffeineWin),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .padding(top = 16.dp)
                        .padding(bottom = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            if (friendNickname.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .background(color = getColorBasedOnMyScore(friendScore>myScore), shape = RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    Row() {
                        Text(
                            text = friendNickname,
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
                        text = "🍺 : " + if (friendAlcohol) stringResource(
                            id = R.string.alcohol
                        ) else stringResource(id = R.string.no_alcohol),
                        color = getColorBasedOnAlcoholWin(friendAlcoholWin),
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.minsans)),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(top = 16.dp)
                    )
                    Text(
                        text = "🚬 : $friendSmoking" + stringResource(
                            id = R.string.gp
                        ),
                        color = getColorBasedOnAlcoholWin(friendSmokingWin),
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.minsans)),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(top = 16.dp)
                    )
                    Text(
                        text = "☕ : $friendCaffeine" + stringResource(
                            id = R.string.cup
                        ),
                        color = getColorBasedOnAlcoholWin(friendCaffeineWin),
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
}


@Composable
fun SelectFriends(
    onDismiss: () -> Unit,
    friendNickname: String,
    setFriendNickname: (String) -> Unit,
    friendsList: List<String>,
    addFriend: (String) -> Unit,
) {
    val friendDataViewModel: FriendDataViewModel = hiltViewModel()

    var expanded by remember { mutableStateOf(false) }  // 드롭다운 상태
    var showAddFriendDialog by remember { mutableStateOf(false) } // 친구 추가 모달창

    LaunchedEffect(Unit) {
        friendDataViewModel.listenForUsers() // ViewModel에서 친구 목록 감시 시작
    }

    // AddNewFriendDialog 다이얼로그

    if (showAddFriendDialog) {
        AddNewFriendDialog(
            onDismiss = { showAddFriendDialog = false },
            onAddFriend = {
                addFriend(it)
                showAddFriendDialog = false
                friendDataViewModel.listenForUsers() // 친구 추가 후 목록 새로고침
            }
        )
    }


    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(2.dp, MediumBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(id = R.string.select_friends),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 16.dp)
                )
                Box {
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
                            text = if (friendNickname.isEmpty()) stringResource(id = R.string.select_friends) else friendNickname,
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 18.sp
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .background(White)
                            .border(0.dp, Color.Transparent)
                            .shadow(0.dp)
                    ) {
                        friendsList.forEach { friend ->
                            DropdownMenuItem(
                                onClick = {
                                    setFriendNickname(friend)
                                    expanded = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(White, RoundedCornerShape(12.dp))
                            ) {
                                Text(
                                    text = friend,
                                    fontFamily = FontFamily(Font(R.font.minsans)),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    color = MediumBlue,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MediumBlue,
                            contentColor = White
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

@Composable
fun AddNewFriendDialog(
    onDismiss: () -> Unit,
    onAddFriend: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(2.dp, MediumBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(id = R.string.add_friends),
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier
                        .padding(start = 8.dp, bottom = 16.dp)
                )
                androidx.compose.material3.OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(id = R.string.friends_email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightGrey,
                            contentColor = Black
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.close),
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp))
                    Button(
                        onClick = {
                            onAddFriend(email)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MediumBlue,
                            contentColor = White
                        ),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.save_button),
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}
