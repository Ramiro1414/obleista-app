package com.example.obleista_app.backend.modelo;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hora_inicio_hora_fin")
public class HoraInicioHoraFin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String horaInicio;

    private String horaFin;

    private int horarioEstacionamientoId;

    public HoraInicioHoraFin() {
    }

    public HoraInicioHoraFin(int id, String horaInicio, String horaFin, int horarioEstacionamientoId) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.horarioEstacionamientoId = horarioEstacionamientoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public int getHorarioEstacionamientoId() {
        return horarioEstacionamientoId;
    }

    public void setHorarioEstacionamientoId(int horarioEstacionamientoId) {
        this.horarioEstacionamientoId = horarioEstacionamientoId;
    }
}
