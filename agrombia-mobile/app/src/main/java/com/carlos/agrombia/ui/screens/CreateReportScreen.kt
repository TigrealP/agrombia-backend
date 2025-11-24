package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.carlos.agrombia.data.api.RetrofitClient
import com.carlos.agrombia.data.models.ReportCreateRequest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportScreen(
    onBackClick: () -> Unit,
    onReportCreated: () -> Unit
) {
    var tipo by remember { mutableStateOf("Incidente") }
    var descripcion by remember { mutableStateOf("") }
    
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Lista de tipos comunes
    val tiposDisponibles = listOf("Plaga", "Clima", "Incidente", "Mantenimiento", "Otro")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Reporte") },
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
        ) {
            // Dropdown para Tipo
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Reporte") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    tiposDisponibles.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                tipo = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción del problema") },
                modifier = Modifier.fillMaxWidth().height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (descripcion.isBlank()) {
                        scope.launch { snackbarHostState.showSnackbar("La descripción es obligatoria") }
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        try {
                            // Fecha actual en ISO 8601
                            val fechaActual = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
                            
                            RetrofitClient.api.createReport(
                                ReportCreateRequest(
                                    tipo = tipo,
                                    descripcion = descripcion,
                                    fecha = fechaActual
                                )
                            )
                            onReportCreated()
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
                else Text("Enviar Reporte")
            }
        }
    }
}
