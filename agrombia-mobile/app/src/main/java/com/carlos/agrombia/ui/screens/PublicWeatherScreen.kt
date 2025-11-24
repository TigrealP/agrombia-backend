package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.carlos.agrombia.data.api.RetrofitClient
import com.carlos.agrombia.data.models.WeatherResponse
import com.carlos.agrombia.ui.components.WeatherCard
import com.carlos.agrombia.utils.LocationHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicWeatherScreen(onBackClick: () -> Unit) {
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    var weatherResult by remember { mutableStateOf<WeatherResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }

    // Launcher para ubicación automática
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            isLoading = true
            errorMessage = null // Limpiar error previo
            locationHelper.getCurrentLocation(
                onLocationFound = { lat, lon ->
                    latitud = lat.toString()
                    longitud = lon.toString()
                    // Auto-consultar cuando tenemos la ubicación
                    scope.launch {
                        try {
                            weatherResult = RetrofitClient.api.getPublicWeather(lat, lon)
                        } catch (e: Exception) {
                            errorMessage = "Error al consultar API: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                onError = { msg ->
                    isLoading = false
                    errorMessage = msg
                }
            )
        } else {
            errorMessage = "Se necesitan permisos de ubicación."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Consulta Meteorológica") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Ingresa coordenadas para ver el clima en tiempo real.",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Consultar mi ubicación actual")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = latitud,
                    onValueChange = { latitud = it },
                    label = { Text("Latitud") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = longitud,
                    onValueChange = { longitud = it },
                    label = { Text("Longitud") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    scope.launch {
                        try {
                            val lat = latitud.toDoubleOrNull() ?: 0.0
                            val lon = longitud.toDoubleOrNull() ?: 0.0
                            weatherResult = RetrofitClient.api.getPublicWeather(lat, lon)
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Consultar Clima")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (errorMessage != null) {
                Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            if (weatherResult != null) {
                WeatherCard(weather = weatherResult)
            }
        }
    }
}
