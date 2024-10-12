package com.example.obleista_app.backend.modelo;

import java.sql.Timestamp;

public class RegistroAgenteTransitoDTO {
    private int id;
    private Long horaRegistro;
    private String patente;
    private double latitud;
    private double longitud;
    private byte[] foto; // Campo para almacenar rutas de imagen o formatos aceptados

    public RegistroAgenteTransitoDTO() { }

    public RegistroAgenteTransitoDTO(int id, Timestamp horaRegistro, String patente, double latitud, double longitud, byte[] foto) {
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

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
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
                '}';
    }
}
