package com.example.obleista_app.backend.service;

import android.content.Context;

import androidx.room.Room;

import com.example.obleista_app.backend.modelo.Punto;
import com.example.obleista_app.backend.repository.PuntoDao;
import com.example.obleista_app.backend.repository.PuntoDataBase;

import java.util.List;

public class PuntoService {

    private PuntoDao puntoDao;

    public PuntoService(Context context){
        PuntoDataBase puntoDataBase = Room.databaseBuilder(context, PuntoDataBase.class, "punto").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        puntoDao = puntoDataBase.puntoDao();
    }

    public List<Punto> findAll() {
        return this.puntoDao.findAll();
    }

    public Punto getById(int id) {
        return this.puntoDao.findById(id);
    }

    public void save(Punto punto) {
        this.puntoDao.insert(punto);
    }

    public void update(Punto punto){
        this.puntoDao.update(punto);
    }

    public void deleteAll() {
        this.puntoDao.deleteAll();
    }
}
