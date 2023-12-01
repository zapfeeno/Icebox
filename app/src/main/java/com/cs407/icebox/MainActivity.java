package com.cs407.icebox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Item> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("storedItems", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);

        // placeholder item, works
        //dbHelper.addItem("Eggs", "11/01/2023", "11/20/2023");

        dataList = dbHelper.readItems();

        ArrayList<String> dataDisplay = new ArrayList<>();

        // populate items from SQLite database?
        //boxItems testItem = new boxItems("Eggs", "11/01/2023", "11/20/2023");

        for(Item item: dataList) {
            dataDisplay.add(String.format("%s", item.getItemName()));
        }

        // Get color list
        List<Integer> colorList = createColorList();


//        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataDisplay);
//        ListView listView = (ListView) findViewById(R.id.itemDisplay);
//        listView.setAdapter(adapter);

        // Creating the list
        ColorAdaptor adaptor = new ColorAdaptor(this, dataList, colorList);
        ListView listView = (ListView) findViewById(R.id.itemDisplay);
        listView.setAdapter(adaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                Intent intent = new Intent(getApplicationContext(), ItemInfoActivity.class);
                intent.putExtra("itemId", i);


                startActivity(intent);
            }
        });
    }

    public void addFoodItemFunction(View view) {

        // Switch activity
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.codeScanner) {
            Toast.makeText(this, "Code Scanner selected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ScannerPage.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<Integer> createColorList() {

        // TODO: Calculate the Color of each item
        // placeholder
        List<Integer> itemColors = Arrays.asList(Color.RED, Color.BLUE);
        return itemColors;
    }
}