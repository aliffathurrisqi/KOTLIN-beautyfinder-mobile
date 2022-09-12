package com.alfari.beautyfinder

data class LoginResponse(
    val username: String,
    val password: String,
    val email: String,
    val nama_lengkap: String,
    val img: String,
    val akses: String
    )
