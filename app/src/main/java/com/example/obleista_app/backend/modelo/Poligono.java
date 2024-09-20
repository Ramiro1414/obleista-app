package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Poligono {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double precio;

    public Poligono() {
    }

    public Poligono(int id, double precio) {
        this.id = id;
        this.precio = precio;
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

    @Override
    public String toString() {
        return "Poligono{" +
                "id=" + id +
                ", precio=" + precio +
                '}';
    }
}