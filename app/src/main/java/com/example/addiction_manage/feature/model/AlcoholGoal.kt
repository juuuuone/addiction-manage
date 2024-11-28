package com.example.addiction_manage.feature.model

data class AlcoholGoal(
    val id: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val goal: Int = 0,
)
