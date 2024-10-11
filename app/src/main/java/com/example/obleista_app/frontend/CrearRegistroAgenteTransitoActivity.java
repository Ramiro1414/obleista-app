package com.example.obleista_app.frontend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.modelo.RegistroAgenteTransito;
import com.example.obleista_app.backend.repository.RegistroAgenteTransitoDataBase;
import com.example.obleista_app.backend.service.CoordsManager;
import com.example.obleista_app.backend.service.CoordsService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.sql.Timestamp;

public class CrearRegistroAgenteTransitoActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText patenteEditText, horaRegistroText, latitudText, longitudText;
    private Button guardarButton;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private RegistroAgenteTransitoDataBase db;

    // atributos utilizados para crear registro
    private String photoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_registro_agente_transito);

        db = Room.databaseBuilder(getApplicationContext(),
                        RegistroAgenteTransitoDataBase.class, "registro_agente_transito_db")
                .allowMainThreadQueries()  // No recomendado para operaciones grandes, pero para simplicidad aquí está bien
                .build();

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        CoordsService coordsService = new CoordsService(this, fusedLocationClient);
        CoordsManager coordsManager = new CoordsManager(this, coordsService);
        coordsManager.requestCoordinates();

        // Obtener el nombre de la imagen pasada por el intent
        this.photoName = getIntent().getStringExtra("photoName");

        // Vincula los elementos de la UI
        imageView = findViewById(R.id.imageView);
        patenteEditText = findViewById(R.id.ingresarPatente);
        horaRegistroText = findViewById(R.id.horaRegistro);
        latitudText = findViewById(R.id.latitud);
        longitudText = findViewById(R.id.longitud);

        guardarButton = findViewById(R.id.buttonGuardar);

        // Obtiene la hora actual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()); // Formato 24 horas (usa "hh:mm a" para 12 horas con AM/PM)
        String currentTime = dateFormat.format(calendar.getTime()); // usado para mostrar en pantalla
        long timeInMillis = calendar.getTimeInMillis(); // usado para mandar a la base de datos

        // Crea un objeto Timestamp con el valor actual
        Timestamp currentTimestamp = new Timestamp(timeInMillis);

        // Asigna la hora al EditText
        horaRegistroText.setText(currentTime);

        coordsManager.getLatitudLongitud((latitud, longitud) -> {
            // Aquí tienes la latitud y longitud disponibles
            latitudText.setText(String.valueOf(latitud));
            longitudText.setText(String.valueOf(longitud));
        });

        // Solicita permisos si no están ya otorgados
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                cargarImagen();
            }
        } else {
            cargarImagen(); // En versiones anteriores no se necesitan permisos en tiempo de ejecución
        }

        // Implementa el botón de guardar (aquí no hace nada, pero puedes agregar funcionalidad)
        guardarButton.setOnClickListener(v -> {
            // Acción para guardar la patente, por ahora solo muestra un mensaje con la patente ingresada
            String patente = patenteEditText.getText().toString();
            double latitud = Double.parseDouble(latitudText.getText().toString());
            double longitud = Double.parseDouble(longitudText.getText().toString());
            /* currentTimeStamp se tiene que enviar tambien !!! es la hora en que se realiza el registro */
            /* this.photoName se tiene que enviar tambien !!! es el nombre del archivo que tiene la foto */
            insertarRegistro(patente, latitud, longitud, timeInMillis, this.photoName);

            Intent intent = new Intent();
            //intent.putExtra("PATENTE", patente);
            setResult(RESULT_OK, intent);
            finish();  // Finaliza la actividad y retorna el resultado
        });
    }

    // Método para cargar la imagen si se tienen los permisos necesarios
    private void cargarImagen() {
        String imagePath = Environment.getExternalStorageDirectory().getPath() + "/Pictures/Registros_Estacionamiento/" + this.photoName;
        Log.d("ARCHIVO", imagePath);
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            Uri uri = Uri.fromFile(imgFile);
            imageView.setImageURI(uri);  // Establece la imagen en el ImageView
        } else {
            patenteEditText.setText("Imagen no encontrada.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, carga la imagen
                cargarImagen();
            } else {
                // Permiso denegado
                patenteEditText.setText("Permiso denegado para leer el almacenamiento.");
            }
        }
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

    public byte[] convertImageToBytes(String imagePath) {
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
