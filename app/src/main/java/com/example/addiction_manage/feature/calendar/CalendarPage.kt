package com.example.addiction_manage.feature.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.Black
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.addiction_manage.ui.theme.LightBlue
import com.example.addiction_manage.ui.theme.WhiteBlue
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.MediumBlue
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.addiction_manage.feature.alcohol.AlcoholGoalViewModel
import com.example.addiction_manage.feature.alcohol.AlcoholViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineGoalViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineViewModel
import com.example.addiction_manage.feature.model.Alcohol
import com.example.addiction_manage.feature.model.Caffeine
import com.example.addiction_manage.feature.model.Smoking
import com.example.addiction_manage.feature.mypage.checkUser
import com.example.addiction_manage.feature.smoking.SmokingGoalViewModel
import com.example.addiction_manage.feature.smoking.SmokingViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAdjusters
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
    navigateToFriends: () -> Unit,
    navigateToMyPage: () -> Unit,
    selectedItem: Int,
    navController: NavController,
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    var nickname: String = currentUser?.let { checkUser(it) }.toString()


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
        ) {
            val currentMonth = YearMonth.now()
            val formatter = DateTimeFormatter.ofPattern("MMMM", Locale.US)
            val formatterKor=DateTimeFormatter.ofPattern("MMMM",Locale.KOREA)
            val monthName = currentMonth.format(formatter)
            val monthNameKor=currentMonth.format(formatterKor)

            Spacer(modifier = Modifier.height(30.dp))
            // 여기에 텍스트를 독립적으로 배치합니다.
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
                    text = " 님의 ${monthNameKor}을 확인해볼까요?",
                    color = Black,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    //.padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .background(color = WhiteBlue, shape = RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Text(text = monthName, fontSize = 32.sp, fontFamily = FontFamily(Font(R.font.medium))) // "December"와 같이 출력됩니다.
                SimpleCalendar()
            }
            Spacer(modifier = Modifier.height(30.dp))

            Row(modifier = Modifier.padding(start=16.dp)){
                Text(text = stringResource(id = R.string.success),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    //.padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .background(color = WhiteBlue, shape = RoundedCornerShape(10.dp)),
            ) {
                WeeklySuccessCalendar()
            }
        }


    }
}


/*달력 생성 함수*/
@Composable
fun SimpleCalendar() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDialog by remember { mutableStateOf(false) }  // Dialog를 보여줄지 여부를 결정하는 상태
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()

    // 선택된 날짜에 대한 대화상자
    if (showDialog) {
        DateDialog(selectedDate) {
            showDialog = false  // 대화상자를 닫을 때 호출
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.padding(16.dp)
    ) {
        items((1..daysInMonth).toList()) { day ->
            val isSelected =
                selectedDate.dayOfMonth == day && selectedDate.month == currentMonth.month

            val date = currentMonth.atDay(day)
            val dayOfWeek = date.get(ChronoField.DAY_OF_WEEK)

            val textColor = when {
                isSelected -> Color.White
                dayOfWeek == 6 -> Color.Blue    // Saturday
                dayOfWeek == 7 -> Color.Red     // Sunday
                else -> Color.Black
            }



            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        selectedDate = currentMonth.atDay(day)
                        showDialog = true  // 날짜를 클릭하면 Dialog를 열기
                    }
                    .padding(4.dp)
                    .background(
                        color = if (isSelected) MediumBlue else Color.Transparent,
                        shape = CircleShape,
                    )
            ) {
                Text(
                    text = day.toString(),
                    color = textColor,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
            }
        }
    }
}

@Composable
fun WeeklySuccessCalendar() {

    val smokingViewModel: SmokingViewModel = hiltViewModel()
    val smokingGoalViewModel: SmokingGoalViewModel = hiltViewModel()
    var smokingGoals = smokingGoalViewModel.goal.collectAsState().value
    var smokingGoalsNum= 0
    if(smokingGoals.isNotEmpty()){
       var smokingGoalsString= smokingGoals.joinToString{it.goal}
        smokingGoalsNum=smokingGoalsString.toInt()
    }

    val caffeineViewModel: CaffeineViewModel = hiltViewModel()
    val caffeineGoalViewModel: CaffeineGoalViewModel = hiltViewModel()
    val caffeineGoals = caffeineGoalViewModel.goal.collectAsState().value
    var caffeineGoalsNum=0
    if(caffeineGoals.isNotEmpty()){
        var caffeineGoalsString= caffeineGoals.joinToString{it.goal}
        caffeineGoalsNum=caffeineGoalsString.toInt()
    }

    LaunchedEffect(key1 = true) {
        smokingViewModel.listenForSmokingRecords()
        caffeineViewModel.listenForCaffeineRecords()
    }

    val smokingRecords = smokingViewModel.smokingRecords.collectAsState().value
    val caffeineRecords = caffeineViewModel.caffeineRecords.collectAsState().value

    val today = LocalDate.now()
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            (0..6).forEach { i ->
                val day = startOfWeek.plusDays(i.toLong())

                // 각 습관에 대한 성공 여부 계산
                val smokingAmount = smokingViewModel.getSmokingRecord(smokingRecords, day)?.cigarettes ?: -1
                val caffeineAmount = caffeineViewModel.getCaffeineRecord(caffeineRecords, day)?.drinks ?: -1
                var dayEmoji=""
                var isSmokingSuccess = false
                var isCaffeineSuccess=false

                if(smokingAmount==-1 && caffeineAmount==-1){
                    dayEmoji=""
                }

                else{
                    if(smokingGoals.isEmpty() || smokingAmount<=smokingGoalsNum){
                        isSmokingSuccess = true
                    }

                    if(caffeineGoals.isEmpty() || caffeineAmount<=caffeineGoalsNum){
                        isCaffeineSuccess = true
                    }

                    dayEmoji = when {
                        isSmokingSuccess && isCaffeineSuccess -> "🤗"
                        else -> "😭"
                    }
                }


                // 날짜 및 상태 이모지 표시
                Box(
                    modifier = Modifier.padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier=Modifier.height(5.dp))
                        Text(
                            text = day.dayOfMonth.toString(),
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 16.sp
                        )
                        Spacer(modifier=Modifier.height(5.dp))
                        Text(
                            text = dayEmoji,
                            fontFamily = FontFamily(Font(R.font.minsans)),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}








/*날짜 클릭하면 뜨는 창 (어떤거 기록할건지)*/
@Composable
fun DateDialog(date: LocalDate, onDismiss: () -> Unit) {
    val alcoholString = stringResource(id = R.string.alcohol)
    val smokingString = stringResource(id = R.string.smoking)
    val caffeineString = stringResource(id = R.string.caffeine)

    val day = date.dayOfMonth
    val currentUser = FirebaseAuth.getInstance().currentUser
    val nickname: String = currentUser?.let { checkUser(it) }.toString()
    var isLoading by remember { mutableStateOf(true) }

    val alcoholViewModel = hiltViewModel<AlcoholViewModel>()
    val alcoholGoalViewModel = hiltViewModel<AlcoholGoalViewModel>()
    val smokingViewModel = hiltViewModel<SmokingViewModel>()
    val smokingGoalViewModel = hiltViewModel<SmokingGoalViewModel>()
    val caffeineViewModel = hiltViewModel<CaffeineViewModel>()
    val caffeineGoalViewModel = hiltViewModel<CaffeineGoalViewModel>()

    LaunchedEffect(key1 = true) {
        alcoholViewModel.listenForAlcoholRecords()
        smokingViewModel.listenForSmokingRecords()
        caffeineViewModel.listenForCaffeineRecords()
    }

    var currentAlcohol by remember { mutableStateOf(true) }
    var hasAlcohol by remember { mutableStateOf(false) }
    var currentSmoking by remember { mutableIntStateOf(0) }
    var hasSmoking by remember { mutableStateOf(false) }
    var currentCaffeine by remember { mutableIntStateOf(0) }
    var hasCaffeine by remember { mutableStateOf(false) }

    var isExistsAlcohol by remember { mutableStateOf<Alcohol?>(null) }
    var isExistsSmoking by remember { mutableStateOf<Smoking?>(null) }
    var isExistsCaffeine by remember { mutableStateOf<Caffeine?>(null) }

    val alcoholRecords = alcoholViewModel.alcoholRecords.collectAsState()
    val hasAlcoholGoal = alcoholGoalViewModel.isAlcoholChecked.collectAsState()
    val smokingRecords = smokingViewModel.smokingRecords.collectAsState()
    val hasSmokingGoal = smokingGoalViewModel.isSmokingChecked.collectAsState()
    val caffeineRecords = caffeineViewModel.caffeineRecords.collectAsState()
    val hasCaffeineGoal = caffeineGoalViewModel.isCaffeineChecked.collectAsState()

    LaunchedEffect(
        alcoholRecords.value,
        smokingRecords.value,
        caffeineRecords.value,
        hasAlcoholGoal.value,
        hasSmokingGoal.value,
        hasCaffeineGoal.value,
    ) {
        if (alcoholRecords.value.isNotEmpty() && smokingRecords.value.isNotEmpty() && caffeineRecords.value.isNotEmpty()) {
            isLoading = false
        }
        if (alcoholRecords.value.isNotEmpty()) {
            currentAlcohol =
                alcoholViewModel.getAlcoholRecord(alcoholRecords.value, date)?.doDrink ?: false
            isExistsAlcohol =
                alcoholViewModel.getAlcoholRecord(alcoholRecords.value, date)
        }
        if (smokingRecords.value.isNotEmpty()) {
            currentSmoking =
                smokingViewModel.getSmokingRecord(smokingRecords.value, date)?.cigarettes ?: 0
            isExistsSmoking =
                smokingViewModel.getSmokingRecord(smokingRecords.value, date)
        }
        if (caffeineRecords.value.isNotEmpty()) {
            currentCaffeine =
                caffeineViewModel.getCaffeineRecord(caffeineRecords.value, date)?.drinks ?: 0
            isExistsCaffeine =
                caffeineViewModel.getCaffeineRecord(caffeineRecords.value, date)
        }
        if (!hasAlcoholGoal.value) {
            hasAlcohol = true
        }
        if (!hasSmokingGoal.value) {
            hasSmoking = true
        }
        if (!hasCaffeineGoal.value) {
            hasCaffeine = true
        }
    }

    val mediumFont = FontFamily(Font(R.font.medium))

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f) // 모달창 크기
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(16.dp),
            color = White,
            border = BorderStroke(2.dp, color = MediumBlue)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // 닫기 버튼
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close),
                            tint = Color.Gray
                        )
                    }
                }

                // 타이틀
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$nickname ${
                            stringResource(id = R.string.days1)
                        } ",
                        color = DarkGray,
                        fontFamily = mediumFont,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "$day${
                            stringResource(
                                id = R.string.days2
                            )
                        }",
                        color = DarkGray,
                        fontFamily = mediumFont,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 음주 기록
                if (hasAlcohol) {
                    Text(
                        text = stringResource(id = R.string.soju) + " 🍺",
                        fontSize = 25.sp,
                        fontFamily = mediumFont,
                        color = MediumBlue
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isExistsAlcohol != null) {
                            if (currentAlcohol) stringResource(id = R.string.alcohol) else stringResource(
                                id = R.string.no_alcohol
                            )
                        } else stringResource(id = R.string.no_record),
                        fontSize = 25.sp,
                        fontFamily = mediumFont,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                if (hasSmoking) {
                    // 흡연 기록
                    Text(
                        text = "$smokingString 🚬",
                        fontSize = 25.sp,
                        fontFamily = mediumFont,
                        color = MediumBlue
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isExistsSmoking != null) currentSmoking.toString() + stringResource(
                            id = R.string.gp
                        ) else "기록 없음",
                        fontSize = 25.sp,
                        fontFamily = mediumFont,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                if (hasCaffeine) {
                    // 카페인 기록
                    Text(
                        text = "$caffeineString ☕",
                        fontSize = 25.sp,
                        fontFamily = mediumFont,
                        color = MediumBlue
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = if (isExistsCaffeine != null) currentCaffeine.toString() + stringResource(
                            id = R.string.cup
                        ) else "기록 없음", // 예시로 "2잔" 표시
                        fontSize = 25.sp,
                        fontFamily = mediumFont,
                        color = Color.Black
                    )
                }


                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}


/*탑바*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(
    navigateUp: () -> Unit,
    navigateToMyPage: () -> Unit,
) {
    TopAppBar(
        title = {
            Row {
                Text(
                    text = "DeToxify",
                    fontFamily = FontFamily(Font(R.font.bold)),
                    //modifier = Modifier.height(70.dp),
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
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        actions = {
            // 여기에 사용자 정의 아이콘을 추가
            IconButton(onClick = navigateToMyPage) {
                Icon(
                    //painter = painterResource(id = R.drawable.account_circle_24px),
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "MypageButton",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = BackgroundColor, // TopAppBar의 배경색
            titleContentColor = Black, // 제목의 색상
            actionIconContentColor = Black, // 액션 아이콘 색상
            navigationIconContentColor = Black
        )
    )
}


/*바텀바*/
@Composable
fun BottomAppBarComponent(
    navController: NavController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val selectedItem = when (currentRoute) {
        "calendar" -> 0
        "home" -> 1
        "statistic" -> 2
        "friends" -> 3
        else -> -1
    }

    BottomAppBar(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(),
        containerColor = BackgroundColor,
        contentColor = Black
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TabItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.calendar),
                        color = if (selectedItem == 0) Black else LightGray,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_month_24px),
                        contentDescription = "Calendar",
                        tint = if (selectedItem == 0) Black else LightGray
                    )
                },
                isSelected = selectedItem == 0,
                onSelect = { navController.navigate("calendar") },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.home),
                        color = if (selectedItem == 1) Black else LightGray,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.home_24px),
                        contentDescription = "Home",
                        tint = if (selectedItem == 1) Black else LightGray
                    )
                },
                isSelected = selectedItem == 1,
                onSelect = { navController.navigate("home") },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.statistics),
                        color = if (selectedItem == 2) Black else LightGray,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.database_24px),
                        contentDescription = "Statistic",
                        tint = if (selectedItem == 2) Black else LightGray
                    )
                },
                isSelected = selectedItem == 2,
                onSelect = { navController.navigate("statistic") },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.friends),
                        color = if (selectedItem == 3) Black else LightGray,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.group_24px),
                        contentDescription = "Friends",
                        tint = if (selectedItem == 3) Black else LightGray
                    )
                },
                isSelected = selectedItem == 3,
                onSelect = { navController.navigate("friends") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


/*캘린더 탭*/
@Composable
fun TabItem(
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(0.dp)
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .background(if (isSelected) White else White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        text()
    }
}

