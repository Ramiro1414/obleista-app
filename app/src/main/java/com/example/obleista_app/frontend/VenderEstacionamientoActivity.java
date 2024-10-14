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
        timePickerInicio.setIs24HourView(true);
        TimePicker timePickerFin = findViewById(R.id.horaFin);
        timePickerFin.setIs24HourView(true);
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

            // Muestra el cuadro de diálogo para confirmar la venta
            mostrarDialogoConfirmacion(patente, horaInicioMillis, horaFinMillis);
        });

        Button btnRegresar = findViewById(R.id.buttonRegresar);
        btnRegresar.setOnClickListener(v -> {
            // Regresar a la actividad principal
            Intent intent = new Intent(VenderEstacionamientoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void mostrarDialogoConfirmacion(String patente, long horaInicioMillis, long horaFinMillis) {
        // Convertir milisegundos a horas y minutos para mostrar en el resumen
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTimeInMillis(horaInicioMillis);
        String horaInicioStr = String.format("%02d:%02d", calendarInicio.get(Calendar.HOUR_OF_DAY), calendarInicio.get(Calendar.MINUTE));

        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTimeInMillis(horaFinMillis);
        String horaFinStr = String.format("%02d:%02d", calendarFin.get(Calendar.HOUR_OF_DAY), calendarFin.get(Calendar.MINUTE));

        // Crear el cuadro de diálogo
        new AlertDialog.Builder(this)
                .setTitle("Confirmar venta")
                .setMessage("Patente: " + patente + "\nHora de inicio: " + horaInicioStr + "\nHora de fin: " + horaFinStr)
                .setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Si se confirma, guarda el registro en la base de datos
                        RegistroEstacionamientoSinApp registro = new RegistroEstacionamientoSinApp();
                        registro.setPatente(patente);
                        registro.setHoraInicio(horaInicioMillis);
                        registro.setHoraFin(horaFinMillis);

                        new Thread(() -> {
                            db.registroDao().insert(registro);
                            runOnUiThread(() -> {
                                Toast.makeText(VenderEstacionamientoActivity.this, "Estacionamiento vendido", Toast.LENGTH_SHORT).show();
                                // Regresar a la actividad principal
                                Intent intent = new Intent(VenderEstacionamientoActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        }).start();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cerrar el cuadro de diálogo
                        dialog.dismiss();
                    }
                })
                .show();
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
