package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.LineaPoligono;

import java.util.List;

@Dao
public interface LineaPoligonoDao {
    @Query("SELECT * FROM linea_poligono ORDER BY id")
    List<LineaPoligono> findAll();

    @Query("SELECT * FROM linea_poligono WHERE id = :id")
    LineaPoligono findById(int id);

    @Insert
    void insert(LineaPoligono lineaPoligono);

    @Update
    void update(LineaPoligono lineaPoligono);

    @Delete
    void delete(LineaPoligono lineaPoligono);
}