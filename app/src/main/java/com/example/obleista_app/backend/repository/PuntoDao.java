package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.Punto;

import java.util.List;

@Dao
public interface PuntoDao {
    @Query("SELECT * FROM punto ORDER BY id")
    List<Punto> findAll();

    @Query("SELECT * FROM punto WHERE id = :id")
    Punto findById(int id);

    @Query("DELETE FROM punto")
    void deleteAll();

    @Insert
    void insert(Punto punto);

    @Update
    void update(Punto punto);

    @Delete
    void delete(Punto punto);
}