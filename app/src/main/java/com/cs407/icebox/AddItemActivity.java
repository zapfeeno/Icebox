package com.cs407.icebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

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

        // Get from input fields
        EditText itemName = (EditText) findViewById(R.id.itemNameInput);
        EditText date = (EditText) findViewById(R.id.expirationDateInput);

        // Get purchase date
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        String purchaseDate = dateFormat.format(new Date());

        //TODO: Generate random ID for item to be added to the database
        UUID randomId = UUID.randomUUID();
        String itemId = randomId.toString();

        dbHelper.addItem(itemId, itemName.getText().toString(), purchaseDate, date.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}