package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.sql.Timestamp;

@Entity(tableName = "registro_agente_transito")
public class RegistroAgenteTransito {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Long horaRegistro;
    private String patente;
    private double latitud;
    private double longitud;
    private String foto; // Campo para almacenar rutas de imagen o formatos aceptados

    public RegistroAgenteTransito() {
    }

    public RegistroAgenteTransito(int id, Timestamp horaRegistro, String patente, double latitud, double longitud, String foto) {
        this.id = id;
        this.horaRegistro = horaRegistro.getTime();
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

    public Long getHoraRegistro() { return horaRegistro; }

    public void setHoraRegistro(Long hora) {
        this.horaRegistro = hora;
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
                ", hora=" + horaRegistro +
                ", patente='" + patente + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", foto='" + foto + '\'' +
                '}';
    }
}