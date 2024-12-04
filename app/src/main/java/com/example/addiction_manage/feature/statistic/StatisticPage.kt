package com.example.addiction_manage.feature.statistic

import android.util.Log
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.addiction_manage.feature.alcohol.AlcoholGoalViewModel
import com.example.addiction_manage.feature.alcohol.AlcoholViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineGoalViewModel
import com.example.addiction_manage.feature.caffeine.CaffeineViewModel
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.WhiteBlue
import com.example.addiction_manage.ui.theme.White

import com.example.addiction_manage.feature.calendar.BottomAppBarComponent
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.feature.graph.ColumnGraph
import com.example.addiction_manage.feature.smoking.SmokingGoalViewModel
import com.example.addiction_manage.feature.smoking.SmokingViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.absoluteValue

val minSansFontFamily = FontFamily(Font(R.font.minsans))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticPage(
    navigateToCalendar: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToStatistic: () -> Unit,
    navigateToFriends:()->Unit,
    navigateToMyPage: () -> Unit,
    navController: NavController,
    selectedItem: Int,
) {
    val alcoholString = stringResource(id = R.string.alcohol)
    val smokingString = stringResource(id = R.string.smoking)
    val caffeineString = stringResource(id = R.string.caffeine)

    var isLoading by remember { mutableStateOf(true) }
    var selectedOption by remember { mutableStateOf("") }

    var yesterdayAlcohol by remember { mutableStateOf(false) }
    var currentAlcohol by remember { mutableStateOf(true) }
    var weekAlcohol by remember { mutableIntStateOf(0) }
    var goalAlcohol by remember { mutableIntStateOf(0) }

    var yesterdaySmoking by remember { mutableIntStateOf(0) }
    var currentSmoking by remember { mutableIntStateOf(0) }
    var weekSmoking by remember { mutableIntStateOf(0) }
    var goalSmoking by remember { mutableIntStateOf(0) }

    var yesterdayCaffeine by remember { mutableIntStateOf(0) }
    var currentCaffeine by remember { mutableIntStateOf(0) }
    var weekCaffeine by remember { mutableIntStateOf(0) }
    var goalCaffeine by remember { mutableIntStateOf(0) }

    var alcoholGraphData by remember { mutableStateOf(emptyList<Pair<String, Int>>()) }
    var smokingGraphData by remember { mutableStateOf(emptyList<Pair<String, Int>>()) }
    var caffeineGraphData by remember { mutableStateOf(emptyList<Pair<String, Int>>()) }

    var options by remember { mutableStateOf<List<String>>(emptyList()) }

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid ?: return

    val alcoholViewModel = hiltViewModel<AlcoholViewModel>()
    val alcoholGoalViewModel = hiltViewModel<AlcoholGoalViewModel>()
    val smokingViewModel = hiltViewModel<SmokingViewModel>()
    val smokingGoalViewModel = hiltViewModel<SmokingGoalViewModel>()
    val caffeineViewModel = hiltViewModel<CaffeineViewModel>()
    val caffeineGoalViewModel = hiltViewModel<CaffeineGoalViewModel>()

    LaunchedEffect(key1 = true) {
        alcoholViewModel.listenForAlcoholRecords(userId)
        smokingViewModel.listenForSmokingRecords(userId)
        caffeineViewModel.listenForCaffeineRecords(userId)
    }
    val alcoholRecords = alcoholViewModel.alcoholRecords.collectAsState()
    val alcoholGoal = alcoholGoalViewModel.goal.collectAsState()
    val hasAlcoholGoal = alcoholGoalViewModel.isAlcoholChecked.collectAsState()
    val smokingRecords = smokingViewModel.smokingRecords.collectAsState()
    val smokingGoal = smokingGoalViewModel.goal.collectAsState()
    val hasSmokingGoal = smokingGoalViewModel.isSmokingChecked.collectAsState()
    val caffeineRecords = caffeineViewModel.caffeineRecords.collectAsState()
    val caffeineGoal = caffeineGoalViewModel.goal.collectAsState()
    val hasCaffeineGoal = caffeineGoalViewModel.isCaffeineChecked.collectAsState()

    LaunchedEffect(
        alcoholRecords.value,
        smokingRecords.value,
        caffeineRecords.value,
        alcoholGoal.value,
        smokingGoal.value,
        caffeineGoal.value,
        hasAlcoholGoal.value,
        hasSmokingGoal.value,
        hasCaffeineGoal.value,
    ) {
        if (alcoholRecords.value.isNotEmpty() && smokingRecords.value.isNotEmpty() && caffeineRecords.value.isNotEmpty()) {
            isLoading = false
        }
        if (alcoholRecords.value.isNotEmpty()) {
            yesterdayAlcohol =
                alcoholViewModel.getYesterdayAlcoholRecord(alcoholRecords.value)?.doDrink ?: false
            currentAlcohol =
                alcoholViewModel.getTodayAlcoholRecord(alcoholRecords.value)?.doDrink ?: false
            weekAlcohol = alcoholViewModel.getWeekTotalAlcoholRecord(alcoholRecords.value)
            alcoholGraphData = alcoholViewModel.getWeekAlcoholRecord(alcoholRecords.value)
        }
        if (smokingRecords.value.isNotEmpty()) {
            yesterdaySmoking =
                smokingViewModel.getYesterdaySmokingRecord(smokingRecords.value)?.cigarettes ?: 0
            currentSmoking =
                smokingViewModel.getTodaySmokingRecord(smokingRecords.value)?.cigarettes ?: 0
            weekSmoking = smokingViewModel.getWeekTotalSmokingRecord(smokingRecords.value)
            smokingGraphData = smokingViewModel.getWeekSmokingRecord(smokingRecords.value)
        }
        if (caffeineRecords.value.isNotEmpty()) {
            yesterdayCaffeine =
                caffeineViewModel.getYesterdayCaffeineRecord(caffeineRecords.value)?.drinks ?: 0
            currentCaffeine =
                caffeineViewModel.getTodayCaffeineRecord(caffeineRecords.value)?.drinks ?: 0
            weekCaffeine = caffeineViewModel.getWeekTotalCaffeineRecord(caffeineRecords.value)
            caffeineGraphData = caffeineViewModel.getWeekCaffeineRecord(caffeineRecords.value)
        }
        if (alcoholGoal.value.isNotEmpty()) {
            goalAlcohol = alcoholGoalViewModel.getCurrentUserGoal()?.goal?.toInt() ?: 0
        }
        if (smokingGoal.value.isNotEmpty()) {
            goalSmoking = smokingGoalViewModel.getCurrentUserGoal()?.goal?.toInt() ?: 0
        }
        if (caffeineGoal.value.isNotEmpty()) {
            goalCaffeine = caffeineGoalViewModel.getCurrentUserGoal()?.goal?.toInt() ?: 0
        }

        var updatedOptions = mutableListOf<String>()

        if (!hasAlcoholGoal.value) {
            updatedOptions.add(alcoholString)
        }
        if (!hasSmokingGoal.value) {
            updatedOptions.add(smokingString)
        }
        if (!hasCaffeineGoal.value) {
            updatedOptions.add(caffeineString)
        }
        options = updatedOptions
        selectedOption = options.firstOrNull() ?: ""
    }

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
                navigateToFriends=navigateToFriends,
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
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = MediumBlue
                )
                Text(
                    text = stringResource(id = R.string.loading),
                    fontFamily = minSansFontFamily,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            } else {
                Spacer(modifier = Modifier.padding(12.dp))
                TimeframeSelector(
                    options = options,
                    selectedOption = selectedOption,
                    onOptionSelected = { selectedOption = it }
                )
                Spacer(modifier = Modifier.padding(12.dp))
                if (selectedOption == alcoholString) {
                    AlcoholCount(
                        yesterdayAlcohol = yesterdayAlcohol,
                        currentAlcohol = currentAlcohol,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    AlcoholStatistic(
                        progress = weekAlcohol.toFloat() / goalAlcohol,
                        weekAlcohol = weekAlcohol,
                        goalAlcohol = goalAlcohol,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    ColumnGraph(
                        unit = stringResource(id = R.string.soju),
                        data = alcoholGraphData,
                    )
                } else if (selectedOption == smokingString) {
                    SmokingCount(
                        yesterdaySmoking = yesterdaySmoking,
                        currentSmoking = currentSmoking,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    SmokingStatistic(
                        progress = weekSmoking.toFloat() / (goalSmoking * 7),
                        weekSmoking = weekSmoking,
                        goalSmoking = goalSmoking * 7,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    ColumnGraph(
                        unit = stringResource(id = R.string.gp),
                        data = smokingGraphData,
                    )
                } else if (selectedOption == caffeineString) {
                    CaffeineCount(
                        yesterdayCaffeine = yesterdayCaffeine,
                        currentCaffeine = currentCaffeine,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    CaffeineStatistic(
                        progress = weekCaffeine.toFloat() / (goalCaffeine * 7),
                        weekCaffeine = weekCaffeine,
                        goalCaffeine = goalCaffeine * 7,
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    ColumnGraph(
                        unit = stringResource(id = R.string.cup),
                        data = caffeineGraphData,
                    )
                }
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
    current: String,
    goal: String,
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
                text = stringResource(id = R.string.now) + " : $current",
                fontSize = 14.sp,
                fontFamily = minSansFontFamily,
                color = Color.Black
            )
            Text(
                text = stringResource(id = R.string.goal) + " : $goal",
                fontSize = 14.sp,
                fontFamily = minSansFontFamily,
                color = Color.Black
            )
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
                Text(
                    text = stringResource(id = R.string.yesterday),
                    fontSize = 14.sp,
                    fontFamily = minSansFontFamily
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = if (yesterdayAlcohol) stringResource(id = R.string.alcohol) else stringResource(
                        id = R.string.no_alcohol
                    ),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.today),
                    fontSize = 14.sp,
                    fontFamily = minSansFontFamily
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = if (currentAlcohol) stringResource(id = R.string.alcohol) else stringResource(
                        id = R.string.no_alcohol
                    ),
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
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AlcoholStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    weekAlcohol: Int,
    goalAlcohol: Int,
) {
    val alcoholTitle = stringResource(id = R.string.weekly_alcohol_statistic)
    val current = weekAlcohol.toString() + stringResource(id = R.string.day)
    val goal = goalAlcohol.toString() + stringResource(id = R.string.day)

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
            current = current,
            goal = goal,
            backgroundColor = WhiteBlue,
            progressColor = MediumBlue
        )
    }
}

@Composable
fun SmokingCount(
    yesterdaySmoking: Int,
    currentSmoking: Int,
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
                Text(
                    text = stringResource(id = R.string.yesterday),
                    fontSize = 14.sp,
                    fontFamily = minSansFontFamily
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = yesterdaySmoking.toString() + stringResource(id = R.string.gp),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.today),
                    fontSize = 14.sp,
                    fontFamily = minSansFontFamily
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = currentSmoking.toString() + stringResource(id = R.string.gp),
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
                    text = (currentSmoking - yesterdaySmoking).absoluteValue.toString(),
                    fontSize = 20.sp,
                    fontFamily = minSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = if (currentSmoking > yesterdaySmoking) Red else Blue
                )
                Image(
                    painter = painterResource(
                        id = if (currentSmoking > yesterdaySmoking) R.drawable.uparrow
                        else R.drawable.downarrow
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SmokingStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    weekSmoking: Int,
    goalSmoking: Int,
) {
    val smokingTitle = stringResource(id = R.string.weekly_smoking_statistic)
    val current = weekSmoking.toString() + stringResource(id = R.string.gp)
    val goal = goalSmoking.toString() + stringResource(id = R.string.gp)

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
            current = current,
            goal = goal,
            backgroundColor = WhiteBlue,
            progressColor = MediumBlue
        )
    }
}

@Composable
fun CaffeineCount(
    yesterdayCaffeine: Int,
    currentCaffeine: Int,
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
                Text(
                    text = stringResource(id = R.string.yesterday),
                    fontSize = 14.sp,
                    fontFamily = minSansFontFamily
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = yesterdayCaffeine.toString() + stringResource(id = R.string.cup),
                    fontSize = 22.sp,
                    fontFamily = minSansFontFamily,
                )
            }
            Column(
                modifier = Modifier.padding(vertical = 5.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.today),
                    fontSize = 14.sp,
                    fontFamily = minSansFontFamily
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = currentCaffeine.toString() + stringResource(id = R.string.cup),
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
                    text = (currentCaffeine - yesterdayCaffeine).absoluteValue.toString(),
                    fontSize = 20.sp,
                    fontFamily = minSansFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = if (currentCaffeine > yesterdayCaffeine) Red else Blue
                )
                Image(
                    painter = painterResource(
                        id = if (currentCaffeine > yesterdayCaffeine) R.drawable.uparrow
                        else R.drawable.downarrow
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CaffeineStatistic(
    progress: Float, // 목표 대비 진행률 (0.0f ~ 1.0f)
    weekCaffeine: Int,
    goalCaffeine: Int,
) {
    val caffeineTitle = stringResource(id = R.string.weekly_caffeine_statistic)
    val current = weekCaffeine.toString() + stringResource(id = R.string.cup)
    val goal = goalCaffeine.toString() + stringResource(id = R.string.cup)

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
            current = current,
            goal = goal,
            backgroundColor = WhiteBlue,
            progressColor = MediumBlue
        )
    }
}