package com.example.a1a22ikeraadefinitiva.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String NAME = "DATOS.db";
    public static final String TABLE = "t_persona";


    public DbHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE + "(" +
                "nombre TEXT PRIMARY KEY," +
                "descripcion TEXT NOT NULL" +
                ")";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE);
        onCreate(sqLiteDatabase);
    }
}
