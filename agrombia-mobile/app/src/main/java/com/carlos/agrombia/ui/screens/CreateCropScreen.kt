package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.carlos.agrombia.data.models.CropCreateRequest
import com.carlos.agrombia.utils.LocationHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCropScreen(
    onBackClick: () -> Unit,
    onCropCreated: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }
    
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }

    // Launcher para pedir permisos
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        if (isGranted) {
            isLoading = true
            locationHelper.getCurrentLocation(
                onLocationFound = { lat, lon ->
                    latitud = lat.toString()
                    longitud = lon.toString()
                    isLoading = false
                    scope.launch { snackbarHostState.showSnackbar("Ubicación obtenida: $lat, $lon") }
                },
                onError = { msg ->
                    isLoading = false
                    scope.launch { snackbarHostState.showSnackbar(msg) }
                }
            )
        } else {
            scope.launch { snackbarHostState.showSnackbar("Se necesitan permisos para usar el GPS") }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Cultivo") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Cultivo (ej: Lote Norte)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = tipo,
                onValueChange = { tipo = it },
                label = { Text("Tipo (ej: Café, Maíz)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Ubicación Geográfica", style = MaterialTheme.typography.titleSmall)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Usar mi ubicación actual")
            }
            
            Spacer(modifier = Modifier.height(8.dp))

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
            Text(
                "Ej: Bogotá es Lat 4.71, Lon -74.07",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (nombre.isBlank() || latitud.isBlank() || longitud.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("Completa los campos obligatorios") }
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        try {
                            RetrofitClient.api.createCrop(
                                CropCreateRequest(
                                    nombre = nombre,
                                    tipo = tipo,
                                    latitud = latitud.toDoubleOrNull() ?: 0.0,
                                    longitud = longitud.toDoubleOrNull() ?: 0.0
                                )
                            )
                            onCropCreated() // Volver y recargar
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error: ${e.message}")
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
                else Text("Guardar Cultivo")
            }
        }
    }
}
