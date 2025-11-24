package com.carlos.agrombia.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
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
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Ingresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).padding(16.dp)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column {
                        Text(
                            text = "Bienvenido al Portal del Campo 🇨🇴",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text("Noticias recientes:", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
                items(newsList) { news ->
                    NewsGridCard(news)
                }
            }
        }
    }
}
