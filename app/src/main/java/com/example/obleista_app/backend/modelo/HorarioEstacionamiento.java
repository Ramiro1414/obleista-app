package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "horario_estacionamiento")
public class HorarioEstacionamiento {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date fechaInicio;
    private Date fechaFin;
    private String nombre;

    public HorarioEstacionamiento() {
    }

    public HorarioEstacionamiento(int id, Date fechaInicio, Date fechaFin, String nombre) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "HorarioEstacionamiento{" +
                "id=" + id +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}