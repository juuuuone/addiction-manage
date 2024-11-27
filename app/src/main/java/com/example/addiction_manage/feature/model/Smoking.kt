package com.example.addiction_manage.feature.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Smoking(
    val id: String = "",
    val createdAt: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    val cigarettes: Int = 0,
)
