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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.feature.alcohol.AlcoholViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineViewModel
import com.example.addiction_manage.ui.theme.LightBlue
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.WhiteBlue
import com.example.addiction_manage.ui.theme.White
import java.util.*

import com.example.addiction_manage.feature.calendar.BottomAppBarComponent
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.feature.graph.ColumnGraph
import com.example.addiction_manage.feature.smoking.SmokingViewModel
import com.example.addiction_manage.ui.theme.White
import com.google.firebase.auth.FirebaseAuth
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
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: return
    val alcoholViewModel = hiltViewModel<AlcoholViewModel>()
    val smokingViewModel = hiltViewModel<SmokingViewModel>()
    val caffeineViewModel = hiltViewModel<CaffeineViewModel>()

    LaunchedEffect(key1 = true) {
        alcoholViewModel.listenForAlcoholRecords(userId)
        smokingViewModel.listenForSmokingRecords(userId)
        caffeineViewModel.listenForCaffeineRecords(userId)
    }
    val alcoholRecords = alcoholViewModel.alcoholRecords.collectAsState()
    val smokingRecords = smokingViewModel.smokingRecords.collectAsState()
    val caffeineRecords = caffeineViewModel.caffeineRecords.collectAsState()

    /*
    어제, 오늘 기록은 각 ViewModel에서 어제랑 오늘 기록 하나씩 가져오면 될 것 같고
    일주일 전체 기록 가져오면 그래프에 주고, 다 더해서 게이지에 주면 되겠네
    일주일 단위에 대한 날짜들도 전부 가져와서 그래프 x축에 적으면 될 듯
    뭔가 user의 createdAt을 시작점으로 일주일 단위로 끊으면 되지 않을까?
     */

    var selectedOption by remember { mutableStateOf("음주") }
    val yesterdayAlcohol by remember { mutableStateOf(false) }
    val currentAlcohol by remember { mutableStateOf(true) }
    val weekAlcohol by remember { mutableFloatStateOf(2f) }
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
                options = listOf(stringResource(id=R.string.alcohol), stringResource(id=R.string.smoking), stringResource(id=R.string.caffeine)),
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )

            Spacer(modifier = Modifier.padding(12.dp))

            // 통계 페이지
            if (selectedOption == stringResource(id=R.string.alcohol)) {
                AlcoholCount(
                    yesterdayAlcohol = yesterdayAlcohol,
                    currentAlcohol = currentAlcohol,
                )
                Spacer(modifier = Modifier.padding(10.dp))
                AlcoholStatistic(
                    progress = 0.3f,
                    weekAlcohol = weekAlcohol,
                    goalAlcohol = goalAlcohol,
                )
                Spacer(modifier = Modifier.padding(10.dp))
                ColumnGraph(unit = stringResource(id=R.string.cup), threshold = goalAlcohol)
            } else if (selectedOption == stringResource(id=R.string.smoking)) {
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
                ColumnGraph(unit = stringResource(id=R.string.gp), threshold = goalSmoking)
            } else if (selectedOption == stringResource(id=R.string.caffeine)) {
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
                ColumnGraph(unit = stringResource(id=R.string.cup), threshold = goalCaffeine)
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
            Text(
                text = stringResource(id=R.string.now) + " : $achievedText",
                fontSize = 14.sp,
                fontFamily = minSansFontFamily,
                color = Color.Black
            )
//            Text(text = "목표 : $goalText", fontSize = 14.sp, color = Color.Black)
        }
    }
}


@Composable
fun AlcoholCount(
    yesterdayAlcohol: Boolean,
    currentAlcohol: Boolean,
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
                Text(text = stringResource(id=R.string.yesterday), fontSize = 14.sp, fontFamily = minSansFontFamily)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = if (yesterdayAlcohol) stringResource(id=R.string.alcohol) else stringResource(id=R.string.no_alcohol),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id=R.string.today), fontSize = 14.sp, fontFamily = minSansFontFamily)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = if (currentAlcohol) stringResource(id=R.string.alcohol) else stringResource(id=R.string.no_alcohol),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
            }
            Row(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(
                        id = if (currentAlcohol) R.drawable.downarrow else R.drawable.uparrow
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
    weekAlcohol: Float,
    goalAlcohol: Float,
) {
    val alcoholTitle = stringResource(id=R.string.weekly_alcohol_statistic)
    val current = weekAlcohol.toInt().toString() + stringResource(id=R.string.cup)
    val goal = goalAlcohol.toInt().toString() + stringResource(id=R.string.cup)

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
                Text(text = stringResource(id=R.string.yesterday), fontSize = 14.sp, fontFamily = minSansFontFamily)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = yesterdaySmoking.toInt().toString() + stringResource(id=R.string.gp),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id=R.string.today), fontSize = 14.sp, fontFamily = minSansFontFamily)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = currentSmoking.toInt().toString() + stringResource(id=R.string.gp),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
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
    val smokingTitle = stringResource(id=R.string.weekly_smoking_statistic)
    val current = currentSmoking.toInt().toString() + stringResource(id=R.string.gp)
    val goal = goalSmoking.toInt().toString() + stringResource(id=R.string.gp)

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
                Text(text = stringResource(id=R.string.yesterday), fontSize = 14.sp, fontFamily = minSansFontFamily)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = yesterdayCaffeine.toInt().toString() + stringResource(id=R.string.cup),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id=R.string.today), fontSize = 14.sp, fontFamily = minSansFontFamily)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = currentCaffeine.toInt().toString() + stringResource(id=R.string.cup),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
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
    val caffeineTitle = stringResource(id=R.string.weekly_caffeine_statistic)
    val current = currentCaffeine.toInt().toString() + stringResource(id=R.string.cup)
    val goal = goalCaffeine.toInt().toString() + stringResource(id=R.string.cup)

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