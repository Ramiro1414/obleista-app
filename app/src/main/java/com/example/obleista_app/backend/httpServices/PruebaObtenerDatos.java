package com.example.obleista_app.backend.httpServices;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PruebaObtenerDatos {
    private static final String BASE_URL = "http://if012estm.fi.mdn.unp.edu.ar:28003/";
    private ApiServicePrueba apiService;
    private Context context;

    public PruebaObtenerDatos(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiServicePrueba.class);
    }

    public void obtenerDatosDelServidor() {
        Call<DataPackage> call = apiService.obtenerDatos();
        call.enqueue(new Callback<DataPackage>() {
            @Override
            public void onResponse(Call<DataPackage> call, Response<DataPackage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Registro recibido exitosamente:\n" + response.body().getData().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al recibir registro: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DataPackage> call, Throwable t) {
                Log.e("DataError", "Fallo en la solicitud: " + t.getMessage());
                Toast.makeText(context, "Error al recibir registro: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}


