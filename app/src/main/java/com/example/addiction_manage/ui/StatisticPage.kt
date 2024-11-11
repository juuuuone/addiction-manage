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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.graphics.Color.Companion.Red
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import androidx.compose.ui.graphics.Color
import java.util.*




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticPage() {
    var selectedOption by remember { mutableStateOf("일주일") } // Default selected option

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = { TopAppBarComponent() },
        bottomBar = { BottomAppBarComponent() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(innerPadding)
                .background(color = LightGrey, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 25.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Timeframe Selector
            TimeframeSelector(
                options = listOf("하루", "일주일", "한달"),
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Statistics based on selected option
            AlcoholStatistic(
                progress = 0.3f,
                selectedOption = selectedOption
            )
            SmokingStatistic(
                progress = 0.7f,
                selectedOption = selectedOption
            )
            CaffeineStatistic(
                progress = 0.5f,
                selectedOption = selectedOption
            )
        }
    }
}

@Composable
fun TimeframeSelector(
    options: List<String>, // Options to select from
    selectedOption: String, // Currently selected option
    onOptionSelected: (String) -> Unit // Callback when an option is selected
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        options.forEach { option ->
            Surface(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .selectable(
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) }
                    )
                    .background(
                        color = if (option == selectedOption) LightRed else LightGrey,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = if (option == selectedOption) LightRed else LightGrey
            ) {
                Text(
                    text = option,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun GaugeGraph(
    modifier: Modifier = Modifier,
    progress: Float, // Current progress (0.0f to 1.0f)
    goalText: String,
    achievedText: String,
    backgroundColor: Color = Color.LightGray,
    progressColor: Color = Color.Red
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(backgroundColor, shape = RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .background(progressColor, shape = RoundedCornerShape(10.dp))
            )
        }

        // Goal Text
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = achievedText, fontSize = 14.sp, color = Color.Black)
            Text(text = goalText, fontSize = 14.sp, color = Color.Black)
        }
    }
}

@Composable
fun AlcoholStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    selectedOption: String // 옵션마다 텍스트
) {
    val alcoholTitle = when (selectedOption) {
        "하루" -> "오늘의 음주 통계"
        "일주일" -> "이번주의 음주 통계"
        "한달" -> "이번달의 음주 통계"
        else -> "음주 통계"
    }

    val achievedText = when (selectedOption) {
        "하루" -> "오늘: 1잔"
        "일주일" -> "이번주: 2잔"
        "한달" -> "이번달: 12잔"
        else -> "달성치 없음"
    }

    val goalText = when (selectedOption){
        "하루" -> "오늘: 1잔"
        "일주일" -> "이번주: 5잔"
        "한달" -> "이번달: 20잔"
        else -> "목표 없음"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = alcoholTitle,
            style = typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GaugeGraph(
            progress = progress,
            goalText = goalText,
            achievedText = achievedText,
            backgroundColor = Color.LightGray,
            progressColor = Color.Red
        )
    }
}

@Composable
fun SmokingStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    selectedOption: String // 옵션마다 텍스트
) {
    val smokingTitle = when (selectedOption) {
        "하루" -> "오늘의 흡연 통계"
        "일주일" -> "이번주의 흡연 통계"
        "한달" -> "이번달의 흡연 통계"
        else -> "흡연 통계"
    }
    val achievedText = when (selectedOption) {
        "하루" -> "오늘: 4개피"
        "일주일" -> "이번주: 8개피"
        "한달" -> "이번달: 36개피"
        else -> "달성치 없음"
    }
    val goalText = when (selectedOption){
        "하루" -> "오늘: 5개피"
        "일주일" -> "이번주: 30개피"
        "한달" -> "이번달: 100개피"
        else -> "목표 없음"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = smokingTitle,
            style = typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GaugeGraph(
            progress = progress,
            goalText = goalText,
            achievedText = achievedText,
            backgroundColor = Color.LightGray,
            progressColor = Color.Red
        )
    }
}

@Composable
fun CaffeineStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    selectedOption: String // 옵션마다 텍스트
) {
    val caffeineTitle = when (selectedOption) {
        "하루" -> "오늘의 카페인 통계"
        "일주일" -> "이번주의 카페인 통계"
        "한달" -> "이번달의 카페인 통계"
        else -> "카페인 통계"
    }
    val achievedText = when (selectedOption) {
        "하루" -> "오늘: 1잔"
        "일주일" -> "이번주: 2잔"
        "한달" -> "이번달: 12잔"
        else -> "달성치 없음"
    }
    val goalText = when (selectedOption){
        "하루" -> "오늘: 1잔"
        "일주일" -> "이번주: 5잔"
        "한달" -> "이번달: 20잔"
        else -> "목표 없음"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = caffeineTitle,
            style = typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GaugeGraph(
            progress = progress,
            goalText = goalText,
            achievedText = achievedText,
            backgroundColor = Color.LightGray,
            progressColor = Color.Red
        )
    }
}