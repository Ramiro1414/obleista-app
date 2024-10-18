package com.example.obleista_app.backend.service;

import android.graphics.Bitmap;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.*;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class OCRService {
    private static final String TOKEN = "249bbb07b2b7ae4398610603ecf88530ac5a936b";
    private Context context;

    public OCRService(Context context) {
        this.context = context;
    }

    public void recognizePlate(Bitmap bitmap, PlateRecognitionCallback callback) {
        new Thread(() -> {
            try {
                // Convertir el Bitmap a un archivo temporal
                File tempFile = new File(context.getCacheDir(), "temp_image.png");
                FileOutputStream fos = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
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

                Log.d("OCRService", "Respuesta JSON: " + responseBody);

                JSONObject jsonResponse = new JSONObject(responseBody);

                if (jsonResponse.has("results")) {
                    JSONArray results = jsonResponse.getJSONArray("results");
                    String plate = null;

                    if (results.length() > 0) {
                        JSONObject bestResult = results.getJSONObject(0);
                        plate = bestResult.getString("plate");
                    }

                    String finalPlate = plate;
                    runOnUiThread(() -> callback.onPlateRecognized(finalPlate != null ? finalPlate : "No encontrada"));
                } else {
                    runOnUiThread(() -> callback.onPlateRecognized("No encontrada"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> callback.onPlateRecognized("Error en OCR"));
            }
        }).start();
    }

    // Ejecutar en el hilo principal
    private void runOnUiThread(Runnable runnable) {
        new android.os.Handler(context.getMainLooper()).post(runnable);
    }
}
