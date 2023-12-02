package com.cs407.icebox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import android.os.AsyncTask;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;


import java.util.ArrayList;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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


        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataDisplay);
        ListView listView = (ListView) findViewById(R.id.itemDisplay);
        listView.setAdapter(adapter);

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
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                new GetProductNameTask().execute(intentResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class GetProductNameTask extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... barcodes) {
            Request request = new Request.Builder()
                    .url("https://world.openfoodfacts.org/api/v0/product/" + barcodes[0] + ".json")
                    .build();
            try (Response response = client.newCall(request).execute()) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                JSONObject product = jsonResponse.getJSONObject("product");
                return product.getString("product_name");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String productName) {
            super.onPostExecute(productName);
            if (productName != null) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra("ITEM_NAME", productName);
                startActivity(intent);
            }
        }
    }

}