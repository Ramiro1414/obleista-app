package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.HoraInicioHoraFin;

import java.util.List;

@Dao
public interface HoraInicioHoraFinDao {

    @Query("SELECT * FROM hora_inicio_hora_fin ORDER BY id")
    List<HoraInicioHoraFin> findAll();

    @Query("SELECT * FROM hora_inicio_hora_fin WHERE id = :id")
    HoraInicioHoraFin findById(int id);

    @Query("DELETE FROM hora_inicio_hora_fin")
    void deleteAll();

    @Insert
    void insert(HoraInicioHoraFin horaInicioHoraFin);

    @Update
    void update(HoraInicioHoraFin horaInicioHoraFin);

    @Delete
    void delete(HoraInicioHoraFin horaInicioHoraFin);
}
