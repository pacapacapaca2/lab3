# Лабораторная работа №3. Работа с базой данных
- _Выполнил:_ Стан Богдан
- _Язык программирования:_ Java

## Что делает приложение?
1. Состоит из:
   - [Меню](#activity1).
   - [Экрана вывода данных из таблицы "Одногруппники"](#activity2).
   - Т.к. происходит взаимодействие с БД, был создан отдельный класс `DBHelper` (файл `DBHelper.java`).
2. Создает базу данных и таблицу "Одногруппники" при запуске, если они не существуют.
3. Удаляет все записи и добавляет 5 новых при первом запуске.
4. Выводит данные из таблицы на отдельный экран при нажатии на кнопку "посмотреть".
5. Добавляет новую запись при нажатии на кнопку "добавить".
6. Обновляет последнюю запись при нажатии на кнопку "заменить".
7. При изменении версии базы данных удаляет таблицу «Одногруппники» и создает таблицу «Одногруппники», содержащую больше полей (ветка: task-2).

---
### <a id="activity1"> Меню </a>

Меню состоит из 3 кнопок:
- **"посмотреть"**. По тапу открывается [Экран вывода данных из таблицы "Одногруппники"](#activity2).
- **"добавить"**. По тапу добавляется новая строка в таблицу "Одногруппники":
  - для ветки `master` и `task-2`

    -  ``` java
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
       ```
- **"заменить".** По тапу последняя запись таблицы изменяется на "Иванов Иван Иванович":
  - ``` java
    btnUpdateLastRecord.setOnClickListener(v -> {
            updateLastRecord();
        });
    ```
   - для ветки `master`
     -  ``` java
        private void updateLastRecord() {
          Cursor cursor = db.rawQuery("SELECT * FROM classmates ORDER BY ID DESC LIMIT 1", null);
          if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
            ContentValues contentValues = new ContentValues();
            contentValues.put("FIO", "Иванов Иван Иванович");
            db.update("classmates", contentValues, "ID = ?", new String[]{String.valueOf(id)});
          }
          cursor.close();
        }
        ```
  - для ветки`task-2`
    - ``` java
      private void updateLastRecord() {
        Cursor cursor = db.rawQuery("SELECT * FROM classmates ORDER BY ID DESC LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("ID"));
            ContentValues contentValues = new ContentValues();
            contentValues.put("LastName", "Иванов");
            contentValues.put("FirstName", "Иван");
            contentValues.put("MiddleName", "Иванович");
            db.update("classmates", contentValues, "ID = ?", new String[]{String.valueOf(id)});
        }
        cursor.close();
      }
      ```
---
### <a id="activity2"> Экран вывода данных из таблицы "Одногруппники" </a>
На этом экране также происходит обращение к БД.

#### Для ветки `master`
Таблица "Одногруппники" состоит из полей:
- ID
- ФИО
- время добавления записи

Для ветки `task-2`
Таблица "Одногруппники" состоит из полей:
- ID
- Фамилия
- Имя
- Отчество
- время добавления записи
- 
На экране выводятся записи в виде списка, для загрузки записей используется следующая функция:
``` java
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
```
---


