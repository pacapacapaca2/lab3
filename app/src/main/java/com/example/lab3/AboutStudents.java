package com.example.lab3;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AboutStudents extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;
    ListView listView;
    ArrayList<String> studentList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_students);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
        listView = findViewById(R.id.listView);

        studentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        listView.setAdapter(adapter);
        loadRecords();
    }

    private void loadRecords() {
        Cursor cursor = db.rawQuery("SELECT * FROM classmates", null);
        if (cursor.moveToFirst()) {
            do {
                String record;
                if (DBHelper.DATABASE_VERSION == 3) {
                    @SuppressLint("Range") String fio = cursor.getString(cursor.getColumnIndex("FIO"));
                    @SuppressLint("Range") String addedTime = cursor.getString(cursor.getColumnIndex("added_time"));
                    record = fio + " - " + addedTime;
                } else {
                    @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
                    @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex("FirstName"));
                    @SuppressLint("Range") String middleName = cursor.getString(cursor.getColumnIndex("MiddleName"));
                    @SuppressLint("Range") String addedTime = cursor.getString(cursor.getColumnIndex("added_time"));
                    record = lastName + " " + firstName + " " + middleName + " - " + addedTime;
                }
                studentList.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
