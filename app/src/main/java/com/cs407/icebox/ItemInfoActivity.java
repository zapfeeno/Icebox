package com.cs407.icebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

// Item information page when you click into certain item
public class ItemInfoActivity extends AppCompatActivity {

    String itemName;
    String purchaseDate;
    String expDate;
    TextView name;
    TextView datePurchased;
    TextView dateExpired;
    String id;
    String displayName;

    private int itemId = -1;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);


        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("storedItems", Context.MODE_PRIVATE, null);
        dbHelper = new DBHelper(sqLiteDatabase);

        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", -1);

        name = (TextView) findViewById(R.id.title);
        datePurchased = (TextView) findViewById(R.id.dateAddedText);
        dateExpired = (TextView) findViewById(R.id.dateExpireText);

        if(itemId != -1) {
            Item item = MainActivity.dataList.get(itemId);

            itemName = item.getItemName();
            purchaseDate = item.getPurchaseDate();
            id = item.getId();


            if(itemName.length() > 15) {
                displayName = itemName.substring(0, 12) + "...";
                name.setText(displayName);
            } else {
                name.setText(itemName);
            }


            datePurchased.setText("Date added: " + item.getPurchaseDate());
            dateExpired.setText("Expiry date: " + item.getExpDate());
        }



    }

    public void deleteItem(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        // remove item from SQLite database
        Log.i("ItemInfoActivity", name.getText().toString() + ", " + datePurchased.getText().toString());

        dbHelper.removeItem(id, itemName);

        startActivity(intent);
    }

    public void returnToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}