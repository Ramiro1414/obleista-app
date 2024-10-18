package com.example.obleista_app.backend.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.util.Log;

import com.example.obleista_app.R;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.*;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class OCRService {
    private static final String TOKEN = "249bbb07b2b7ae4398610603ecf88530ac5a936b";
    private Context context; // Agrega el contexto

    public OCRService(Context context) {
        this.context = context; // Inicializa el contexto
    }

    public void recognizePlate(Bitmap bitmap) {
        new Thread(() -> {
            try {
                // Cargar la imagen desde los recursos
                //Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.demo2);

                // Convertir el Bitmap a un archivo temporal
                File tempFile = new File(context.getCacheDir(), "temp_image.png");
                FileOutputStream fos = new FileOutputStream(tempFile);
                // Guardar el Bitmap sin pérdida de calidad
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // PNG no comprime, 100% de calidad
                fos.flush();
                fos.close();

                // Crear el cliente de OkHttp
                OkHttpClient client = new OkHttpClient();

                // Preparar la solicitud
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("upload", tempFile.getName(),
                                RequestBody.create(MediaType.parse("image/png"), tempFile))
                        .build();

                Request request = new Request.Builder()
                        .url("https://api.platerecognizer.com/v1/plate-reader/")
                        .addHeader("Authorization", "Token " + TOKEN)
                        .post(requestBody)
                        .build();

                // Ejecutar la solicitud
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();

                // Imprimir la respuesta JSON para depuración
                Log.d("OCRService", "Respuesta JSON: " + responseBody);

                // Manejar la respuesta...
                JSONObject jsonResponse = new JSONObject(responseBody);

                // Verifica si 'results' está presente en el JSON
                if (jsonResponse.has("results")) {
                    JSONArray results = jsonResponse.getJSONArray("results");
                    String plate = null;

                    if (results.length() > 0) {
                        JSONObject bestResult = results.getJSONObject(0);
                        plate = bestResult.getString("plate");
                    }

                    // Muestra el resultado en un Log
                    if (plate != null) {
                        Log.d("OCRService", "Patente: " + plate);
                    } else {
                        Log.d("OCRService", "No se encontraron patentes.");
                    }
                } else {
                    Log.d("OCRService", "La propiedad 'results' no se encuentra en el JSON.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}