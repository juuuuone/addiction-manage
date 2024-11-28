package com.example.addiction_manage.feature.model

data class AlcoholGoal(
    val id: String = "",
    val userId: String = "",
    val goal: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)
