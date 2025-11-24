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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)), // Verde muy claro
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Clima Actual", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            if (weather != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${weather.current_weather.temperature}°C",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Viento: ${weather.current_weather.windspeed} km/h")
                        Text("Código: ${weather.current_weather.weathercode}")
                    }
                }
            } else {
                Text("Sin datos", color = Color.Gray)
            }
        }
    }
}
