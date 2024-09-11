package com.example.obleista_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencia al botón
        Button button = findViewById(R.id.miBoton);

        // Configuración del evento click
        button.setOnClickListener(v -> {
            // Muestra el mensaje Toast
            Toast.makeText(MainActivity.this, "Hello world", Toast.LENGTH_SHORT).show();
        });
    }
}