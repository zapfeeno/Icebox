package com.cs407.icebox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Random;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {


    DBHelper dbHelper;
    private String selectedExpirationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("storedItems", Context.MODE_PRIVATE, null);
        dbHelper = new DBHelper(sqLiteDatabase);

    }

    public void setExpiryDate(View view) {
        Button expirationDateButton = findViewById(R.id.expirationDateButton);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddItemActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedExpirationDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                        expirationDateButton.setText(selectedExpirationDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void addToIcebox(View view) {

        // Get from input fields
        EditText itemName = (EditText) findViewById(R.id.itemNameInput);
        Button date = (Button) findViewById(R.id.expirationDateButton);

        // Get purchase date
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        String purchaseDate = dateFormat.format(new Date());

        // Generate random id
        Random random = new Random();
        String id = String.valueOf(1 + random.nextInt(1000000000));
        if (selectedExpirationDate == null || selectedExpirationDate.isEmpty()) {

        } else {
            dbHelper.addItem(id, itemName.getText().toString(), purchaseDate, date.getText().toString());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}