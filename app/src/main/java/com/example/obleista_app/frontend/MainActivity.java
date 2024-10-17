package com.example.obleista_app.frontend;

import android.content.Intent;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.httpServices.ActualizacionService;
import com.example.obleista_app.backend.httpServices.SubirRegistros;
import com.example.obleista_app.backend.service.CameraManager;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private ImageView imageView;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderTitulo();

        Button button = findViewById(R.id.miBoton);
        Button buttonVenderEstacionamiento = findViewById(R.id.buttonVenderEstacionamiento);
        Button buttonSubirRegistros = findViewById(R.id.buttonSubirRegistros);
        Button buttonActualizar = findViewById(R.id.buttonActualziar);

        CameraManager cameraManager = new CameraManager(this, imageView);

        button.setOnClickListener(v -> cameraManager.abrirCamara());

        buttonSubirRegistros.setOnClickListener( v -> {
            SubirRegistros subirRegistros = new SubirRegistros(this);
            subirRegistros.enviarRegistrosASistemaCentralConFotos();
            subirRegistros.enviarRegistrosConductorSinApp();
        });

        buttonVenderEstacionamiento.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VenderEstacionamientoActivity.class);
            startActivity(intent);
        });

        buttonActualizar.setOnClickListener(v -> {
            ActualizacionService actualizacionService = new ActualizacionService(this);
            actualizacionService.actualizarPoligonosDeEstacionamiento();
            actualizacionService.actualizarPatronesDePatentes();
            actualizacionService.actualizarHorariosDeEstacionamiento();

            new DataBaseLogPrinter(this);
        });
    }

    public void renderTitulo() {
        TextView textViewTitle = findViewById(R.id.textView3321);
        String text = "EstacionAR obleistas";
        SpannableString spannableString = new SpannableString(text);

        // Obtener el color cyan de los recursos
        int cyanColor = getResources().getColor(R.color.cyan, getTheme());

        // Cambiar el color de "AR" a cyan
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(cyanColor);
        spannableString.setSpan(colorSpan, 8, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewTitle.setText(spannableString);
    }

}