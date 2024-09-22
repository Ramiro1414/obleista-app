package com.example.obleista_app.backend.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.obleista_app.backend.modelo.RegistroEstacionamientoSinApp;

@Database(entities = {RegistroEstacionamientoSinApp.class}, version = 1)
public abstract class RegistroEstacionamientoSinAppDataBase extends RoomDatabase {

    public abstract RegistroEstacionamientoSinAppDao registroDao();

}