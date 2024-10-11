package com.example.obleista_app.backend.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;

import java.util.List;

@Dao
public interface RegistroAgenteTransitoDao {
    @Query("SELECT * FROM registro_agente_transito ORDER BY id")
    List<RegistroAgenteTransito> findAll();

    @Query("SELECT * FROM registro_agente_transito WHERE id = :id")
    RegistroAgenteTransito findById(int id);

    @Insert
    void insert(RegistroAgenteTransito registro);

    @Update
    void update(RegistroAgenteTransito registro);

    @Delete
    void delete(RegistroAgenteTransito registro);

    @Query("DELETE FROM registro_agente_transito")
    void deleteAll();
}
