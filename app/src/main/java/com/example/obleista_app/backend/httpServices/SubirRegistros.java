package com.example.obleista_app.backend.httpServices;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;
import com.example.obleista_app.backend.modelo.RegistroAgenteTransitoDTO;
import com.example.obleista_app.backend.repository.RegistroAgenteTransitoDataBase;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubirRegistros {

    private static final String BASE_URL = "http://if012estm.fi.mdn.unp.edu.ar:28003/";
    private final ApiServicePrueba apiService;
    private final Context context;
    private RegistroAgenteTransitoDataBase db;
    // Agrega un ExecutorService con un tamaño de hilo fijo
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // Ajusta el tamaño del pool según sea necesario

    public SubirRegistros(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiServicePrueba.class);

        // Inicializa la base de datos
        this.db = Room.databaseBuilder(context.getApplicationContext(),
                        RegistroAgenteTransitoDataBase.class, "registro_agente_transito_db")
                .fallbackToDestructiveMigration() // Manejo de migraciones, ajusta según sea necesario
                .build();
    }

    public void enviarRegistrosASistemaCentral() {
        // Primero obtenemos todos los registros de la base de datos
        new Thread(() -> {
            List<RegistroAgenteTransito> registros = db.registroDao().findAll();

            if (registros != null && !registros.isEmpty()) {
                for (RegistroAgenteTransito registro : registros) {
                    // Para cada registro, enviamos los datos al backend
                    enviarRegistro(registro);
                }
                /* Se eliminan los registros de la base de datos local */
                db.registroDao().deleteAll();
                /* Se eliminan las fotografias del directorio Pictures/Registros_Estacionamiento */
            } else {
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "No hay registros para enviar", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void enviarRegistro(RegistroAgenteTransito registro) {

        Timestamp horaRegistro = new Timestamp(registro.getHoraRegistro());

        // Creamos un nuevo objeto para enviar, con el ID siempre en 0
        RegistroAgenteTransito registroParaEnviar = new RegistroAgenteTransito(
                0, // El ID se define como 0, como solicitaste
                horaRegistro,
                registro.getPatente(),
                registro.getLatitud(),
                registro.getLongitud(),
                null // Foto será null por ahora
        );

        // Enviar el objeto usando Retrofit
        apiService.registrarDatos(registroParaEnviar).enqueue(new Callback<DataPackage>() {
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

    public void enviarRegistrosASistemaCentralConFotos() {
        // Primero obtenemos todos los registros de la base de datos
        executorService.execute(() -> {
            List<RegistroAgenteTransito> registros = db.registroDao().findAll();

            if (registros != null && !registros.isEmpty()) {
                for (RegistroAgenteTransito registro : registros) {
                    // Para cada registro, enviamos los datos al backend
                    enviarRegistroConFoto(registro);
                }
                // Asegúrate de eliminar los registros después de que todos se hayan enviado
                db.registroDao().deleteAll();
                // Se pueden eliminar las fotografías también, si es necesario
            } else {
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "No hay registros para enviar", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void enviarRegistroConFoto(RegistroAgenteTransito registro) {

        Timestamp horaRegistro = new Timestamp(registro.getHoraRegistro());

        byte[] foto = convertirImagenEnArregloDeBytes(registro.getFoto());

        // Creamos un nuevo objeto para enviar, con el ID siempre en 0
        RegistroAgenteTransitoDTO registroParaEnviar = new RegistroAgenteTransitoDTO(
                0, // El ID se define como 0, como solicitaste
                horaRegistro,
                registro.getPatente(),
                registro.getLatitud(),
                registro.getLongitud(),
                foto
        );

        apiService.registrarDatos(registroParaEnviar).enqueue(new Callback<DataPackage>() {
            @Override
            public void onResponse(Call<DataPackage> call, Response<DataPackage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ((Activity) context).runOnUiThread(() -> {
                        Toast.makeText(context, "Registro enviado exitosamente", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    ((Activity) context).runOnUiThread(() -> {
                        Toast.makeText(context, "Error al enviar registro: " + response.code(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<DataPackage> call, Throwable t) {
                Log.e("DataError", "Fallo en la solicitud: " + t.getMessage());
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "Error al enviar registro: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void eliminarRegistrosYFotografias() {
        db.registroDao().deleteAll();
    }

    public byte[] convertirImagenEnArregloDeBytes(String imageName) {

        // Construir la ruta completa de la imagen
        String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/Registros_Estacionamiento/" + imageName;

        // Cargar la imagen desde la ruta
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        if (bitmap == null) {
            Log.e("IMAGE CONVERSION", "Error: Imagen no encontrada en la ruta especificada.");
            return null;
        }

        // Crear un ByteArrayOutputStream para convertir el bitmap a bytes
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Comprimir el bitmap en el formato deseado (por ejemplo, JPEG) y convertirlo a bytes
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        // Convertir a arreglo de bytes
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray;
    }

}
