package com.example.obleista_app.frontend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.obleista_app.R;

import java.io.File;

public class CrearRegistroAgenteTransitoActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText patenteEditText;
    private Button guardarButton;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private String photoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_registro_agente_transito);

        // Obtener el nombre de la imagen pasada por el intent
        this.photoName = getIntent().getStringExtra("photoName");

        // Vincula los elementos de la UI
        imageView = findViewById(R.id.imageView);
        patenteEditText = findViewById(R.id.ingresarPatente);
        guardarButton = findViewById(R.id.buttonGuardar);

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
            Intent intent = new Intent();
            intent.putExtra("PATENTE", patente);
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
}
