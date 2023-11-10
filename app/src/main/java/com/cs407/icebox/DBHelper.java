package com.cs407.icebox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {

    static SQLiteDatabase sqLiteDatabase;

    public DBHelper (SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public static void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS storedItems" +
                "(id INTEGER PRIMARY KEY, itemId INTEGER, itemName TEXT, purchaseDate TEXT, expDate TEXT)");
    }

    public ArrayList<boxItems> readItems() {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM storedItems", (new String[]{}));
        int itemIndex = c.getColumnIndex("itemName");
        int purchaseIndex = c.getColumnIndex("purchaseDate");
        int expIndex = c.getColumnIndex("expDate");
        c.moveToFirst();
        ArrayList<boxItems> itemList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String itemName = c.getString(itemIndex);
            String purchaseDate = c.getString(purchaseIndex);
            String expDate = c.getString(expIndex);

            boxItems item = new boxItems(itemName, purchaseDate, expDate);
            itemList.add(item);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();
        return itemList;
    }

    public void addItem(String itemName, String purchaseDate, String expDate) {
        createTable();
        sqLiteDatabase.execSQL("INSERT INTO storedItems (itemName, purchaseDate, expDate) VALUES (?, ?, ?)",
                new String[]{itemName, purchaseDate, expDate});
    }

/*
    public void deleteItem(String itemName, String purchaseDate) {
        createTable();
        String date = "";
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT date FROM notes WHERE filename = ?",
                new String[]{filename});
        if(cursor.moveToNext()) {
            date = cursor.getString(0);
        }
        sqLiteDatabase.execSQL("DELETE FROM notes WHERE filename = ? AND date = ?",
                new String[]{filename,date});
        cursor.close();
    }
*/

}
