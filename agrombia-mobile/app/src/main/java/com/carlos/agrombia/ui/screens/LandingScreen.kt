package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.carlos.agrombia.data.api.RetrofitClient
import com.carlos.agrombia.data.models.NewsItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    onLoginClick: () -> Unit,
    onWeatherClick: () -> Unit
) {
    var newsList by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            newsList = RetrofitClient.api.getNews()
        } catch (e: Exception) {
            // Ignorar error en landing
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agrombia") },
                actions = {
                    IconButton(onClick = onWeatherClick) {
                        Icon(Icons.Default.Search, contentDescription = "Clima")
                    }
                    Button(
                        onClick = onLoginClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF2E7D32)),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Ingresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                item {
                    Text(
                        text = "Bienvenido al Portal del Campo 🇨🇴",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Noticias recientes:", color = Color.Gray)
                }
                items(newsList) { news ->
                    NewsCard(news)
                }
            }
        }
    }
}
