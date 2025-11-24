package com.carlos.agrombia.data.models

// --- CULTIVOS ---
data class Crop(
    val id: Int? = null,
    val nombre: String,
    val tipo: String?,
    val latitud: Double?,
    val longitud: Double?,
    val fecha_registro: String? = null
)

data class CropCreateRequest(
    val nombre: String,
    val tipo: String,
    val latitud: Double,
    val longitud: Double
)

// --- CLIMA (Estructura de OpenMeteo) ---
data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val current_weather: CurrentWeather
)

data class CurrentWeather(
    val temperature: Double,
    val windspeed: Double,
    val winddirection: Double,
    val weathercode: Int,
    val time: String
)

// --- TAREAS ---
data class Task(
    val id: Int? = null,
    val titulo: String,
    val descripcion: String?,
    val fecha: String?,
    val crop_id: Int
)

data class TaskCreateRequest(
    val titulo: String,
    val descripcion: String,
    val fecha: String, // ISO format
    val crop_id: Int
)

// --- REPORTES ---
data class ReportCreateRequest(
    val tipo: String,
    val descripcion: String,
    val fecha: String
)

data class Report(
    val id: Int? = null,
    val tipo: String?,
    val descripcion: String?,
    val fecha: String?
)
