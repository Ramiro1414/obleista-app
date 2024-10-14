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

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.sql.Timestamp;

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
            int horaInicio = timePickerInicio.getCurrentHour();
            int minutoInicio = timePickerInicio.getCurrentMinute();
            int horaFin = timePickerFin.getCurrentHour();
            int minutoFin = timePickerFin.getCurrentMinute();

            // Convierte las horas y minutos en milisegundos
            long horaInicioMillis = convertirAHoraEnMilisegundos(horaInicio, minutoInicio);
            long horaFinMillis = convertirAHoraEnMilisegundos(horaFin, minutoFin);

            if (!patente.isEmpty()) {
                // Crea el registro y lo guarda en la base de datos
                RegistroEstacionamientoSinApp registro = new RegistroEstacionamientoSinApp();
                registro.setPatente(patente);
                registro.setHoraInicio(new Timestamp(horaInicioMillis));
                registro.setHoraFin(new Timestamp(horaFinMillis));

                new Thread(() -> {
                    db.registroDao().insert(registro);
                    runOnUiThread(() -> Toast.makeText(this, "Estacionamiento vendido", Toast.LENGTH_SHORT).show());
                }).start();
            } else {
                Toast.makeText(this, "Por favor, ingrese la patente", Toast.LENGTH_SHORT).show();
            }
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
        return (hora * 60L + minuto) * 60L * 1000L;
    }
}
