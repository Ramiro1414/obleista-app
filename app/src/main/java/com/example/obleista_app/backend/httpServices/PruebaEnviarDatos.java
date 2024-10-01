package com.example.obleista_app.backend.httpServices;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PruebaEnviarDatos {
    private static final String BASE_URL = "http://if012estm.fi.mdn.unp.edu.ar:28003/";
    private final ApiServicePrueba apiService;
    private final Context context;

    public PruebaEnviarDatos(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiServicePrueba.class);
    }

    public void enviarRegistroConductor() {

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        Timestamp hora = new Timestamp(calendar.getTimeInMillis());
        String patente = "ABC123";
        double latitud = -42.87654321;
        double longitud = -65.12345678;
        String foto = "foto";
        RegistroAgenteTransito registroAgenteTransito = new RegistroAgenteTransito(0, hora, patente, longitud, latitud, foto);

        // Envia el objeto a través de Retrofit
        apiService.registrarDatos(registroAgenteTransito).enqueue(new Callback<DataPackage>() {
            @Override
            public void onResponse(Call<DataPackage> call, Response<DataPackage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Registro enviado exitosamente:\n" + response.body().getData().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al enviar registro: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DataPackage> call, Throwable t) {
                Log.e("DataError", "Fallo en la solicitud: " + t.getMessage());
                Toast.makeText(context, "Error al enviar registro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}