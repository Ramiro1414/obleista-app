package com.example.obleista_app.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.obleista_app.R;

import androidx.appcompat.app.AppCompatActivity;

public class VenderEstacionamientoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_estacionamiento);

        Button btnRegresar = findViewById(R.id.buttonRegresar);
        btnRegresar.setOnClickListener(v -> {
            // Regresar a la actividad principal
            Intent intent = new Intent(VenderEstacionamientoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
