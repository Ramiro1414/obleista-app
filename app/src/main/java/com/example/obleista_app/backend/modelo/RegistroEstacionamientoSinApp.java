package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.obleista_app.backend.repository.Converter;

import java.sql.Timestamp;

@Entity(tableName = "registro_estacionamiento_sin_app")
public class RegistroEstacionamientoSinApp {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @TypeConverters(Converter.class)
    private Timestamp horaInicio;
    @TypeConverters(Converter.class)
    private Timestamp horaFin;
    private String patente;

    public RegistroEstacionamientoSinApp() {
    }

    public RegistroEstacionamientoSinApp(int id, Timestamp horaInicio, Timestamp horaFin, String patente) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.patente = patente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Timestamp horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Timestamp getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Timestamp horaFin) {
        this.horaFin = horaFin;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    @Override
    public String toString() {
        return "RegistroEstacionamientoSinApp{" +
                "id=" + id +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", patente='" + patente + '\'' +
                '}';
    }
}