package com.example.obleista_app.frontend;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;
import com.example.obleista_app.backend.repository.RegistroAgenteTransitoDataBase;
import com.example.obleista_app.backend.service.CoordsManager;
import com.example.obleista_app.backend.service.CoordsService;
import com.example.obleista_app.backend.service.OCRService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class ModoObleistaActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    ImageView imagenSeleccionada;
    private EditText patenteEditText;
    private double latitud;
    private double longitud;
    private String photoName;
    private RegistroAgenteTransitoDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_obleista);

        db = Room.databaseBuilder(getApplicationContext(),
                        RegistroAgenteTransitoDataBase.class, "registro_agente_transito_db")
                .allowMainThreadQueries()  // No recomendado para operaciones grandes, pero para simplicidad aquí está bien
                .build();

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        CoordsService coordsService = new CoordsService(this, fusedLocationClient);
        CoordsManager coordsManager = new CoordsManager(this, coordsService);
        coordsManager.requestCoordinates();

        /* Obtener coordenadas de la ubicacion donde se toma la fotografia */
        coordsManager.getLatitudLongitud((latitud, longitud) -> {
            this.latitud = latitud;
            this.longitud = longitud;
        });

        imagenSeleccionada = findViewById(R.id.displayImageView);
        patenteEditText = findViewById(R.id.ingresarPatente);

        /* Boton para guardar registro */
        Button btnGuardar = findViewById(R.id.buttonGuardar);
        btnGuardar.setOnClickListener(v -> {
            String patente = patenteEditText.getText().toString().trim(); // Eliminar espacios en blanco al inicio y al final

            if (patente.isEmpty()) {
                // Mostrar Toast si el campo está vacío
                Toast.makeText(ModoObleistaActivity.this, "Debe ingresar la patente", Toast.LENGTH_SHORT).show();
            } else if (this.photoName == null) {
                // Mostrar Toast si no se ha tomado una fotografía
                Toast.makeText(ModoObleistaActivity.this, "Debe tomar una fotografía", Toast.LENGTH_SHORT).show();
            } else {
                // Si la patente no está vacía y hay una imagen, proceder a guardar el registro
                Calendar calendar = Calendar.getInstance();
                long timeInMillis = calendar.getTimeInMillis();
                insertarRegistro(patente, this.latitud, this.longitud, timeInMillis, this.photoName);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        /* Abrir camara para tomar fotografia */
        Button btnCamara = findViewById(R.id.buttonCamara);
        btnCamara.setOnClickListener(v -> askCameraPermissions());

        /* Regresar a la pantalla principal */
        Button btnRegresar = findViewById(R.id.buttonRegresar);
        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(ModoObleistaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {android.Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");

            OCRService ocrService = new OCRService(this);
            ocrService.recognizePlate(image, plate -> {
                Toast.makeText(this, "Patente: " + plate, Toast.LENGTH_LONG).show();

                // Guardar la imagen en almacenamiento externo
                String savedImagePath = guardarImagenEnAlmacenamientoExterno(image);

                if (savedImagePath != null) {
                    imagenSeleccionada.setImageBitmap(image);
                    Log.d("IMAGEN", "Imagen guardada en: " + savedImagePath);
                } else {
                    Log.e("IMAGEN", "Error al guardar la imagen");
                }
            });
        }
    }

    private String guardarImagenEnAlmacenamientoExterno(Bitmap image) {
        // Obtener el ContentResolver y el nombre del archivo
        ContentResolver resolver = getContentResolver();
        String imgName = System.currentTimeMillis() + ".jpg";
        String imagePath = "Pictures/Registros_Estacionamiento/";

        this.photoName = imgName;

        // Crear ContentValues para almacenar la imagen
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imgName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, imagePath);

        // Insertar la imagen en MediaStore y obtener la URI
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (uri == null) {
            return null; // Error al insertar la imagen
        }

        // Guardar el bitmap en el almacenamiento externo
        try {
            OutputStream outputStream = resolver.openOutputStream(uri);
            if (outputStream != null) {
                image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            }
        } catch (IOException e) {
            Log.e("ERROR", e.toString());
            return null; // Error al guardar la imagen
        }

        // Devolver la ruta donde se guardó la imagen
        return imagePath + imgName;
    }

    public void insertarRegistro(String patente, double latitud, double longitud, long horaRegistro, String foto) {

        // Crea un nuevo objeto RegistroAgenteTransito
        RegistroAgenteTransito registro = new RegistroAgenteTransito();
        registro.setHoraRegistro(horaRegistro);
        registro.setPatente(patente);
        registro.setLatitud(latitud);
        registro.setLongitud(longitud);
        registro.setFoto(foto);

        // Inserta el registro en la base de datos
        new Thread(() -> {
            db.registroDao().insert(registro);
        }).start();
    }


}
