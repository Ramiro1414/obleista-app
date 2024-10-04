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
import com.example.obleista_app.backend.httpServices.PruebaEnviarDatos;
import com.example.obleista_app.backend.httpServices.PruebaObtenerDatos;
import com.example.obleista_app.backend.service.CameraManager;
import com.example.obleista_app.backend.service.HoraFechaDispositivo;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderTitulo();

        Button button = findViewById(R.id.miBoton);
        Button buttonHoraFecha = findViewById(R.id.miBotonHoraFecha);
        Button buttonPruebaData = findViewById(R.id.buttonPruebaData);
        Button buttonPruebaSendData = findViewById(R.id.buttonPruebaSendData);
        Button buttonVenderEstacionamiento = findViewById(R.id.buttonVenderEstacionamiento);

        CameraManager cameraManager = new CameraManager(this, imageView);

        HoraFechaDispositivo horaFechaDispositivo = new HoraFechaDispositivo(MainActivity.this);

        button.setOnClickListener(v -> cameraManager.abrirCamara());

        buttonHoraFecha.setOnClickListener(v -> horaFechaDispositivo.mostrarHoraYFechaDispositivo());

        buttonPruebaData.setOnClickListener(v -> {
            PruebaObtenerDatos dataGetter = new PruebaObtenerDatos(this);
            dataGetter.obtenerDatosDelServidor();
        });

        buttonPruebaSendData.setOnClickListener(v -> {
            PruebaEnviarDatos dataSender = new PruebaEnviarDatos(this);
            dataSender.enviarRegistroConductor();
        });

        buttonVenderEstacionamiento.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VenderEstacionamientoActivity.class);
            startActivity(intent);
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