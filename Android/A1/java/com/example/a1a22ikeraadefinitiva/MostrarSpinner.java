package com.example.a1a22ikeraadefinitiva;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1a22ikeraadefinitiva.database.CRUDoperator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MostrarSpinner extends AppCompatActivity {
    private Tools tools;
    private TextView tvNombre;
    private TextView tvDescription;
    private Spinner spPersonas;

    private Button bttnGuardarSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_listview);
        setUp();

        bttnGuardarSpinner.setOnClickListener(view -> {
            String nombre = tvNombre.getText().toString().trim();
            String description = tvDescription.getText().toString().trim();
            Persona persona;
            File file;

            if (nombre.isEmpty() || description.isEmpty())
                Toast.makeText(MostrarSpinner.this, R.string.toastGuardar, Toast.LENGTH_SHORT).show();
            else{
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MostrarSpinner.this);
                String customPath = preferences.getString("rutaPersona" , "DATOS");
                persona = new Persona(nombre , description);
                String pathFile = tools.formatPath(getExternalFilesDir(null).toString() , customPath);
                file = new File(pathFile);

                if(!file.exists()) file.mkdirs();
                file = new File(pathFile + "/" + nombre);

                try {
                    FileWriter writer = new FileWriter(file);
                    writer.write(persona.toString());
                    writer.close();
                } catch (IOException e) {throw new RuntimeException(e);}
                Log.i("PERSONA EXPORTADA" , "Fichero guardado en --> " + file.getAbsolutePath());
            }
        });
    }

    private void setUp() {
        tvNombre = findViewById(R.id.tvNombreSpinner);
        tvDescription = findViewById(R.id.tvDescripcionSpinner);
        spPersonas = (Spinner) findViewById(R.id.spPersonas);
        bttnGuardarSpinner = findViewById(R.id.bttnGuardarSpinner);
        tools = new Tools();

        definirAdaptador();
    }

    private void definirAdaptador() {
        CRUDoperator operator = new CRUDoperator(MostrarSpinner.this);
        ArrayList<Persona> personas = operator.getPersonas();


        String[] nombres = new String[personas.size()];

        for(int i = 0 ; i < nombres.length ; i++) nombres[i] = personas.get(i).getNombre();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombres);
        spPersonas.setAdapter(adapter);
    }


}
