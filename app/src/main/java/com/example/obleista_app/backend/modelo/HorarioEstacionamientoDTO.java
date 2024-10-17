package com.example.obleista_app.backend.modelo;

import java.util.Collection;

public class HorarioEstacionamientoDTO {
    private int id;

    private String nombre;

    private String fechaInicio;
    private String fechaFin;

    private Collection<HoraInicioHoraFinDTO> horariosDelDia;

    public HorarioEstacionamientoDTO(int id, String nombre, String fechaInicio, String fechaFin, Collection<HoraInicioHoraFinDTO> horariosDelDia) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.horariosDelDia = horariosDelDia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Collection<HoraInicioHoraFinDTO> getHorariosDelDia() {
        return horariosDelDia;
    }

    public void setHorariosDelDia(Collection<HoraInicioHoraFinDTO> horariosDelDia) {
        this.horariosDelDia = horariosDelDia;
    }
}
