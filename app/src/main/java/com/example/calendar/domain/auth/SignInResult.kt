package com.example.calendar.domain.auth

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)
