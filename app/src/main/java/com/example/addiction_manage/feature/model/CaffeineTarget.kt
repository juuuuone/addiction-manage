package com.example.addiction_manage.feature.model

data class CaffeineTarget(
    val id: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val target: Int = 0,
)
