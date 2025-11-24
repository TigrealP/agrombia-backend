package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.carlos.agrombia.data.api.RetrofitClient
import com.carlos.agrombia.data.models.Crop
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropsScreen(
    onAddClick: () -> Unit,
    onCropClick: (Int, String) -> Unit
) {
    var crops by remember { mutableStateOf<List<Crop>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Función para cargar cultivos
    fun loadCrops() {
        scope.launch {
            isLoading = true
            try {
                crops = RetrofitClient.api.getMyCrops()
            } catch (e: Exception) {
                // Manejar error
            } finally {
                isLoading = false
            }
        }
    }

    // Cargar al iniciar
    LaunchedEffect(Unit) {
        loadCrops()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF2E7D32)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Cultivo", tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text(
                text = "Mis Cultivos",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (crops.isEmpty()) {
                Text("No tienes cultivos registrados aún. ¡Crea uno!")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(crops) { crop ->
                        CropCard(crop, onClick = { onCropClick(crop.id ?: 0, crop.nombre) })
                    }
                }
            }
        }
    }
}

@Composable
fun CropCard(crop: Crop, onClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFF2E7D32))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = crop.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = crop.tipo ?: "Sin tipo", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
