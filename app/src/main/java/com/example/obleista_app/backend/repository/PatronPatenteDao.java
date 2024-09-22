package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.PatronPatente;

import java.util.List;

@Dao
public interface PatronPatenteDao {
    @Query("SELECT * FROM patron_patente ORDER BY id")
    List<PatronPatente> findAll();

    @Query("SELECT * FROM patron_patente WHERE id = :id")
    PatronPatente findById(int id);

    @Insert
    void insert(PatronPatente patronPatente);

    @Update
    void update(PatronPatente patronPatente);

    @Delete
    void delete(PatronPatente patronPatente);
}