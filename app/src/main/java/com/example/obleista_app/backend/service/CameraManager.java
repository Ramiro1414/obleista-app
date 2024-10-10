package com.example.obleista_app.backend.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.obleista_app.frontend.CrearRegistroAgenteTransitoActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraManager {

    Uri imageUri;
    ImageView imageView;
    public ActivityResultLauncher<Intent> takePictureLauncher;
    private final Context context;
    private final String RUTA_IMAGENES = "Registros_Estacionamiento/";
    private String photoName;

    public CameraManager(AppCompatActivity activity, ImageView imageView) {
        this.context = activity;
        this.imageView = imageView;

        // Registra el ActivityResultLauncher directamente en la actividad
        this.takePictureLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        // Inicia la actividad CrearRegistroAgenteTransitoActivity
                        Log.d("BBBBBBBBBBBBBBBB", "EJECUTA ANTES");

                        String readPhotoName = readFromFile();

                        Intent intent = new Intent(context, CrearRegistroAgenteTransitoActivity.class);
                        intent.putExtra("photoName", readPhotoName);

                        clearFile();

                        context.startActivity(intent);
                    }
                });
    }

    public void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imagePath = createImage();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
        takePictureLauncher.launch(intent); // Usa el ActivityResultLauncher para iniciar la cámara
    }

    private Uri createImage() {
        Uri uri;
        ContentResolver resolver = context.getContentResolver();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        String imgName = String.valueOf(System.currentTimeMillis());
        Log.d("AAAAAAAAAAAAAAAAAAAAAA", imgName + ".jpg");
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/"+RUTA_IMAGENES);

        this.photoName = imgName+".jpg";

        // Llamar al método para escribir en el archivo
        writeToFile(this.photoName);

        Uri finalUri = resolver.insert(uri, contentValues);
        this.imageUri = finalUri;
        return finalUri;

    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    // Método para escribir en un archivo
    private void writeToFile(String data) {
        File file = new File(context.getFilesDir(), "tmp.dat"); // Archivo dentro del almacenamiento interno
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data.getBytes());
            fos.flush();
            Log.d("FILE WRITING", "Archivo tmp.dat escrito correctamente.");
        } catch (IOException e) {
            Log.e("FILE WRITING ERROR", "Error al escribir el archivo tmp.dat: " + e.getMessage());
        }
    }

    private String readFromFile() {
        File file = new File(context.getFilesDir(), "tmp.dat");
        StringBuilder stringBuilder = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(file)) {
            int character;
            while ((character = fis.read()) != -1) {
                stringBuilder.append((char) character);
            }
            Log.d("FILE READING", "Contenido leído del archivo: " + stringBuilder.toString());
        } catch (IOException e) {
            Log.e("FILE READING ERROR", "Error al leer el archivo tmp.dat: " + e.getMessage());
        }

        return stringBuilder.toString();
    }

    private void clearFile() {
        File file = new File(context.getFilesDir(), "tmp.dat");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Abre el archivo en modo escritura y no escribe nada, vaciando el contenido.
            fos.write(new byte[0]);
            Log.d("FILE CLEAR", "Archivo tmp.dat vaciado correctamente.");
        } catch (IOException e) {
            Log.e("FILE CLEAR ERROR", "Error al vaciar el archivo tmp.dat: " + e.getMessage());
        }
    }
}
