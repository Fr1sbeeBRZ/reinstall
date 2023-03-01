package com.example.a1a22ikeraadefinitiva;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1a22ikeraadefinitiva.database.CRUDoperator;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MostrarListview extends AppCompatActivity {
    private Tools tools;
    private ArrayList<Persona> personas;
    private TextView tvNombre;
    private TextView tvDescription;
    private ListView lvPersonas;
    private Button bttnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_listview);
        setUp();

        bttnGuardar.setOnClickListener(view -> {
            String nombre = tvNombre.getText().toString().trim();
            String description = tvDescription.getText().toString().trim();
            Persona persona;
            File file;

            if (nombre.isEmpty() || description.isEmpty())
                Toast.makeText(MostrarListview.this, R.string.toastGuardar, Toast.LENGTH_SHORT).show();
            else{
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MostrarListview.this);
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
        tvNombre = findViewById(R.id.tvNombre);
        tvDescription = findViewById(R.id.tvDescripcion);
        lvPersonas = findViewById(R.id.lvPersonas);
        bttnGuardar = findViewById(R.id.bttnGuardarListView);
        tools = new Tools();
        adaptadorListview();

        lvPersonas.setOnItemClickListener((adapterView, view, position, l) -> {
            Persona persona = personas.get(position);
            tvNombre.setText(persona.getNombre());
            tvDescription.setText(persona.getDescripcion());
        });

        bttnGuardar.setOnClickListener(view -> {
            String nombre = (String) tvNombre.getText();
            String description = (String) tvDescription.getText();

            if(nombre.isEmpty() || description.isEmpty())Toast.makeText(MostrarListview.this, R.string.toastGuardar, Toast.LENGTH_SHORT).show();
            else {
                SharedPreferences preferences = getSharedPreferences("preferences" , MODE_PRIVATE);
                String ruta = preferences.getString("rutaPersona" , "DATOS");
                String pathFile = tools.formatPath(getExternalFilesDir(null).getAbsolutePath() , ruta);
                File file = new File(pathFile);

                if(!file.exists()) file.mkdirs();
                file = new File(pathFile + nombre + ".txt");

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(nombre + " - " + description);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {throw new RuntimeException(e);}
                Log.i("PERSONA EXPORTADA" , "Fichero guardado en --> " + file.getAbsolutePath());
            }

        });

    }

    private void adaptadorListview() {
        CRUDoperator operator = new CRUDoperator(MostrarListview.this);
        personas = operator.getPersonas();

        if (personas.isEmpty()) return;

        String[] nombres = new String[personas.size()];

        for(int i = 0 ; i < nombres.length ; i++) nombres[i] = personas.get(i).getNombre();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MostrarListview.this, android.R.layout.simple_list_item_1, nombres);
        lvPersonas.setAdapter(adapter);
    }


}
