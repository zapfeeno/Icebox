package com.cs407.icebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ItemInfoActivity extends AppCompatActivity {

    String itemName;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        Intent intent = getIntent();
        itemName = intent.getStringExtra("itemName");

        name = (TextView) findViewById(R.id.title);
        name.setText(itemName);
    }

    public void deleteItem(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}