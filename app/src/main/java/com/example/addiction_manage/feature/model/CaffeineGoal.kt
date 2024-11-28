package com.example.addiction_manage.feature.model

data class CaffeineGoal(
    val id: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val goal: Int = 0,
)
