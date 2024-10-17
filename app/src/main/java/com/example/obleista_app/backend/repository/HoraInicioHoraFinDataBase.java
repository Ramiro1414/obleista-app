package com.example.obleista_app.backend.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.obleista_app.backend.modelo.HoraInicioHoraFin;


@Database(entities = {HoraInicioHoraFin.class}, version = 1)
public abstract class HoraInicioHoraFinDataBase extends RoomDatabase {

    public abstract HoraInicioHoraFinDao horaInicioHoraFinDao();

}
