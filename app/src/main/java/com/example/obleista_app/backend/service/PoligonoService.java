package com.example.obleista_app.backend.service;

import android.content.Context;

import androidx.room.Room;

import com.example.obleista_app.backend.modelo.Poligono;
import com.example.obleista_app.backend.repository.PoligonoDao;
import com.example.obleista_app.backend.repository.PoligonoDataBase;

import java.util.List;

public class PoligonoService {

    private PoligonoDao poligonoDao;

    public PoligonoService(Context context){
        PoligonoDataBase poligonoDataBase = Room.databaseBuilder(context, PoligonoDataBase.class, "poligono").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        poligonoDao = poligonoDataBase.poligonoDao();
    }

    public List<Poligono> findAll() {
        return this.poligonoDao.findAll();
    }

    public Poligono getById(int id) {
        return this.poligonoDao.findById(id);
    }

    public void save(Poligono poligono){
        this.poligonoDao.insert(poligono);
    }

    public void update(Poligono poligono){
        this.poligonoDao.update(poligono);
    }

    public void deleteAll() {
        this.poligonoDao.deleteAll();
    }

}
