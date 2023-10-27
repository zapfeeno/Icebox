package com.cs407.icebox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


    }

    public void addToIcebox(View view) {
        EditText itemName = (EditText) findViewById(R.id.itemNameInput);
        EditText date = (EditText) findViewById(R.id.expirationDateInput);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}