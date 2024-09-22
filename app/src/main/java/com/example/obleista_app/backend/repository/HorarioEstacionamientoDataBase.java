package com.example.obleista_app.backend.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.obleista_app.backend.modelo.HorarioEstacionamiento;

@Database(entities = {HorarioEstacionamiento.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class HorarioEstacionamientoDataBase extends RoomDatabase {

    public abstract HorarioEstacionamientoDao horarioEstacionamientoDao();

}