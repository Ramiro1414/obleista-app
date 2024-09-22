package com.example.obleista_app.backend.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;

@Database(entities = {RegistroAgenteTransito.class}, version = 1)
public abstract class RegistroAgenteTransitoDataBase extends RoomDatabase {

    public abstract RegistroAgenteTransitoDao registroDao();
}
