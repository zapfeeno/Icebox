package com.cs407.icebox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper {

    static SQLiteDatabase sqLiteDatabase;

    public DBHelper (SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public static void createTable() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS storedItems" + "(id INTEGER PRIMARY KEY, itemId TEXT, itemName TEXT, purchaseDate TEXT, expDate TEXT)");
    }



    public ArrayList<Item> readItems() {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM storedItems", (new String[]{}));
        int idIndex = c.getColumnIndex("id");
        int itemIndex = c.getColumnIndex("itemName");
        int purchaseIndex = c.getColumnIndex("purchaseDate");
        int expIndex = c.getColumnIndex("expDate");
        c.moveToFirst();
        ArrayList<Item> itemList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String itemName = c.getString(itemIndex);
            String purchaseDate = c.getString(purchaseIndex);
            String expDate = c.getString(expIndex);
            String id = c.getString(idIndex);
            Item item = new Item(itemName, purchaseDate, expDate, id);
            itemList.add(item);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();
        return itemList;
    }


    public void addItem(String id, String itemName, String purchaseDate, String expDate) {
        createTable();
        sqLiteDatabase.execSQL("INSERT INTO storedItems (id, itemName, purchaseDate, expDate) VALUES (?, ?, ?, ?)",
                new String[]{id, itemName, purchaseDate, expDate});
    }



    public void removeItem(String id, String iName) {
        createTable();
        sqLiteDatabase.execSQL("DELETE FROM storedItems WHERE id = ? AND itemName = ?",
                new String[]{id, iName});
    }
}