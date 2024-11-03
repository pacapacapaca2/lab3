package com.example.lab3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        Button btnShowRecords = findViewById(R.id.button2);
        Button btnAddRecord = findViewById(R.id.button);
        Button btnUpdateLastRecord = findViewById(R.id.button3);

        btnShowRecords.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutStudents.class);
            startActivity(intent);
        });

        btnAddRecord.setOnClickListener(v -> {
            ContentValues contentValues = new ContentValues();
            if (DBHelper.DATABASE_VERSION == 3) {
                contentValues.put("FIO", "Новый Одногруппник");
            } else if (DBHelper.DATABASE_VERSION == 2) {
                contentValues.put("LastName", "Новый");
                contentValues.put("FirstName", "Одногруппник");
                contentValues.put("MiddleName", "Маzафакеr");
            }
            db.insert("classmates", null, contentValues);
        });

        btnUpdateLastRecord.setOnClickListener(v -> updateLastRecord());
    }

    private void updateLastRecord() {
        Cursor cursor = db.rawQuery("SELECT * FROM classmates ORDER BY ID DESC LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("ID");

            // Проверка, что индекс столбца "ID" найден
            if (idIndex >= 0) {
                int id = cursor.getInt(idIndex);
                ContentValues contentValues = new ContentValues();

                if (DBHelper.DATABASE_VERSION == 3) {
                    contentValues.put("FIO", "Иванов Иван Иванович");
                } else if (DBHelper.DATABASE_VERSION == 2) {
                    contentValues.put("LastName", "Иванов");
                    contentValues.put("FirstName", "Иван");
                    contentValues.put("MiddleName", "Иванович");
                }
                db.update("classmates", contentValues, "ID = ?", new String[]{String.valueOf(id)});
            } else {
                // Обработка ситуации, когда столбец "ID" не найден
                Log.e("MainActivity", "Column 'ID' not found in query result.");
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
