package com.example.obleista_app;

import static android.media.MediaRecorder.VideoSource.CAMERA;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Uri imageUri;
    ImageView imageView;
    private ActivityResultLauncher<Intent> takePictureLauncher;

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

        // Configuración del ActivityResultLauncher para la cámara
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Mostrar la imagen en el ImageView
                        if (imageUri != null) {
                            Toast.makeText(this, "Imagen guardada en: " + imageUri.toString(), Toast.LENGTH_LONG).show();
                            imageView.setImageURI(imageUri);
                        }
                    }
                });

        buttonCamara.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imagePath = createImage();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
            takePictureLauncher.launch(intent); // Usa el ActivityResultLauncher para iniciar la cámara
        });

        buttonHora.setOnClickListener(v -> obtenerHoraDelDispositivo());

        buttonFecha.setOnClickListener(v -> obtenerFechaDelDispositivo());

        buttonHoraYFecha.setOnClickListener(v -> obtenerHoraYFechaDelDispositivo());
    }

    private Uri createImage() {
        Uri uri = null;
        ContentResolver resolver = getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String imgName = String.valueOf(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/"+"My Images/");
        Uri finalUri = resolver.insert(uri, contentValues);
        imageUri = finalUri;
        return finalUri;

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