package com.example.obleista_app.backend.modelo;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = Poligono.class,
                parentColumns = "id",
                childColumns = "poligonoId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Punto.class,
                parentColumns = "id",
                childColumns = "puntoInicioId",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Punto.class,
                parentColumns = "id",
                childColumns = "puntoFinId",
                onDelete = ForeignKey.CASCADE)
})
public class LineaPoligono {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int poligonoId;  // Clave foránea que referencia a la tabla Poligono
    private int puntoInicioId;  // Clave foránea que referencia a la tabla Punto
    private int puntoFinId;  // Clave foránea que referencia a la tabla Punto

    public LineaPoligono() {
    }

    public LineaPoligono(int id, int poligonoId, int puntoInicioId, int puntoFinId) {
        this.id = id;
        this.poligonoId = poligonoId;
        this.puntoInicioId = puntoInicioId;
        this.puntoFinId = puntoFinId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoligonoId() {
        return poligonoId;
    }

    public void setPoligonoId(int poligonoId) {
        this.poligonoId = poligonoId;
    }

    public int getPuntoInicioId() {
        return puntoInicioId;
    }

    public void setPuntoInicioId(int puntoInicioId) {
        this.puntoInicioId = puntoInicioId;
    }

    public int getPuntoFinId() {
        return puntoFinId;
    }

    public void setPuntoFinId(int puntoFinId) {
        this.puntoFinId = puntoFinId;
    }

    @Override
    public String toString() {
        return "LineaPoligono{" +
                "id=" + id +
                ", poligonoId=" + poligonoId +
                ", puntoInicioId=" + puntoInicioId +
                ", puntoFinId=" + puntoFinId +
                '}';
    }
}