package com.example.addiction_manage.ui

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.addiction_manage.ui.theme.LightBlue
import com.example.addiction_manage.ui.theme.WhiteBlue
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.MediumBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
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
                navigateToCalendar = navigateToCalendar,
                navigateToHome = navigateToHome,
                navigateToStatistic = navigateToStatistic,
                isCalendarPage = true,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 120.dp)
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


/*달력 생성 함수*/
/* 달력 생성 함수 */
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
            val isSelected = selectedDate.dayOfMonth == day && selectedDate.month == currentMonth.month
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
    Dialog(onDismissRequest = { onDismiss() }) {
        // Dialog의 내용을 Card로 감싸서 디자인을 추가
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.6f),
            shape = RoundedCornerShape(8.dp),
            color = LightBlue
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(600.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
                    shape = RectangleShape
                ) {
                    Text("나의 음주 기록하기", color = LightRed, fontSize = 24.sp)
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(500.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
                    shape = RectangleShape
                ) {
                    Text("나의 흡연 기록하기", color = LightRed, fontSize = 24.sp)
                }
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(600.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor),
                    shape = RectangleShape
                ) {
                    Text("나의 카페인 기록하기", color = LightRed, fontSize = 24.sp)
                }
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
            Text(
                text = "중독 관리 어플",
            )
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
    isCalendarPage: Boolean = false,
    isHomePage: Boolean = false,
    isStatisticPage: Boolean = false,
) {
    var selectedItem by remember { mutableStateOf(1) }  // 초기 선택 인덱스를 0으로 설정

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
                title = "캘린더",
                icon = { Icon(painter = painterResource(id = R.drawable.calendar_month_24px), contentDescription = "Calender", tint = if (selectedItem == 0) Black else LightGray) },
                isSelected = selectedItem == 0,
                onSelect = {
                    selectedItem = 0
                    navigateToCalendar()
                },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                title = "홈",
                icon = { Icon(painter = painterResource(id = R.drawable.home_24px), contentDescription = "Home", tint = if (selectedItem == 1) Black else LightGray) },
                isSelected = selectedItem == 1,
                onSelect = {
                    selectedItem = 1
                    navigateToHome()
                },
                modifier = Modifier.weight(1f)
            )
            TabItem(
                title = "통계",
                icon = { Icon(painter = painterResource(id = R.drawable.bar_chart_24px), contentDescription = "Bar Chart", tint = if (selectedItem == 2) Black else LightGray) },
                isSelected = selectedItem == 2,
                onSelect = {
                    selectedItem = 2
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
    title: String,
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
        Text(text = title, color = if (isSelected) Black else LightGrey, fontSize = 12.sp)
    }
}

