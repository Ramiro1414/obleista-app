package com.example.obleista_app.frontend;

import android.content.Intent;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.httpServices.SubirRegistros;
import com.example.obleista_app.backend.modelo.RegistroEstacionamientoSinApp;
import com.example.obleista_app.backend.repository.RegistroEstacionamientoSinAppDataBase;
import com.example.obleista_app.backend.service.CameraManager;
import com.example.obleista_app.backend.service.HoraFechaDispositivo;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private ImageView imageView;
    private RegistroEstacionamientoSinAppDataBase db;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa la base de datos
        db = Room.databaseBuilder(getApplicationContext(),
                        RegistroEstacionamientoSinAppDataBase.class, "registros_estacionamiento_db")
                .allowMainThreadQueries() // Para simplificar en pruebas
                .build();

        renderTitulo();

        Button button = findViewById(R.id.miBoton);
        Button buttonHoraFecha = findViewById(R.id.miBotonHoraFecha);
        Button buttonVenderEstacionamiento = findViewById(R.id.buttonVenderEstacionamiento);
        Button buttonSubirRegistros = findViewById(R.id.buttonSubirRegistros);

        CameraManager cameraManager = new CameraManager(this, imageView);

        HoraFechaDispositivo horaFechaDispositivo = new HoraFechaDispositivo(MainActivity.this);

        button.setOnClickListener(v -> cameraManager.abrirCamara());

        buttonHoraFecha.setOnClickListener(v -> horaFechaDispositivo.mostrarHoraYFechaDispositivo());

        buttonSubirRegistros.setOnClickListener( v -> {
            SubirRegistros subirRegistros = new SubirRegistros(this);
            subirRegistros.enviarRegistrosASistemaCentralConFotos();
        });

        buttonVenderEstacionamiento.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VenderEstacionamientoActivity.class);
            startActivity(intent);
        });

        Button buttonSelectPrimerRegistro = findViewById(R.id.buttonSelectPrimerRegistro);
        buttonSelectPrimerRegistro.setOnClickListener(v -> {
                // Ejecutar la consulta en un hilo separado para evitar bloquear la interfaz de usuario
                new Thread(() -> {
                    List<RegistroEstacionamientoSinApp> registros = db.registroDao().findAll();
                    if (registros != null && !registros.isEmpty()) {
                        RegistroEstacionamientoSinApp primerRegistro = registros.get(0);
                        String mensaje = "Primer registro: " + primerRegistro.getPatente() + ", Hora Inicio: " + primerRegistro.getHoraFin() + ", Hora Fin: " + primerRegistro.getHoraFin();
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show());
                    } else {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "No hay registros en la base de datos", Toast.LENGTH_SHORT).show());
                    }
                }).start();
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