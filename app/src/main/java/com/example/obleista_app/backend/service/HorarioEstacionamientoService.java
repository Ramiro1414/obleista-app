package com.example.obleista_app.backend.service;

import android.content.Context;

import androidx.room.Room;

import com.example.obleista_app.backend.modelo.HorarioEstacionamiento;
import com.example.obleista_app.backend.repository.HorarioEstacionamientoDao;
import com.example.obleista_app.backend.repository.HorarioEstacionamientoDataBase;

import java.util.List;

public class HorarioEstacionamientoService {

    private HorarioEstacionamientoDao horarioEstacionamientoDao;

    public HorarioEstacionamientoService (Context context){
        HorarioEstacionamientoDataBase horarioEstacionamientoDataBase = Room.databaseBuilder(context, HorarioEstacionamientoDataBase.class, "horario_estacionamiento").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        horarioEstacionamientoDao = horarioEstacionamientoDataBase.horarioEstacionamientoDao();
    }

    public List<HorarioEstacionamiento> findAll() {
        return this.horarioEstacionamientoDao.findAll();
    }

    public HorarioEstacionamiento getById(int id) {
        return this.horarioEstacionamientoDao.findById(id);
    }

    public void save(HorarioEstacionamiento horarioEstacionamiento){
        this.horarioEstacionamientoDao.insert(horarioEstacionamiento);
    }

    public void update(HorarioEstacionamiento horarioEstacionamiento){
        this.horarioEstacionamientoDao.update(horarioEstacionamiento);
    }

    public void deleteAll() {
        this.horarioEstacionamientoDao.deleteAll();
    }
}
