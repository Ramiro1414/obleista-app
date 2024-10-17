package com.example.obleista_app.backend.httpServices;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.example.obleista_app.backend.modelo.HoraInicioHoraFin;
import com.example.obleista_app.backend.modelo.HoraInicioHoraFinDTO;
import com.example.obleista_app.backend.modelo.HorarioEstacionamiento;
import com.example.obleista_app.backend.modelo.HorarioEstacionamientoDTO;
import com.example.obleista_app.backend.modelo.PatronPatente;
import com.example.obleista_app.backend.modelo.Poligono;
import com.example.obleista_app.backend.modelo.PoligonoDTO;
import com.example.obleista_app.backend.modelo.Punto;
import com.example.obleista_app.backend.modelo.PuntoDTO;
import com.example.obleista_app.backend.service.HoraInicioHoraFinService;
import com.example.obleista_app.backend.service.HorarioEstacionamientoService;
import com.example.obleista_app.backend.service.PatronesPatentesService;
import com.example.obleista_app.backend.service.PoligonoService;
import com.example.obleista_app.backend.service.PuntoService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActualizacionService {
    private static final String BASE_URL = "http://if012estm.fi.mdn.unp.edu.ar:28003/";
    private final ApiService apiService;
    private final Context context;

    public ActualizacionService(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void actualizarHorariosDeEstacionamiento() {
        Call<DataPackage<List<HorarioEstacionamientoDTO>>> call = apiService.obtenerHorarios();
        call.enqueue(new Callback<DataPackage<List<HorarioEstacionamientoDTO>>>() {
            @Override
            public void onResponse(Call<DataPackage<List<HorarioEstacionamientoDTO>>> call,
                                   Response<DataPackage<List<HorarioEstacionamientoDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HorarioEstacionamientoService horarioService = new HorarioEstacionamientoService(context);
                    HoraInicioHoraFinService horaService = new HoraInicioHoraFinService(context);

                    List<HorarioEstacionamientoDTO> horariosNuevos = response.body().getData();

                    // Limpiar los datos anteriores
                    horarioService.deleteAll();
                    horaService.deleteAll();

                    for (HorarioEstacionamientoDTO horarioDTO : horariosNuevos) {
                        // Crear y guardar el nuevo horario de estacionamiento
                        HorarioEstacionamiento nuevoHorario = new HorarioEstacionamiento();
                        nuevoHorario.setId(horarioDTO.getId());
                        nuevoHorario.setNombre(horarioDTO.getNombre());
                        nuevoHorario.setFechaInicio(horarioDTO.getFechaInicio());
                        nuevoHorario.setFechaFin(horarioDTO.getFechaFin());

                        horarioService.save(nuevoHorario);
                        int horarioId = horarioDTO.getId();

                        // Guardar los rangos de horas asociados al horario
                        for (HoraInicioHoraFinDTO horaDTO : horarioDTO.getHorariosDelDia()) {
                            HoraInicioHoraFin nuevaHora = new HoraInicioHoraFin();
                            nuevaHora.setId(horaDTO.getId());
                            nuevaHora.setHoraInicio(horaDTO.getHoraInicio());
                            nuevaHora.setHoraFin(horaDTO.getHoraFin());
                            nuevaHora.setHorarioEstacionamientoId(horarioId);

                            horaService.save(nuevaHora);
                        }
                    }

                    Toast.makeText(context,
                            "Registros recibidos exitosamente. Total horarios: " + horarioService.findAll().size(),
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(context,
                            "Registros recibidos exitosamente. Total rangos de horas: " + horaService.findAll().size(),
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context,
                            "Error al recibir registro: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataPackage<List<HorarioEstacionamientoDTO>>> call, Throwable t) {
                Log.e("DataError", "Fallo en la solicitud: " + t.getMessage());
                Toast.makeText(context,
                        "Error al recibir registro: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    public void actualizarPatronesDePatentes(){
        Call<DataPackage<List<PatronPatente>>> call = apiService.obtenerPatrones();
        call.enqueue(new Callback<DataPackage<List<PatronPatente>>>() {
            @Override
            public void onResponse(Call<DataPackage<List<PatronPatente>>> call, Response<DataPackage<List<PatronPatente>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PatronesPatentesService patronesPatentesService = new PatronesPatentesService(context);

                    List<PatronPatente> patronesNuevos = response.body().getData();
                    patronesPatentesService.deleteAll();
                    for(PatronPatente patronPatente : patronesNuevos) {
                        PatronPatente nuevoPatronPatente = new PatronPatente();
                        nuevoPatronPatente.setExpresionRegularPatente(patronPatente.getExpresionRegularPatente());
                        patronesPatentesService.save(nuevoPatronPatente);
                    }

                    Toast.makeText(context, "Registros recibidos exitosamente. Total patrones: " + patronesPatentesService.findAll().size(), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context, "Error al recibir registro: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataPackage<List<PatronPatente>>> call, Throwable t) {
                Log.e("DataError", "Fallo en la solicitud: " + t.getMessage());
                Toast.makeText(context, "Error al recibir registro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void actualizarPoligonosDeEstacionamiento() {
        Call<DataPackage<List<PoligonoDTO>>> call = apiService.obtenerPoligonos();
        call.enqueue(new Callback<DataPackage<List<PoligonoDTO>>>() {
            @Override
            public void onResponse(Call<DataPackage<List<PoligonoDTO>>> call, Response<DataPackage<List<PoligonoDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PoligonoService poligonoService = new PoligonoService(context);
                    PuntoService puntoService = new PuntoService(context);

                    List<PoligonoDTO> poligonosNuevos = response.body().getData();

                    // Limpiar datos anteriores
                    poligonoService.deleteAll();
                    puntoService.deleteAll();

                    for (PoligonoDTO poligonoDTO : poligonosNuevos) {
                        // Crear y guardar el nuevo polígono
                        Poligono nuevoPoligono = new Poligono();
                        nuevoPoligono.setId(poligonoDTO.getId());
                        nuevoPoligono.setPrecio(poligonoDTO.getPrecio());
                        nuevoPoligono.setNombre(poligonoDTO.getNombre());
                        poligonoService.save(nuevoPoligono);
                        int poligonoId = poligonoDTO.getId();

                        // Guardar los puntos asociados al polígono
                        for (PuntoDTO puntoDTO : poligonoDTO.getPuntos()) {
                            Punto nuevoPunto = new Punto();
                            nuevoPunto.setId(puntoDTO.getId());
                            nuevoPunto.setLatitud(puntoDTO.getLatitud());
                            nuevoPunto.setLongitud(puntoDTO.getLongitud());
                            nuevoPunto.setPoligonoId(poligonoId);

                            puntoService.save(nuevoPunto);
                        }
                    }

                    Toast.makeText(context,
                            "Registros recibidos exitosamente. Total polígonos: " + poligonoService.findAll().size(),
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(context,
                            "Registros recibidos exitosamente. Total puntos: " + puntoService.findAll().size(),
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(context,
                            "Error al recibir registro: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataPackage<List<PoligonoDTO>>> call, Throwable t) {
                Log.e("DataError", "Fallo en la solicitud: " + t.getMessage());
                Toast.makeText(context,
                        "Error al recibir registro: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


}


