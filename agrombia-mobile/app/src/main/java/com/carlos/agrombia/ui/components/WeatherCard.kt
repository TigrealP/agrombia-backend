package com.carlos.agrombia.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlos.agrombia.data.models.WeatherResponse

@Composable
fun WeatherCard(weather: WeatherResponse?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Clima Actual", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            if (weather != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${weather.current_weather.temperature}°C",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Viento: ${weather.current_weather.windspeed} km/h", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Código: ${weather.current_weather.weathercode}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                Text("Sin datos", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
