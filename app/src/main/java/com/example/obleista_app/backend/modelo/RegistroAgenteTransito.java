package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.sql.Timestamp;

@Entity
public class RegistroAgenteTransito {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Timestamp hora;
    private String patente;
    private double latitud;
    private double longitud;
    private String foto; // Campo para almacenar rutas de imagen o formatos aceptados

    public RegistroAgenteTransito() {
    }

    public RegistroAgenteTransito(int id, Timestamp hora, String patente, double latitud, double longitud, String foto) {
        this.id = id;
        this.hora = hora;
        this.patente = patente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getHora() {
        return hora;
    }

    public void setHora(Timestamp hora) {
        this.hora = hora;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "RegistroAgenteTransito{" +
                "id=" + id +
                ", hora=" + hora +
                ", patente='" + patente + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", foto='" + foto + '\'' +
                '}';
    }
}