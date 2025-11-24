package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.carlos.agrombia.data.api.RetrofitClient
import com.carlos.agrombia.data.models.Task
import com.carlos.agrombia.data.models.TaskCreateRequest
import com.carlos.agrombia.data.models.WeatherResponse
import com.carlos.agrombia.ui.components.WeatherCard
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropDetailScreen(
    cropId: Int,
    cropName: String,
    onBackClick: () -> Unit
) {
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var tasks by remember { mutableStateOf<List<Task>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Estado para crear tarea (Dialog)
    var showCreateTaskDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Función para cargar datos
    fun loadData() {
        scope.launch {
            isLoading = true
            try {
                // Cargar Clima y Tareas en paralelo
                weather = RetrofitClient.api.getCropWeather(cropId)
                tasks = RetrofitClient.api.getTasksByCrop(cropId)
            } catch (e: Exception) {
                // Manejar error
            } finally {
                isLoading = false
            }
        }
    }

    LaunchedEffect(cropId) {
        loadData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cropName) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateTaskDialog = true },
                containerColor = Color(0xFF2E7D32),
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Nueva Tarea") }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading && weather == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. SECCIÓN CLIMA
                    item {
                        WeatherCard(weather)
                    }

                    // 2. SECCIÓN TAREAS
                    item {
                        Text(
                            text = "Tareas Pendientes",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (tasks.isEmpty()) {
                        item {
                            Text("No hay tareas programadas para este cultivo.", color = Color.Gray)
                        }
                    } else {
                        items(tasks) { task ->
                            TaskItem(task, onDelete = {
                                scope.launch {
                                    try {
                                        RetrofitClient.api.deleteTask(task.id ?: 0)
                                        loadData() // Recargar lista
                                    } catch (e: Exception) {
                                        // Error
                                    }
                                }
                            })
                        }
                    }
                    
                    // Espacio final para que el FAB no tape el último item
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }

    // DIÁLOGO CREAR TAREA
    if (showCreateTaskDialog) {
        CreateTaskDialog(
            onDismiss = { showCreateTaskDialog = false },
            onConfirm = { titulo, desc ->
                scope.launch {
                    try {
                        val fechaActual = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
                        RetrofitClient.api.createTask(
                            TaskCreateRequest(
                                titulo = titulo,
                                descripcion = desc,
                                fecha = fechaActual,
                                crop_id = cropId
                            )
                        )
                        showCreateTaskDialog = false
                        loadData() // Recargar lista
                    } catch (e: Exception) {
                        // Error
                    }
                }
            }
        )
    }
}

@Composable
fun TaskItem(task: Task, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Gray)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(task.titulo, fontWeight = FontWeight.Bold)
                    if (!task.descripcion.isNullOrBlank()) {
                        Text(task.descripcion, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}

@Composable
fun CreateTaskDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea") },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { 
                if (titulo.isNotBlank()) onConfirm(titulo, descripcion) 
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
