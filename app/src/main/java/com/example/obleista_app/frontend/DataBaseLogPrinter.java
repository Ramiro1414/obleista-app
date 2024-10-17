package com.example.obleista_app.frontend;

import android.content.Context;
import android.util.Log;

import com.example.obleista_app.backend.modelo.HoraInicioHoraFin;
import com.example.obleista_app.backend.modelo.HorarioEstacionamiento;
import com.example.obleista_app.backend.modelo.PatronPatente;
import com.example.obleista_app.backend.modelo.Poligono;
import com.example.obleista_app.backend.modelo.Punto;
import com.example.obleista_app.backend.service.HoraInicioHoraFinService;
import com.example.obleista_app.backend.service.HorarioEstacionamientoService;
import com.example.obleista_app.backend.service.PatronesPatentesService;
import com.example.obleista_app.backend.service.PoligonoService;
import com.example.obleista_app.backend.service.PuntoService;

import java.util.List;

public class DataBaseLogPrinter {

    private HorarioEstacionamientoService horarioService;
    private HoraInicioHoraFinService horaService;
    private PoligonoService poligonoService;
    private PuntoService puntoService;
    private PatronesPatentesService patronesPatentesService;

    public DataBaseLogPrinter(Context context) {
        horarioService = new HorarioEstacionamientoService(context);
        horaService = new HoraInicioHoraFinService(context);
        poligonoService = new PoligonoService(context);
        puntoService = new PuntoService(context);
        patronesPatentesService = new PatronesPatentesService(context);

        imprimirDatosBD();
    }

    private void imprimirDatosBD() {
        // Horarios de estacionamiento
        List<HorarioEstacionamiento> horarios = horarioService.findAll();
        for (HorarioEstacionamiento horario : horarios) {
            Log.d("BD_Horario", "ID: " + horario.getId() +
                    ", Nombre: " + horario.getNombre() +
                    ", Fecha Inicio: " + horario.getFechaInicio() +
                    ", Fecha Fin: " + horario.getFechaFin());
        }

        // Rangos de horas por día
        List<HoraInicioHoraFin> rangosHoras = horaService.findAll();
        for (HoraInicioHoraFin rango : rangosHoras) {
            Log.d("BD_Hora", "ID: " + rango.getId() +
                    ", Hora Inicio: " + rango.getHoraInicio() +
                    ", Hora Fin: " + rango.getHoraFin() +
                    ", Horario ID: " + rango.getHorarioEstacionamientoId());
        }

        // Polígonos
        List<Poligono> poligonos = poligonoService.findAll();
        for (Poligono poligono : poligonos) {
            Log.d("BD_Poligono", "ID: " + poligono.getId() +
                    ", Nombre: " + poligono.getNombre() +
                    ", Precio: " + poligono.getPrecio());
        }

        // Puntos asociados a polígonos
        List<Punto> puntos = puntoService.findAll();
        for (Punto punto : puntos) {
            Log.d("BD_Punto", "ID: " + punto.getId() +
                    ", Latitud: " + punto.getLatitud() +
                    ", Longitud: " + punto.getLongitud() +
                    ", Polígono ID: " + punto.getPoligonoId());
        }

        // Patrones de patentes
        List<PatronPatente> patrones = patronesPatentesService.findAll();
        for (PatronPatente patron : patrones) {
            Log.d("BD_Patron", "Expresión Regular: " + patron.getExpresionRegularPatente());
        }
    }

}
