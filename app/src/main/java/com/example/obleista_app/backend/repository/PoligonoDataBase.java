package com.example.obleista_app.backend.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.obleista_app.backend.modelo.Poligono;

@Database(entities = {Poligono.class}, version = 2)
public abstract class PoligonoDataBase extends RoomDatabase {

    public abstract PoligonoDao poligonoDao();

}