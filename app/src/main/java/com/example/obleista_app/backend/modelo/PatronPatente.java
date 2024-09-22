package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "patron_patente")
public class PatronPatente {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String expresionRegularPatente;

    public PatronPatente() {
    }

    public PatronPatente(int id, String expresionRegularPatente) {
        this.id = id;
        this.expresionRegularPatente = expresionRegularPatente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpresionRegularPatente() {
        return expresionRegularPatente;
    }

    public void setExpresionRegularPatente(String expresionRegularPatente) {
        this.expresionRegularPatente = expresionRegularPatente;
    }

    @Override
    public String toString() {
        return "PatronPatente{" +
                "id=" + id +
                ", expresionRegularPatente='" + expresionRegularPatente + '\'' +
                '}';
    }
}