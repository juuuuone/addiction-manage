package com.example.addiction_manage.feature.alcohol

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.addiction_manage.R
import com.example.addiction_manage.ui.theme.BackgroundColor
import com.example.addiction_manage.ui.theme.LightRed
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.Black
import com.example.addiction_manage.ui.theme.MediumGrey
import com.example.addiction_manage.ui.theme.DarkRed

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.addiction_manage.feature.calendar.TopAppBarComponent
import com.example.addiction_manage.feature.model.Alcohol
import com.example.addiction_manage.ui.theme.MediumBlue
import com.example.addiction_manage.ui.theme.White
import com.example.addiction_manage.ui.theme.LightGrey
import com.example.addiction_manage.ui.theme.WhiteBlue


@Composable
fun AlcoholPage(
    navigateToMyPage: () -> Unit,
    navController: NavController,
    navigateToHome: () -> Unit
) {
    var answer by rememberSaveable { mutableStateOf(false) }
    val viewModel = hiltViewModel<AlcoholViewModel>()
    val alcoholRecords = viewModel.alcoholRecords.collectAsState()
    LaunchedEffect(alcoholRecords.value) {
        answer = viewModel.getTodayAlcoholRecord(alcoholRecords.value)?.doDrink ?: false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBarComponent(
                navigateToMyPage = navigateToMyPage,
                navigateUp = {
                    navController.navigateUp()
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AlcoholRecording(
                answer = answer,
                setAnswer = { answer = it },
                navigateToHome = navigateToHome,
                addAlcohol = { viewModel.addAlcoholRecord(it) }
            )
        }
    }
}

@Composable
fun AlcoholRecording(
    answer: Boolean,
    setAnswer: (Boolean) -> Unit,
    navigateToHome: () -> Unit,
    addAlcohol: (Boolean) -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(answer) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp),
        shape = RoundedCornerShape(8.dp),
        color = White
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(stringResource(id = R.string.today_alcohol), color = MediumBlue, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(60.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        selected = true
                        setAnswer(true)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .width(150.dp)
                        .height(50.dp),
                    colors = if (selected) ButtonDefaults.buttonColors(containerColor = MediumBlue)
                    else ButtonDefaults.buttonColors(
                        containerColor = WhiteBlue
                    ),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Text(
                        stringResource(id = R.string.yes),
                        color = Black,
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.minsans))
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(
                    onClick = {
                        selected = false
                        setAnswer(false)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .width(150.dp)
                        .height(50.dp),
                    colors = if (!selected) ButtonDefaults.buttonColors(containerColor = MediumBlue)
                    else ButtonDefaults.buttonColors(
                        containerColor = WhiteBlue
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.no),
                        fontSize = 20.sp,
                        color = Black,
                        fontFamily = FontFamily(Font(R.font.minsans))
                    )
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            OutlinedButton(
                onClick = {
                    addAlcohol(answer)
                    navigateToHome()
                },
                modifier = Modifier
                    .padding(4.dp)
                    .width(150.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = White),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(width = 2.dp, color = MediumBlue)
            ) {
                Text(
                    stringResource(id = R.string.record_button),
                    fontSize = 20.sp,
                    color = Black,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
            }
        }
    }
}
