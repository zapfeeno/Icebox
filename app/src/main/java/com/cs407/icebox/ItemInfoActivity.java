package com.cs407.icebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ItemInfoActivity extends AppCompatActivity {

    String itemName;
    String purchaseDate;
    String expDate;
    TextView name;
    TextView datePurchased;
    TextView dateExpired;

    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);


        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("storedItems", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", -1);

        name = (TextView) findViewById(R.id.title);
        datePurchased = (TextView) findViewById(R.id.dateAddedText);
        dateExpired = (TextView) findViewById(R.id.dateExpireText);

        if(itemId != -1) {
            boxItems item = MainActivity.dataList.get(itemId);

            name.setText(item.getItemName());
            datePurchased.setText("Date added: " + item.getPurchaseDate());
            dateExpired.setText("Expiry date: " + item.getExpDate());
        }



    }

    public void deleteItem(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        // remove item from SQLite database
        startActivity(intent);
    }

    public void returnToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}