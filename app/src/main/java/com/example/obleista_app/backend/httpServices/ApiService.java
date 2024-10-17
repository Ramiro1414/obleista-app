package com.example.obleista_app.backend.httpServices;

import com.example.obleista_app.backend.modelo.HorarioEstacionamientoDTO;
import com.example.obleista_app.backend.modelo.PatronPatente;
import com.example.obleista_app.backend.modelo.PoligonoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("comunicador/actualizar")
    Call<DataPackage> obtenerDatos();

    @POST("comunicador/registrar/obleista")
    Call<DataPackage> registrarDatos(@Body Object o);

    @POST("comunicador/registrar/conductor-sin-aplicacion")
    Call<DataPackage> registrarDatosConductor(@Body Object o);

    @GET("comunicador/actualizar/patrones")
    Call<DataPackage<List<PatronPatente>>> obtenerPatrones();

    @GET("comunicador/actualizar/poligonos")
    Call<DataPackage<List<PoligonoDTO>>> obtenerPoligonos();

    @GET("comunicador/actualizar/horarios")
    Call<DataPackage<List<HorarioEstacionamientoDTO>>> obtenerHorarios();
}
