package com.cs407.icebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.EditText;
import java.util.Random;


public class AddItemActivity extends AppCompatActivity {


    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("storedItems", Context.MODE_PRIVATE, null);
        dbHelper = new DBHelper(sqLiteDatabase);

    }

    public void addToIcebox(View view) {
        EditText itemName = (EditText) findViewById(R.id.itemNameInput);
        EditText date = (EditText) findViewById(R.id.expirationDateInput);
        Random random = new Random();
        String id = String.valueOf(1 + random.nextInt(1000000000));
        dbHelper.addItem(id, itemName.getText().toString(), "placeholderDate", date.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}