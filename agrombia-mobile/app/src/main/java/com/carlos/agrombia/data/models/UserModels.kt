package com.carlos.agrombia.data.models

data class UserRegisterRequest(
    val nombre: String,
    val email: String,
    val contraseña: String,
    val rol: String = "Agricultor",
    val ubicacion: String? = null
)

data class RegisterResponse(
    val msg: String,
    val user_id: Int
)
