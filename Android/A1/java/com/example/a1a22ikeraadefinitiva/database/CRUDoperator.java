package com.example.a1a22ikeraadefinitiva.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.a1a22ikeraadefinitiva.Persona;

import java.util.ArrayList;


public class CRUDoperator extends DbHelper{
    private Context context;
    public CRUDoperator(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarPersona(Persona persona){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre" , persona.getNombre());
        values.put("descripcion" , persona.getDescripcion());

        return database.insert(TABLE , null , values);
    }

    public ArrayList<Persona> getPersonas(){
        ArrayList<Persona> personas = new ArrayList<>();

        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String nombre;
        String descripcion;
        Persona persona;

        Cursor cursor = database.rawQuery("SELECT nombre , descripcion FROM " + TABLE , null);

        if (cursor.moveToFirst()){
            do{
                nombre = cursor.getString(0);
                descripcion = cursor.getString(1);
                persona = new Persona(nombre , descripcion);
                personas.add(persona);
            }while (cursor.moveToNext());
        }
        return personas;
    }
}
