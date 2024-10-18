package com.example.obleista_app.backend.service;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

 /*
    CoordsManager es una clase que maneja tanto la verificación de permisos como la obtención de la ubicación.
    Se utiliza para separar estas responsabilidades del MainActivity y tener un código más organizado.
 */

public class CoordsManager {

    private final Activity activity;
    private final CoordsService coordsService;

    // Modificar el constructor para permitir la inyección de CoordsService
    public CoordsManager(Activity activity, CoordsService coordsService) {
        this.activity = activity;
        this.coordsService = coordsService != null ? coordsService : new CoordsService(activity, null);
    }

    public void requestCoordinates() {
        // ContextCompat.checkSelfPermission: Comprueba si el permiso de ubicación ya ha sido concedido.
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicita los permisos
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            obtenerCoordenadas();
        }
    }

    /*
        handlePermissionResult: Maneja la respuesta del usuario cuando se le solicita el permiso de ubicación.
     */
    public void handlePermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerCoordenadas();
            } else {
                Toast.makeText(activity, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
        obtenerCoordenadas: Llama a CoordsService para obtener la ubicación actual del dispositivo y muestra las coordenadas en un Toast.
     */
    private void obtenerCoordenadas() {
        coordsService.getLastLocation(location -> {
            if (location != null) {
                double latitud = location.getLatitude();
                double longitud = location.getLongitude();
                //Toast.makeText(activity, "Latitud: " + latitud + "\nLongitud: " + longitud, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface LocationCallback {
        void onLocationReceived(double latitud, double longitud);
    }

    public void getLatitudLongitud(LocationCallback callback) {
        coordsService.getLastLocation(location -> {
            if (location != null) {
                double latitud = location.getLatitude();
                double longitud = location.getLongitude();
                callback.onLocationReceived(latitud, longitud);
            }
        });
    }
}