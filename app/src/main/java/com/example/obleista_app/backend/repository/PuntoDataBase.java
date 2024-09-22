package com.example.obleista_app.backend.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.obleista_app.backend.modelo.Punto;

@Database(entities = {Punto.class}, version = 1)
public abstract class PuntoDataBase extends RoomDatabase {

    public abstract PuntoDao puntoDao();

}