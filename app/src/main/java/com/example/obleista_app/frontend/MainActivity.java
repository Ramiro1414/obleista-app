package com.example.obleista_app.frontend;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.service.CameraManager;
import com.example.obleista_app.backend.service.HoraFechaDispositivo;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.miBoton);
        Button buttonCamara = findViewById(R.id.miBotonCamara);
        Button buttonHora = findViewById(R.id.miBotonHora);
        Button buttonFecha = findViewById(R.id.miBotonFecha);
        Button buttonHoraYFecha = findViewById(R.id.miBotonHoraYFecha);

        // Configuración del evento click
        button.setOnClickListener(v -> {
            // Muestra el mensaje Toast
            Toast.makeText(MainActivity.this, "Hello world", Toast.LENGTH_SHORT).show();
            Log.d("botonPresionado", "BOTON PRESIONADO");
        });

        CameraManager cameraManager = new CameraManager(this, imageView);

        buttonCamara.setOnClickListener(v -> cameraManager.abrirCamara());

        HoraFechaDispositivo horaFechaDispositivo = new HoraFechaDispositivo(MainActivity.this);

        buttonHora.setOnClickListener(v -> horaFechaDispositivo.mostrarHoraDispositivo());

        buttonFecha.setOnClickListener(v -> horaFechaDispositivo.mostrarFechaDispositivo());

        buttonHoraYFecha.setOnClickListener(v -> horaFechaDispositivo.mostrarHoraYFechaDispositivo());
    }

}