package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.HorarioEstacionamiento;

import java.util.List;

@Dao
public interface HorarioEstacionamientoDao {
    @Query("SELECT * FROM horario_estacionamiento ORDER BY id")
    List<HorarioEstacionamiento> findAll();

    @Query("SELECT * FROM horario_estacionamiento WHERE id = :id")
    HorarioEstacionamiento findById(int id);

    @Query("DELETE FROM horario_estacionamiento")
    void deleteAll();

    @Insert
    void insert(HorarioEstacionamiento horarioEstacionamiento);

    @Update
    void update(HorarioEstacionamiento horarioEstacionamiento);

    @Delete
    void delete(HorarioEstacionamiento horarioEstacionamiento);
}