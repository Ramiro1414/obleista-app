package com.example.obleista_app.backend.modelo;

import java.util.Collection;

public class PoligonoDTO {

    private int id;

    private double precio;

    private String nombre;

    private Collection<PuntoDTO> puntos;

    public PoligonoDTO(int id, double precio, String nombre, Collection<PuntoDTO> puntos) {
        this.id = id;
        this.precio = precio;
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<PuntoDTO> getPuntos() {
        return puntos;
    }

    public void setPuntos(Collection<PuntoDTO> puntos) {
        this.puntos = puntos;
    }
}
