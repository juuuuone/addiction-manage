package com.example.addiction_manage.feature.model

data class SmokingGoal(
    val id: String = "",
    val goal: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
