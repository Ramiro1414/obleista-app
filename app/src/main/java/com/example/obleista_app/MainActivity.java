package com.example.obleista_app;

import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        // Referencia al botón de la hora
        Button buttonHora = findViewById(R.id.miBotonHora);
        // Referencia al botón de la fecha
        Button buttonFecha = findViewById(R.id.miBotonFecha);
        // Referencia al botón de la fecha
        Button buttonHoraYFecha = findViewById(R.id.miBotonHoraYFecha);

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

        buttonHora.setOnClickListener(v -> obtenerHoraDelDispositivo());

        buttonFecha.setOnClickListener(v -> obtenerFechaDelDispositivo());

        buttonHoraYFecha.setOnClickListener(v -> obtenerHoraYFechaDelDispositivo());
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void obtenerHoraDelDispositivo() {

        // Obtener la hora actual utilizando Calendar
        Calendar calendar = Calendar.getInstance();

        // Obtener la hora, minutos y segundos
        int hours = calendar.get(Calendar.HOUR_OF_DAY);  // HOUR_OF_DAY para formato 24 horas
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        // Formatear la hora en hh:mm:ss
        String timeString = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
        Toast.makeText(MainActivity.this, timeString, Toast.LENGTH_SHORT).show();
    }

    public void obtenerFechaDelDispositivo() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = DateFormat.getDateInstance();
        Toast.makeText(MainActivity.this, dateFormat.format(currentTime), Toast.LENGTH_SHORT).show();
    }

    public void obtenerHoraYFechaDelDispositivo() {

        // Obtener la hora actual utilizando Calendar
        Calendar calendar = Calendar.getInstance();

        // Obtener la hora, minutos y segundos
        int hours = calendar.get(Calendar.HOUR_OF_DAY);  // HOUR_OF_DAY para formato 24 horas
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        // Formatear la hora en hh:mm:ss
        String horaString = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);

        // Obtener el día, mes y año
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;  // Los meses en Calendar comienzan desde 0, por lo que agregamos 1
        int year = calendar.get(Calendar.YEAR);

        // Formatear la fecha en dd/MM/yyyy
        String fechaString = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);

        Toast.makeText(MainActivity.this, horaString + " - " + fechaString, Toast.LENGTH_SHORT).show();
    }

}