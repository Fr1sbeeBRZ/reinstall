package com.example.a1a22ikeraadefinitiva;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1a22ikeraadefinitiva.database.CRUDoperator;


public class Registrar extends AppCompatActivity {
    private EditText etNombre;
    private EditText etDescripcion;
    private Button bttnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar);
        setUp();

        bttnAdd.setOnClickListener(view -> {
            String nombre = etNombre.getText().toString().trim();
            String description = etDescripcion.getText().toString().trim();
            Persona persona;

            if (nombre.isEmpty() || description.isEmpty()) Toast.makeText(Registrar.this, R.string.toastRegistrar, Toast.LENGTH_SHORT).show();
            else{
                persona = new Persona(nombre , description);
                CRUDoperator operator = new CRUDoperator(Registrar.this);

                if(operator.insertarPersona(persona) > 0){
                    Toast.makeText(this, R.string.toastPersonaRegistrada, Toast.LENGTH_SHORT).show();
                    Log.i("REGISTRAR PERSONAS" , "Persona registrada");
                } else{
                    Toast.makeText(this, R.string.toastErrorRegistrando , Toast.LENGTH_SHORT).show();
                    Log.e("ERROR REGISTRANDO" , "No se a podido registrar a la persona");
                }
                finish();
            }
        });
    }

    private void setUp() {
        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        bttnAdd = findViewById(R.id.bttnAdd);
    }
}
