package com.example.addiction_manage.feature.statistic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.LightGrey
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.LightBlue
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.WhiteBlue
import com.example.addiction_manage.ui.theme.White
import java.util.*

import com.example.addiction_manage.feature.calendar.BottomAppBarComponent
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.feature.graph.ColumnGraph
import com.example.addiction_manage.ui.theme.White
import kotlin.math.absoluteValue

val minSansFontFamily = FontFamily(Font(R.font.minsans))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticPage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
    navigateToMyPage: () -> Unit,
    navController: NavController,
    selectedItem: Int,
) {
    var selectedOption by remember { mutableStateOf("음주") }
    val yesterdayAlcohol by remember { mutableFloatStateOf(3f) }
    val currentAlcohol by remember { mutableFloatStateOf(2f) }
    val yesterdaySmoking by remember { mutableFloatStateOf(4f) }
    val currentSmoking by remember { mutableFloatStateOf(5f) }
    val yesterdayCaffeine by remember { mutableFloatStateOf(1f) }
    val currentCaffeine by remember { mutableFloatStateOf(1f) }
    val goalAlcohol by remember { mutableFloatStateOf(3f) }
    val goalSmoking by remember { mutableFloatStateOf(3f) }
    val goalCaffeine by remember { mutableFloatStateOf(3f) }

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
                isStatisticPage = true,
                selectedItem = selectedItem,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding)
                .background(color = White, shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 25.dp),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(12.dp))
            // 카테고리 선택
            TimeframeSelector(
                options = listOf("음주", "흡연", "카페인"),
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )

            Spacer(modifier = Modifier.padding(12.dp))

            // 통계 페이지
            if (selectedOption == "음주") {
                AlcoholCount(
                    yesterdayAlcohol = yesterdayAlcohol,
                    currentAlcohol = currentAlcohol,
                )
                Spacer(modifier = Modifier.padding(10.dp))
                AlcoholStatistic(
                    progress = 0.3f,
                    currentAlcohol = currentAlcohol,
                    goalAlcohol = goalAlcohol,
                )
                Spacer(modifier = Modifier.padding(10.dp))
                ColumnGraph(unit = "잔", threshold = goalAlcohol)
            } else if (selectedOption == "흡연") {
                SmokingCount(
                    yesterdaySmoking = yesterdaySmoking,
                    currentSmoking = currentSmoking,
                )
                Spacer(modifier = Modifier.padding(10.dp))
                SmokingStatistic(
                    progress = 0.7f,
                    currentSmoking = currentSmoking,
                    goalSmoking = goalSmoking
                )
                Spacer(modifier = Modifier.padding(10.dp))
                ColumnGraph(unit = "개피", threshold = goalSmoking)
            } else if (selectedOption == "카페인") {
                CaffeineCount(
                    yesterdayCaffeine = yesterdayCaffeine,
                    currentCaffeine = currentCaffeine,
                )
                Spacer(modifier = Modifier.padding(10.dp))
                CaffeineStatistic(
                    progress = 0.5f,
                    currentCaffeine = currentCaffeine,
                    goalCaffeine = goalCaffeine
                )
                Spacer(modifier = Modifier.padding(10.dp))
                ColumnGraph(unit = "잔", threshold = goalCaffeine)
            }
        }
    }
}

@Composable
fun TimeframeSelector(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
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
                        color = if (option == selectedOption) MediumBlue else WhiteBlue,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = if (option == selectedOption) MediumBlue else WhiteBlue
            ) {
                Text(
                    text = option,
                    fontSize = 14.sp,
                    fontFamily = minSansFontFamily,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun GaugeGraph(
    modifier: Modifier = Modifier,
    progress: Float,
    goalText: String,
    achievedText: String,
    backgroundColor: Color = WhiteBlue,
    progressColor: Color = MediumBlue
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 진행 바
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

        // 목표 & 현재 기록량
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "현재 : $achievedText", fontSize = 14.sp,fontFamily = minSansFontFamily, color = Color.Black)
//            Text(text = "목표 : $goalText", fontSize = 14.sp, color = Color.Black)
        }
    }
}


@Composable
fun AlcoholCount(
    yesterdayAlcohol: Float,
    currentAlcohol: Float,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.dp),
        color = WhiteBlue,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "어제", fontSize = 14.sp,fontFamily = minSansFontFamily,)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = yesterdayAlcohol.toInt().toString() + "잔", fontSize = 22.sp,fontFamily = minSansFontFamily,)
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "오늘", fontSize = 14.sp,fontFamily = minSansFontFamily,)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = currentAlcohol.toInt().toString() + "잔", fontSize = 22.sp,fontFamily = minSansFontFamily,)
            }
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = (currentAlcohol.toInt() - yesterdayAlcohol.toInt()).absoluteValue.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (currentAlcohol.toInt() > yesterdayAlcohol.toInt()) Red else Blue
                )
                Image(
                    painter = painterResource(
                        id = if (currentAlcohol.toInt() > yesterdayAlcohol.toInt()) R.drawable.uparrow
                        else R.drawable.downarrow
                    ),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AlcoholStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    currentAlcohol: Float,
    goalAlcohol: Float,
) {
    val alcoholTitle = "일주일 음주 통계"
    val current = currentAlcohol.toInt().toString() + "잔"
    val goal = goalAlcohol.toInt().toString() + "잔"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = alcoholTitle,
            fontFamily = minSansFontFamily,
            style = typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GaugeGraph(
            progress = progress,
            goalText = goal,
            achievedText = current,
            backgroundColor = WhiteBlue,
            progressColor = MediumBlue
        )
    }
}

@Composable
fun SmokingCount(
    yesterdaySmoking: Float,
    currentSmoking: Float,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.dp),
        color = WhiteBlue,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "어제", fontSize = 14.sp,fontFamily = minSansFontFamily)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = yesterdaySmoking.toInt().toString() + "개피", fontSize = 22.sp,fontFamily = minSansFontFamily,)
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "오늘", fontSize = 14.sp,fontFamily = minSansFontFamily,)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = currentSmoking.toInt().toString() + "개피", fontSize = 22.sp,fontFamily = minSansFontFamily,)
            }
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = (currentSmoking.toInt() - yesterdaySmoking.toInt()).absoluteValue.toString(),
                    fontSize = 20.sp,
                    fontFamily = minSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = if (currentSmoking.toInt() > yesterdaySmoking.toInt()) Red else Blue
                )
                Image(
                    painter = painterResource(
                        id = if (currentSmoking.toInt() > yesterdaySmoking.toInt()) R.drawable.uparrow
                        else R.drawable.downarrow
                    ),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SmokingStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    currentSmoking: Float,
    goalSmoking: Float,
) {
    val smokingTitle = "일주일 흡연 통계"
    val current = currentSmoking.toInt().toString() + "개피"
    val goal = goalSmoking.toInt().toString() + "개피"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = smokingTitle,
            fontFamily = minSansFontFamily,
            style = typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GaugeGraph(
            progress = progress,
            goalText = goal,
            achievedText = current,
            backgroundColor = WhiteBlue,
            progressColor = MediumBlue
        )
    }
}

@Composable
fun CaffeineCount(
    yesterdayCaffeine: Float,
    currentCaffeine: Float,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 7.dp),
        color = WhiteBlue,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "어제", fontSize = 14.sp,fontFamily = minSansFontFamily,)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = yesterdayCaffeine.toInt().toString() + "잔", fontSize = 22.sp,fontFamily = minSansFontFamily,)
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "오늘", fontSize = 14.sp,fontFamily = minSansFontFamily,)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = currentCaffeine.toInt().toString() + "잔", fontSize = 22.sp,fontFamily = minSansFontFamily,)
            }
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = (currentCaffeine.toInt() - yesterdayCaffeine.toInt()).absoluteValue.toString(),
                    fontSize = 20.sp,
                    fontFamily = minSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = if (currentCaffeine.toInt() > yesterdayCaffeine.toInt()) Red else Blue
                )
                Image(
                    painter = painterResource(
                        id = if (currentCaffeine.toInt() > yesterdayCaffeine.toInt()) R.drawable.uparrow
                        else R.drawable.downarrow
                    ),
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CaffeineStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    currentCaffeine: Float,
    goalCaffeine: Float,
) {
    val caffeineTitle = "일주일 카페인 통계"
    val current = currentCaffeine.toInt().toString() + "잔"
    val goal = goalCaffeine.toInt().toString() + "잔"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = caffeineTitle,
            fontFamily = minSansFontFamily,
            style = typography.titleLarge,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        GaugeGraph(
            progress = progress,
            goalText = goal,
            achievedText = current,
            backgroundColor = WhiteBlue,
            progressColor = MediumBlue
        )
    }
}