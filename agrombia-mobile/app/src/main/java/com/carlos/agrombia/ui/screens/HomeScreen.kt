package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.carlos.agrombia.data.api.RetrofitClient
import com.carlos.agrombia.data.models.NewsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var newsList by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Cargar noticias al iniciar
    LaunchedEffect(Unit) {
        try {
            newsList = RetrofitClient.api.getNews()
            isLoading = false
        } catch (e: Exception) {
            errorMessage = "No se pudieron cargar las noticias: ${e.message}"
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Noticias del Agro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32), // Verde Agrombia
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF2E7D32))
            } else if (errorMessage != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "😕", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage ?: "Error desconocido", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { 
                        isLoading = true
                        errorMessage = null
                        scope.launch {
                            try {
                                newsList = RetrofitClient.api.getNews()
                            } catch(e: Exception) {
                                errorMessage = e.message
                            } finally {
                                isLoading = false
                            }
                        }
                    }) {
                        Text("Reintentar")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Lo último en agricultura 🇨🇴",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    items(newsList) { news ->
                        NewsCard(news)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(news: NewsItem) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Imagen de la noticia
            if (!news.imagen.isNullOrEmpty()) {
                AsyncImage(
                    model = news.imagen,
                    contentDescription = "Imagen noticia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = news.titulo ?: "Sin título",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = news.fuente ?: "Fuente desconocida",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${news.fecha?.take(10) ?: ""}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = news.contenido ?: "No hay descripción disponible.",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray
                )
            }
        }
    }
}
