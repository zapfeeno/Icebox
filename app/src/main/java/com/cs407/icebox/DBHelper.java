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
                "(id INTEGER PRIMARY KEY, itemId STRING, itemName TEXT, purchaseDate TEXT, expDate TEXT)");
    }

    public ArrayList<Item> readItems() {
        createTable();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM storedItems", (new String[]{}));
        int itemIndex = c.getColumnIndex("itemName");
        int purchaseIndex = c.getColumnIndex("purchaseDate");
        int expIndex = c.getColumnIndex("expDate");
        c.moveToFirst();
        ArrayList<Item> itemList = new ArrayList<>();
        while (!c.isAfterLast()) {
            String itemName = c.getString(itemIndex);
            String purchaseDate = c.getString(purchaseIndex);
            String expDate = c.getString(expIndex);

            Item item = new Item(itemName, purchaseDate, expDate);
            itemList.add(item);
            c.moveToNext();
        }
        c.close();
        sqLiteDatabase.close();
        return itemList;
    }

    public void addItem(String itemId, String itemName, String purchaseDate, String expDate) {
        createTable();
        sqLiteDatabase.execSQL("INSERT INTO storedItems (itemId, itemName, purchaseDate, expDate) VALUES (?, ?, ?, ?)",
                new String[]{itemId, itemName, purchaseDate, expDate});
    }


    public void deleteItem(String itemId) {
        createTable();
        String purchaseDate = "";
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT purchaseDate FROM storedItems WHERE itemId = ?",
                new String[]{itemId});
        if(cursor.moveToNext()) {
            purchaseDate = cursor.getString(0);
        }
        sqLiteDatabase.execSQL("DELETE FROM notes WHERE itemId = ? AND purchaseDate = ?",
                new String[]{itemId, purchaseDate});
        cursor.close();
    }

}
