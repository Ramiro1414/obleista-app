package com.example.obleista_app.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.modelo.RegistroEstacionamientoSinApp;
import com.example.obleista_app.backend.repository.RegistroEstacionamientoSinAppDataBase;

import android.app.AlertDialog;  // Agregar esta importación
import android.content.DialogInterface;  // Agregar esta importación

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.Calendar;

public class VenderEstacionamientoActivity extends AppCompatActivity {

    private RegistroEstacionamientoSinAppDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_estacionamiento);

        // Inicializa la base de datos
        db = Room.databaseBuilder(getApplicationContext(),
                        RegistroEstacionamientoSinAppDataBase.class, "registros_estacionamiento_db")
                .allowMainThreadQueries()
                .build();

        EditText inputPatente = findViewById(R.id.inputPatente);
        TimePicker timePickerInicio = findViewById(R.id.horaInicio);
        TimePicker timePickerFin = findViewById(R.id.horaFin);
        Button btnVender = findViewById(R.id.buttonVenderEstacionamiento);

        btnVender.setOnClickListener(v -> {
            String patente = inputPatente.getText().toString();
            int horaInicio = timePickerInicio.getHour();
            int minutoInicio = timePickerInicio.getMinute();
            int horaFin = timePickerFin.getHour();
            int minutoFin = timePickerFin.getMinute();

            // Convierte las horas y minutos en milisegundos
            long horaInicioMillis = convertirAHoraEnMilisegundos(horaInicio, minutoInicio);
            long horaFinMillis = convertirAHoraEnMilisegundos(horaFin, minutoFin);

            // Validaciones
            if (patente.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese la patente", Toast.LENGTH_SHORT).show();
                return;
            }

            if (horaInicioMillis == horaFinMillis) {
                Toast.makeText(this, "La hora de inicio no puede ser igual a la hora de fin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (horaFinMillis < horaInicioMillis) {
                Toast.makeText(this, "La hora de fin no puede ser anterior a la hora de inicio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Si las validaciones pasan, crea el registro y lo guarda en la base de datos
            RegistroEstacionamientoSinApp registro = new RegistroEstacionamientoSinApp();
            registro.setPatente(patente);
            registro.setHoraInicio(horaInicioMillis);
            registro.setHoraFin(horaFinMillis);

            new Thread(() -> {
                db.registroDao().insert(registro);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Estacionamiento vendido", Toast.LENGTH_SHORT).show();
                    // Regresar a la actividad principal
                    Intent intent = new Intent(VenderEstacionamientoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }).start();
        });

        Button btnRegresar = findViewById(R.id.buttonRegresar);
        btnRegresar.setOnClickListener(v -> {
            // Regresar a la actividad principal
            Intent intent = new Intent(VenderEstacionamientoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private long convertirAHoraEnMilisegundos(int hora, int minuto) {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        // Establecer la hora y el minuto
        calendar.set(Calendar.HOUR_OF_DAY, hora);
        calendar.set(Calendar.MINUTE, minuto);
        // Establecer los segundos y milisegundos a cero
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // Devolver el tiempo en milisegundos desde el epoch
        return calendar.getTimeInMillis();
    }
}
