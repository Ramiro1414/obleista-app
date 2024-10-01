package com.example.obleista_app.backend.httpServices;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServicePrueba {
    @GET("datos/actualizar")
    Call<DataPackage> obtenerDatos();

    @POST("datos/registrar/obleista")
    Call<DataPackage> registrarDatos(@Body Object o);
}
