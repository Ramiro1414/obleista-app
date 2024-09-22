package com.example.obleista_app.backend.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.obleista_app.backend.modelo.PatronPatente;

@Database(entities = {PatronPatente.class}, version = 1)
public abstract class PatronPatenteDataBase extends RoomDatabase {

    public abstract PatronPatenteDao patronPatenteDao();

}