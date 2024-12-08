package com.example.addiction_manage.feature.alcohol

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.addiction_manage.R
import com.example.addiction_manage.feature.smoking.CheckboxWithBorder
import com.example.addiction_manage.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlcoholGoalPage(
    navController: NavController
) {
    val viewModel: AlcoholGoalViewModel = hiltViewModel()
    val isNoAlcoholChecked by viewModel.isAlcoholChecked.collectAsState()
    val showDialog = remember { mutableStateOf(false) }
    val currentUserGoal = viewModel.getCurrentUserGoal()
    val boldFontFamily = FontFamily(Font(R.font.bold))
    val lightFontFamily = FontFamily(Font(R.font.light))

    val loadingMessage = stringResource(id = R.string.loading)
    val newGoal = remember { mutableStateOf(loadingMessage) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(currentUserGoal) {
        isLoading.value = true
        val goal = currentUserGoal?.goal
        newGoal.value = goal ?: ""
        isLoading.value = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.set_goal),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Black
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BackgroundColor),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.set_alcohol_goal),
                color = Black,
                fontSize = 24.sp,
                fontFamily = boldFontFamily,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.write_alcohol_goal),
                fontSize = 19.sp,
                color = Color.DarkGray,
                fontFamily = lightFontFamily,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(35.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = if (isLoading.value) loadingMessage else newGoal.value,
                        onValueChange = { newGoal.value = it },
                        enabled = true,
                        label = { Text(stringResource(id = R.string.set_alcohol_goal)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            viewModel.addGoal(newGoal.value)
                            showDialog.value = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.save_button),
                            fontFamily = lightFontFamily
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    CheckboxWithBorder(
                        label = stringResource(id = R.string.not_alcohol),
                        isChecked = isNoAlcoholChecked,
                        onCheckedChange = { viewModel.setNoAlcoholChecked(it) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    navController.navigate(route = "smoking-goal")
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.next_button),
                    fontFamily = lightFontFamily,
                    fontSize = 20.sp
                )
            }

            if (showDialog.value) {
                ShowSaveDialog(
                    onDismiss = { showDialog.value = false }
                )
            }
        }
    }
}


@Composable
fun ShowSaveDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(2.dp, color = MediumBlue)
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = stringResource(id = R.string.notify),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily(Font(R.font.minsans))
                )
                Text(
                    text = stringResource(id = R.string.is_saving),
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.minsans)),
                    fontSize = 20.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = MediumBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.check_button),
                            fontFamily = FontFamily(Font(R.font.minsans))
                        )
                    }
                }
            }
        }
    }
}




