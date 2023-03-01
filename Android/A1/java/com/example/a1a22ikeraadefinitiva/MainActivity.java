package com.example.a1a22ikeraadefinitiva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a1a22ikeraadefinitiva.database.DbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private Toolbar appToolbar;
    private Button bttnAlta;
    private Button bttnMostrar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings){
            Intent intent = new Intent(MainActivity.this , Preferences.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp();

        cargarDatabase().show();

        bttnAlta.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this , Registrar.class);
            startActivity(intent);
        });

        bttnMostrar.setOnClickListener(view -> seleccionarMostrar().show());
    }

    private Dialog cargarDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle(R.string.tituloCargarDB);
        builder.setMessage(R.string.mensajeCargarDB);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton(R.string.Assets, (dialogInterface, i) -> copiarDB());
        builder.setNegativeButton(R.string.Code, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setCancelable(false);

        builder.create();
    }

    private void copiarDB() {
        String bddestino = "/data/data/" + getPackageName() + "/databases/" + DbHelper.NAME;
        File file = new File(bddestino);

        if(file.exists()){
            Toast.makeText(this, R.string.AssetsExiste, Toast.LENGTH_SHORT).show();
            return;
        }

        String pathbd = "/data/data/" + getPackageName() + "/databases/";
        File filepathdb = new File(pathbd);
        filepathdb.mkdirs();

        InputStream inputstream;
        try {
            inputstream = getAssets().open(DbHelper.NAME);
            OutputStream outputstream = new FileOutputStream(bddestino);

            int tamread;
            byte[] buffer = new byte[2048];

            while ((tamread = inputstream.read(buffer)) > 0) {
                outputstream.write(buffer, 0, tamread);
            }

            inputstream.close();
            outputstream.flush();
            outputstream.close();
            Toast.makeText(getApplicationContext(), R.string.toast_bd_copiada, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Dialog seleccionarMostrar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.tituloMostrar)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(R.string.mensajeMostrar)
                .setNegativeButton(R.string.listviewMostrar, (dialogInterface, i) -> {
                    Intent intent = new Intent(MainActivity.this , MostrarListview.class);
                    startActivity(intent);
                })
                .setPositiveButton(R.string.spinnerMostrar, (dialogInterface, i) -> {
                    Intent intent = new Intent(MainActivity.this , MostrarSpinner.class);
                    startActivity(intent);
                });

        return builder.create();
    }

    private void setUp() {
        appToolbar = findViewById(R.id.appToolbar);
        bttnAlta = findViewById(R.id.bttnAlta);
        bttnMostrar = findViewById(R.id.bttnMostrar);
        setSupportActionBar(appToolbar);
        appToolbar.inflateMenu(R.menu.menu);

    }
}