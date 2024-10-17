package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "punto")
public class Punto {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double latitud;
    private double longitud;
    private int poligonoId;

    public Punto() {
    }

    public Punto(int id, double latitud, double longitud, int poligonoId) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.poligonoId = poligonoId;
    }

    public int getPoligonoId() {
        return poligonoId;
    }

    public void setPoligonoId(int poligonoId) {
        this.poligonoId = poligonoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Punto{" +
                "id=" + id +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                '}';
    }
}
