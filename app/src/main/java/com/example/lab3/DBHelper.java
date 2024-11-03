package com.example.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "students.db";
    public static final int DATABASE_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableForVersion(db, DATABASE_VERSION);
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Удаляем таблицу, если версия изменилась
        db.execSQL("DROP TABLE IF EXISTS classmates;");
        createTableForVersion(db, newVersion);
        insertInitialData(db);
    }

    private void createTableForVersion(SQLiteDatabase db, int version) {
        if (version == 3) {
            db.execSQL("CREATE TABLE IF NOT EXISTS classmates (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "FIO TEXT, " +
                    "added_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");");
        } else if (version == 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS classmates (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "LastName TEXT, " +
                    "FirstName TEXT, " +
                    "MiddleName TEXT, " +
                    "added_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");");
        }
    }

    private void insertInitialData(SQLiteDatabase db) {
        for (int i = 1; i <= 5; i++) {
            ContentValues contentValues = new ContentValues();
            if (DATABASE_VERSION == 3) {
                contentValues.put("FIO", "Одногруппник " + i);
            } else if (DATABASE_VERSION == 2) {
                contentValues.put("LastName", "Фамилия" + i);
                contentValues.put("FirstName", "Имя" + i);
                contentValues.put("MiddleName", "Отчество" + i);
            }
            db.insert("classmates", null, contentValues);
        }
    }
}
