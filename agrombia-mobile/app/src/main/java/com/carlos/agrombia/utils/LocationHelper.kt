package com.carlos.agrombia.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationHelper(private val context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        onLocationFound: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Usamos getCurrentLocation para obtener una ubicación fresca (más preciso que getLastLocation)
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onLocationFound(location.latitude, location.longitude)
                } else {
                    onError("No se pudo obtener la ubicación. Asegúrate de tener el GPS activo.")
                }
            }.addOnFailureListener {
                onError("Error al obtener ubicación: ${it.message}")
            }
        } catch (e: SecurityException) {
            onError("Permisos de ubicación no otorgados.")
        }
    }
}
