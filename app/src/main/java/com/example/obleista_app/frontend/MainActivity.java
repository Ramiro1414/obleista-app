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
        Button buttonHoraFecha = findViewById(R.id.miBotonHoraFecha);

        CameraManager cameraManager = new CameraManager(this, imageView);

        HoraFechaDispositivo horaFechaDispositivo = new HoraFechaDispositivo(MainActivity.this);

        button.setOnClickListener(v -> cameraManager.abrirCamara());

        buttonHoraFecha.setOnClickListener(v -> horaFechaDispositivo.mostrarHoraYFechaDispositivo());
    }

}