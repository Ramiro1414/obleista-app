package com.example.obleista_app;

import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar si el dispositivo tiene cámara
        Log.d("camaraDispositivo", "DISPOSITIVO CON CAMARA: " + checkCameraHardware(this));

        // Referencia al botón
        Button button = findViewById(R.id.miBoton);
        // Referencia al botón de la camara
        Button buttonCamara = findViewById(R.id.miBotonCamara);

        // Configuración del evento click
        button.setOnClickListener(v -> {
            // Muestra el mensaje Toast
            Toast.makeText(MainActivity.this, "Hello world", Toast.LENGTH_SHORT).show();
            Log.d("botonPresionado", "BOTON PRESIONADO");
        });

        buttonCamara.setOnClickListener(v -> {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
        });
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

}