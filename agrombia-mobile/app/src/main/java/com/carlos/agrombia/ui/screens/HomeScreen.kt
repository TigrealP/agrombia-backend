package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Text(
                            text = "Lo último en agricultura 🇨🇴",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    items(newsList) { news ->
                        NewsGridCard(news)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsGridCard(news: NewsItem) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth().height(220.dp)
    ) {
        Column {
            // Imagen
            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa la mitad superior
                    .fillMaxWidth()
            ) {
                if (!news.imagen.isNullOrEmpty()) {
                    AsyncImage(
                        model = news.imagen,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ExitToApp, null, tint = Color.Gray)
                    }
                }
            }

            // Contenido
            Column(
                modifier = Modifier
                    .weight(1f) // Ocupa la mitad inferior
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = news.titulo ?: "Sin título",
                    style = MaterialTheme.typography.labelLarge, // Texto más pequeño pero bold
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = news.fuente ?: "Fuente",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        }
    }
}
