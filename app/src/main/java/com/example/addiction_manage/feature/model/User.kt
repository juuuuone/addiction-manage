package com.example.addiction_manage.feature.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class User(
    val id: String = "",
    val email: String = "",
    val nickname: String = "",
    var defaultFriendEmail: String = "",
    var friends: MutableList<User> = mutableListOf(),
    val createdAt: String = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
)