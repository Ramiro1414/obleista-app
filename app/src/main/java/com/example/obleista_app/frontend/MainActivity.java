package com.example.obleista_app.frontend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.httpServices.PruebaEnviarDatos;
import com.example.obleista_app.backend.httpServices.PruebaObtenerDatos;
import com.example.obleista_app.backend.httpServices.SubirRegistros;
import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;
import com.example.obleista_app.backend.repository.RegistroAgenteTransitoDataBase;
import com.example.obleista_app.backend.service.CameraManager;
import com.example.obleista_app.backend.service.HoraFechaDispositivo;

import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private ImageView imageView;
    private RegistroAgenteTransitoDataBase db;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa la base de datos
        db = Room.databaseBuilder(getApplicationContext(),
                        RegistroAgenteTransitoDataBase.class, "registro_agente_transito_db")
                .allowMainThreadQueries() // Para simplificar en pruebas
                .build();

        // Configura el botón
        Button botonVerRegistro = findViewById(R.id.botonVerRegistro);
        botonVerRegistro.setOnClickListener(v -> {
            // Cambia el ID a un ID existente para probar
            int idDePrueba = 3; // Cambia esto según el registro que quieras consultar
            obtenerRegistroPorId(idDePrueba);
        });

        renderTitulo();

        Button button = findViewById(R.id.miBoton);
        Button buttonHoraFecha = findViewById(R.id.miBotonHoraFecha);
        Button buttonPruebaData = findViewById(R.id.buttonPruebaData);
        Button buttonPruebaSendData = findViewById(R.id.buttonPruebaSendData);
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

    public void obtenerRegistroPorId(int id) {
        new Thread(() -> {
            RegistroAgenteTransito registro = db.registroDao().findById(id);
            if (registro != null) {
                runOnUiThread(() -> {
                    String mensaje = "ID: " + registro.getId() + "\n" +
                            "Hora Registro: " + new Timestamp(registro.getHoraRegistro()) + "\n" +
                            "Patente: " + registro.getPatente() + "\n" +
                            "Latitud: " + registro.getLatitud() + "\n" +
                            "Longitud: " + registro.getLongitud() + "\n" +
                            "Foto: " + registro.getFoto();
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Registro no encontrado", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}