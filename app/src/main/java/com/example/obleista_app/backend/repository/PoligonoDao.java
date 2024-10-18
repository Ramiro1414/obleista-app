package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.Poligono;

import java.util.List;

@Dao
public interface PoligonoDao {
    @Query("SELECT * FROM poligono ORDER BY id")
    List<Poligono> findAll();

    @Query("SELECT * FROM poligono WHERE id = :id")
    Poligono findById(int id);

    @Query("SELECT * FROM poligono WHERE nombre = :nombre")
    Poligono findByNombre(String nombre);

    @Query("DELETE FROM poligono")
    void deleteAll();

    @Insert
    void insert(Poligono poligono);

    @Update
    void update(Poligono poligono);

    @Delete
    void delete(Poligono poligono);
}