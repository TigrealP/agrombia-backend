package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.carlos.agrombia.data.api.RetrofitClient
import com.carlos.agrombia.data.models.Report
import kotlinx.coroutines.launch

@Composable
fun ReportsScreen(onAddClick: () -> Unit) {
    var reports by remember { mutableStateOf<List<Report>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            reports = RetrofitClient.api.getReports()
        } catch (e: Exception) {
            // Manejar error silenciosamente o mostrar snackbar si se pasara el host state
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFFD32F2F) // Rojo para reportes/incidentes
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo Reporte", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                text = "Reportes e Incidentes",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFD32F2F)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFD32F2F))
            } else if (reports.isEmpty()) {
                Text("No hay reportes registrados.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(reports) { report ->
                        ReportCard(report)
                    }
                }
            }
        }
    }
}

@Composable
fun ReportCard(report: Report) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = report.tipo ?: "Incidente",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = report.descripcion ?: "Sin descripción",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = report.fecha?.take(10) ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
