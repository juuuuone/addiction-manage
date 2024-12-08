package com.example.addiction_manage.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

//val BackgroundColor = Color(0xFF3A3A3A)

val White=Color(0xFFFFFFFF)

// LightRed=Color(0xFFE77272)
val LightGrey=Color(0xFFD9D9D9)

val Black=Color(0xFF000000)

val MediumGrey=Color(0xFFC4C4C4)
val DarkRed=Color(0xFFAE2B2B)

val BackgroundColor = Color(0xFFFFFFFF)


val LightRed=Color(0xFF000000)
//val LightBlue=Color(0xFFA0CFFE)
//
//val MediumBlue=Color(0xFF429FFE)
//val WhiteBlue= Color(0xFFEAF2FF)


val LightBlue = Color(0xFF95BB96) // 예: 비슷한 명도와 채도를 가진 옅은 녹색
val MediumBlue = Color(0xFF4CAF50) // 예: Android Material Design의 'Green 500'
val WhiteBlue = Color(0xFFE8F5E9) // 예: 매우 연한 녹색, Android Material Design의 'Green 50'

val WhiteRed=Color(0xFFFFEBEE)
val MediumRed=Color(0xFFF44336)

fun getColorBasedOnAlcoholWin(myAlcoholWin: Boolean?): Color {
    return when (myAlcoholWin) {
        true -> MediumBlue
        false -> MediumRed
        null -> Black
    }
}

fun getColorBasedOnMyScore(myAlcoholWin: Boolean): Color {
    return when (myAlcoholWin) {
        true -> WhiteBlue
        false -> WhiteRed
    }
}