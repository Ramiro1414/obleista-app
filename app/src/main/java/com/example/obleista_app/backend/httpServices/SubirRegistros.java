package com.example.obleista_app.backend.httpServices;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;
import com.example.obleista_app.backend.modelo.RegistroAgenteTransitoDTO;
import com.example.obleista_app.backend.modelo.RegistroEstacionamientoSinApp;
import com.example.obleista_app.backend.repository.RegistroAgenteTransitoDataBase;
import com.example.obleista_app.backend.repository.RegistroEstacionamientoSinAppDataBase;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
    private final ApiService apiService;
    private final Context context;
    private RegistroAgenteTransitoDataBase db;
    private RegistroEstacionamientoSinAppDataBase dbEstacionamientoSinApp;
    // Agrega un ExecutorService con un tamaño de hilo fijo
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // Ajusta el tamaño del pool según sea necesario
    private final static int VALOR_COMPRESION_IMAGEN = 70;
    private final static String RUTA_IMAGENES = "Registros_Estacionamiento/";

    public SubirRegistros(Context context) {
        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // Inicializa la base de datos
        this.db = Room.databaseBuilder(context.getApplicationContext(),
                        RegistroAgenteTransitoDataBase.class, "registro_agente_transito_db")
                .fallbackToDestructiveMigration() // Manejo de migraciones, ajusta según sea necesario
                .build();

        // Inicializa la base de datos
        this.dbEstacionamientoSinApp = Room.databaseBuilder(context.getApplicationContext(),
                        RegistroEstacionamientoSinAppDataBase.class, "registros_estacionamiento_db")
                .fallbackToDestructiveMigration() // Manejo de migraciones, ajusta según sea necesario
                .build();
    }

    /** Envia los registros de conductor sin app a la base de datos del sistema central */
    public void enviarRegistrosConductorSinApp() {
        // Primero obtenemos todos los registros de la base de datos
        executorService.execute(() -> {
            List<RegistroEstacionamientoSinApp> registros = dbEstacionamientoSinApp.registroDao().findAll();

            if (registros != null && !registros.isEmpty()) {
                for (RegistroEstacionamientoSinApp registro : registros) {
                    // Para cada registro, enviamos los datos al backend
                    enviarRegistroConductorSinApp(registro);
                }
                dbEstacionamientoSinApp.registroDao().deleteAll();
            } else {
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "No hay registros de estacionamiento de conductores sin la aplicacion para enviar", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    public void enviarRegistroConductorSinApp(RegistroEstacionamientoSinApp registro) {

        RegistroEstacionamientoSinApp registroAEnviar = new RegistroEstacionamientoSinApp();
        registroAEnviar.setId(0);
        registroAEnviar.setHoraInicio(registro.getHoraInicio());
        registroAEnviar.setHoraFin(registro.getHoraFin());
        registroAEnviar.setPatente(registro.getPatente());

        // Enviar el objeto usando Retrofit
        apiService.registrarDatosConductor(registroAEnviar).enqueue(new Callback<DataPackage>() {
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
                    eliminarFoto(registro.getFoto());
                }
                db.registroDao().deleteAll();
            } else {
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "No hay registros de agente de transito para enviar", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void eliminarFoto(String nombreFoto) {
        // Obtener el ContentResolver
        ContentResolver resolver = context.getContentResolver();

        // Construir la URI de la imagen a partir del MediaStore
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Images.Media.RELATIVE_PATH + "=? AND " + MediaStore.Images.Media.DISPLAY_NAME + "=?";
        String[] selectionArgs = new String[]{ "Pictures/" + RUTA_IMAGENES, nombreFoto };

        // Eliminar la foto utilizando el ContentResolver
        int rowsDeleted = resolver.delete(uri, selection, selectionArgs);
        if (rowsDeleted > 0) {
            Log.d("Eliminar Foto", "La foto " + nombreFoto + " ha sido eliminada correctamente.");
        } else {
            Log.e("Eliminar Foto", "No se pudo eliminar la foto " + nombreFoto + ". Verifica que exista.");
        }
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, VALOR_COMPRESION_IMAGEN, byteArrayOutputStream);

        // Convertir a arreglo de bytes
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray;
    }

}
