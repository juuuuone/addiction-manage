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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.addiction_manage.feature.mypage.checkUser
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
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
                navigateToCalendar = navigateToCalendar,
                navigateToHome = navigateToHome,
                navigateToStatistic = navigateToStatistic,
                isCalendarPage = true,
                selectedItem = selectedItem,
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            // 여기에 텍스트를 독립적으로 배치합니다.
            Row(){
                Text(
                    text="$nickname",
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.bold)),
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(top = 16.dp)
                )
                Text(
                    text = " 님의 12월을 확인해볼까요?",
                    color = Black,
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .padding(top = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(60.dp))

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

                val currentMonth = YearMonth.now()
                Text(text = currentMonth.toString(), fontSize = 32.sp)
                SimpleCalendar()
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
                    color = if (isSelected) Color.White else Color.Black
                )
            }
        }
    }
}


/*날짜 클릭하면 뜨는 창 (어떤거 기록할건지)*/
@Composable
fun DateDialog(date: LocalDate, onDismiss: () -> Unit) {
    val day = date.dayOfMonth
    val currentUser = FirebaseAuth.getInstance().currentUser
    var nickname: String = currentUser?.let { checkUser(it) }.toString()

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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$nickname ${stringResource(id = R.string.days1)} $day${stringResource(id = R.string.days2)}",
                        color = DarkGray,
                        fontFamily = mediumFont,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))


                // 음주 기록
                Text(
                    text = stringResource(id = R.string.alcohol) + " 🍺",
                    fontSize = 25.sp,
                    fontFamily = mediumFont,
                    color = MediumBlue
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text =  "3" + stringResource(id = R.string.cup), // 예시로 "3잔" 표시
                    fontSize = 25.sp,
                    fontFamily = mediumFont,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))

                // 흡연 기록
                Text(
                    text = stringResource(id = R.string.smoking) + " 🚬",
                    fontSize = 25.sp,
                    fontFamily = mediumFont,
                    color = MediumBlue
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "5"+ stringResource(id = R.string.gp), // 예시로 "5개피" 표시
                    fontSize = 25.sp,
                    fontFamily = mediumFont,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))

                // 카페인 기록
                Text(
                    text = stringResource(id = R.string.caffeine) + " ☕",
                    fontSize = 25.sp,
                    fontFamily = mediumFont,
                    color = MediumBlue
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "2" + stringResource(id = R.string.cup), // 예시로 "2잔" 표시
                    fontSize = 25.sp,
                    fontFamily = mediumFont,
                    color = Color.Black
                )

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
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
    selectedItem: Int,
    isCalendarPage: Boolean = false,
    isHomePage: Boolean = false,
    isStatisticPage: Boolean = false,
) {
    val painter: Painter = painterResource(id = R.drawable.bar_chart_24px)  // Drawable 리소스 ID

    BottomAppBar(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(), // 하단 바를 전체 너비로 설정
        containerColor = BackgroundColor, // 기본 하단 바의 배경색 설정
        contentColor = Black // 아이콘 및 텍스트 색상
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
                        text = stringResource(id= R.string.calendar),
                        color = if (selectedItem == 0) Black else LightGray,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_month_24px),
                        contentDescription = "Calender",
                        tint = if (selectedItem == 0) Black else LightGray
                    )
                },
                isSelected = isCalendarPage,
                onSelect = {
                    navigateToCalendar()
                },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = {
                    Text(
                        text = stringResource(id= R.string.home),
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
                isSelected = isHomePage,
                onSelect = {
                    navigateToHome()
                },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                text = {
                    Text(
                        text = stringResource(id= R.string.statistics),
                        color = if (selectedItem == 2) Black else LightGray,
                        fontSize = 12.sp
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.database_24px),
                        contentDescription = "Bar Chart",
                        tint = if (selectedItem == 2) Black else LightGray
                    )
                },
                isSelected = isStatisticPage,
                onSelect = {
                    navigateToStatistic()
                },
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

