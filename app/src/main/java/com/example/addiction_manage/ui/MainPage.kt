package com.example.addiction_manage.ui

import android.content.Context
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.Black
import java.util.Calendar
import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color.Companion.Red
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import androidx.compose.ui.graphics.Color
import java.util.*




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = BackgroundColor,
            topBar = {     TopAppBarComponent()       },
            bottomBar={    BottomAppBarComponent()        }
//---------------탑바, 바텀바----------------
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp)
                    .padding(top = 150.dp)
                    .background(color = LightGrey, shape = RoundedCornerShape(10.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val currentMonth = YearMonth.now()
                Text(text = currentMonth.toString(), fontSize = 32.sp)
                SimpleCalendar()
            }

        }
    }

@Composable
fun SimpleCalendar() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val currentMonth = YearMonth.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstOfMonth = currentMonth.atDay(1)
    val dayOfWeek = firstOfMonth.get(WeekFields.of(Locale.getDefault()).dayOfWeek())

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.padding(16.dp)
    ) {
        items((1..daysInMonth).toList()) { day ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clickable { selectedDate = currentMonth.atDay(day) }
                    .padding(4.dp)
            ) {
                Text(
                    text = day.toString(),
                    color = if (selectedDate.dayOfMonth == day && selectedDate.month == currentMonth.month) LightRed else Black
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComponent(){
    TopAppBar(
        title = { Text(text = "중독 관리 어플",
        ) },
        navigationIcon = {
            IconButton(onClick = {/*뒤로가기*/ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        actions = {
            // 여기에 사용자 정의 아이콘을 추가
            IconButton(onClick = { /* 아이콘 클릭 시 수행할 동작 */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.account_circle_24px),
                    contentDescription = "MypageButton",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = BackgroundColor, // TopAppBar의 배경색
            titleContentColor = White, // 제목의 색상
            actionIconContentColor = White, // 액션 아이콘 색상
            navigationIconContentColor = White
        )
    )
}


@Composable
fun BottomAppBarComponent() {
    var selectedItem by remember { mutableStateOf(0) }  // 초기 선택 인덱스를 0으로 설정

    BottomAppBar(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth(), // 하단 바를 전체 너비로 설정
        containerColor =  BackgroundColor, // 기본 하단 바의 배경색 설정
        contentColor = Black // 아이콘 및 텍스트 색상
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TabItem1(
                title = "캘린더",
                icon = {  },
                isSelected = selectedItem == 0,
                onSelect = { selectedItem = 0 }
            )
            TabItem2(
                title = "통계",
                icon = {  },
                isSelected = selectedItem == 1,
                onSelect = { selectedItem = 1 }
            )
        }
    }
}

@Composable
fun TabItem1(
    title: String,
    icon: @Composable () -> Unit,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxHeight()
            .fillMaxWidth(0.5f)
            .clickable(onClick = onSelect)
            .background(if (isSelected) LightRed else LightGrey),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        Text(text = title, color = Black, fontSize = 20.sp)
    }
}

@Composable
fun TabItem2(
    title: String,
    icon: @Composable () -> Unit,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxHeight()
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .background(if (isSelected) LightRed else LightGrey),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        Text(text = title, color = Black, fontSize = 20.sp)
    }
}
