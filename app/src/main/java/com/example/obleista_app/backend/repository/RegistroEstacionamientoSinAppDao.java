package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.RegistroEstacionamientoSinApp;

import java.util.List;

@Dao
public interface RegistroEstacionamientoSinAppDao {
    @Query("SELECT * FROM registro_estacionamiento_sin_app ORDER BY id")
    List<RegistroEstacionamientoSinApp> findAll();

    @Query("SELECT * FROM registro_estacionamiento_sin_app WHERE id = :id")
    RegistroEstacionamientoSinApp findById(int id);

    @Insert
    void insert(RegistroEstacionamientoSinApp patronPatente);

    @Update
    void update(RegistroEstacionamientoSinApp patronPatente);

    @Delete
    void delete(RegistroEstacionamientoSinApp patronPatente);
}