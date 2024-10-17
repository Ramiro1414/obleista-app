package com.example.obleista_app.backend.modelo;

public class HoraInicioHoraFinDTO {

    private int id;

    private String horaInicio;

    private String horaFin;

    public HoraInicioHoraFinDTO(int id, String horaInicio, String horaFin) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
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
}
