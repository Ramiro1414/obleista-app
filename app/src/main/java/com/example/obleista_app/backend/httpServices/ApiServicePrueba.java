package com.example.obleista_app.backend.httpServices;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServicePrueba {
    @GET("comunicador/actualizar")
    Call<DataPackage> obtenerDatos();

    @POST("comunicador/registrar/obleista")
    Call<DataPackage> registrarDatos(@Body Object o);

    @POST("comunicador/registrar/conductor-sin-aplicacion")
    Call<DataPackage> registrarDatosConductor(@Body Object o);
}
