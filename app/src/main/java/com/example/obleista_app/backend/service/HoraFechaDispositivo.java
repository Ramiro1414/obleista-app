package com.example.obleista_app.backend.service;

import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class HoraFechaDispositivo {

    private final Context context;

    public HoraFechaDispositivo(Context context) {
        this.context = context;
    }

    public String obtenerHoraDelDispositivo() {

        // Obtener la hora actual utilizando Calendar
        Calendar calendar = Calendar.getInstance();

        // Obtener la hora, minutos y segundos
        int hours = calendar.get(Calendar.HOUR_OF_DAY);  // HOUR_OF_DAY para formato 24 horas
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        // Formatear la hora en hh:mm:ss
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void mostrarHoraDispositivo() {
        Toast.makeText(this.context, obtenerHoraDelDispositivo(), Toast.LENGTH_SHORT).show();
    }

    public String obtenerFechaDispositivo() {

        Calendar calendar = Calendar.getInstance();

        // Obtener el día, mes y año
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;  // Los meses en Calendar comienzan desde 0, por lo que agregamos 1
        int year = calendar.get(Calendar.YEAR);

        // Formatear la fecha en dd/MM/yyyy
        return String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
    }

    public void mostrarFechaDispositivo() {
        Toast.makeText(this.context, obtenerFechaDispositivo(), Toast.LENGTH_SHORT).show();
    }

    public String obtenerHoraYFechaDispositivo() {
        String hora = obtenerHoraDelDispositivo();
        String fecha = obtenerFechaDispositivo();
        return hora + " - " + fecha;
    }

    public void mostrarHoraYFechaDispositivo() {
        Toast.makeText(this.context, obtenerHoraYFechaDispositivo(), Toast.LENGTH_SHORT).show();
    }
}