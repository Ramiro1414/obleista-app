package com.example.obleista_app.backend.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;

import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class CameraManager {

    Uri imageUri;
    ImageView imageView;
    public ActivityResultLauncher<Intent> takePictureLauncher;
    private final Context context;
    private final String RUTA_IMAGENES = "Registros_Estacionamiento/";

    public CameraManager(AppCompatActivity activity, ImageView imageView) {
        this.context = activity;
        this.imageView = imageView;

        // Registra el ActivityResultLauncher directamente en la actividad
        this.takePictureLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        // Mostrar la imagen en el ImageView
                        if (imageUri != null) {
                            Toast.makeText(this.context, "Imagen guardada en: " + imageUri, Toast.LENGTH_LONG).show();
                            imageView.setImageURI(imageUri);
                        }
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
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName+".jpg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/"+RUTA_IMAGENES);
        Uri finalUri = resolver.insert(uri, contentValues);
        imageUri = finalUri;
        return finalUri;

    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
}
