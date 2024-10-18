package com.example.obleista_app.frontend;

import android.content.Intent;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obleista_app.R;
import com.example.obleista_app.backend.httpServices.ActualizacionService;
import com.example.obleista_app.backend.httpServices.SubirRegistros;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermiso();

        renderTitulo();

        Button button = findViewById(R.id.buttonModoObleista);
        Button buttonVenderEstacionamiento = findViewById(R.id.buttonVenderEstacionamiento);
        Button buttonSubirRegistros = findViewById(R.id.buttonSubirRegistros);
        Button buttonActualizar = findViewById(R.id.buttonActualziar);

        /* Iniciar modo obleista */
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ModoObleistaActivity.class);
            startActivity(intent);
        });

        /* Enviar los registros de agente de transito y registros de estacionamiento de personas sin la aplicacion */
        buttonSubirRegistros.setOnClickListener( v -> {
            SubirRegistros subirRegistros = new SubirRegistros(this);
            subirRegistros.enviarRegistrosASistemaCentralConFotos();
            subirRegistros.enviarRegistrosConductorSinApp();
        });

        /* Iniciar modo venta de estacionamiento a personas que no tienen la apicacion */
        buttonVenderEstacionamiento.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VenderEstacionamientoActivity.class);
            startActivity(intent);
        });

        /* Actualizar datos (poligonos, horarios y patrones de patentes) */
        buttonActualizar.setOnClickListener(v -> {
            ActualizacionService actualizacionService = new ActualizacionService(this);
            actualizacionService.actualizarPoligonosDeEstacionamiento();
            actualizacionService.actualizarPatronesDePatentes();
            actualizacionService.actualizarHorariosDeEstacionamiento();

            new DataBaseLogPrinter(this);
        });
    }

    public void renderTitulo() {
        TextView textViewTitle = findViewById(R.id.textView3321);
        String text = "EstacionAR obleistas";
        SpannableString spannableString = new SpannableString(text);

        // Obtener el color cyan de los recursos
        int cyanColor = getResources().getColor(R.color.cyan, getTheme());

        // Cambiar el color de "AR" a cyan
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(cyanColor);
        spannableString.setSpan(colorSpan, 8, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewTitle.setText(spannableString);
    }

    private void solicitarPermiso() {

        // IMPLEMENTAR

    }

}