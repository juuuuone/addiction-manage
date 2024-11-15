package com.example.addiction_manage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.addiction_manage.ui.AlcoholPage
import com.example.addiction_manage.ui.CaffeinePage
import com.example.addiction_manage.ui.MainPage
import com.example.addiction_manage.ui.SmokingPage
import com.example.addiction_manage.ui.theme.Addiction_manageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Addiction_manageTheme {
                    MainPage()
                }
            }
        }
    }
