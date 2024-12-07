package com.example.addiction_manage.feature.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Caffeine(
    val id: String = "",
    val email: String = "",
    val createdAt: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    val drinks: Int = 0,
)
