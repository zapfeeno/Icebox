package com.cs407.icebox;

import static com.cs407.icebox.NotificationHelper.ALARM_TYPE_RTC;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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

import android.graphics.Color;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Item> dataList = new ArrayList<>();
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(!isGranted){
                    Toast.makeText(this, "Please allow all notifications to continue", Toast.LENGTH_LONG).show();
                }
            });
    static PendingIntent alarmIntentRTC;
    static AlarmManager alarmManagerRTC;

    private void requestPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            return;
        }
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();
        //NotificationHelper.scheduleRepeatingRTCNotification(this, "12", "0");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createNotificationChannel(this);
        }
        scheduleRepeatingRTCNotification(this, "11", "59");

        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("storedItems", Context.MODE_PRIVATE, null);
        DBHelper dbHelper = new DBHelper(sqLiteDatabase);


        dataList = dbHelper.readItems();

        ArrayList<String> dataDisplay = new ArrayList<>();


        for(Item item: dataList) {
            dataDisplay.add(String.format("%s", item.getItemName()));
        }

        // Get color list
        List<Integer> colorList = createColorList();


        ColorAdaptor adapter = new ColorAdaptor(this, dataList, colorList);
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
            intentIntegrator.setPrompt("Scan an item barcode");
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

    public List<Integer> createColorList() {

        // Current Threshold:
        // <=1 days red
        // <=4 days yellow
        // else green

        ArrayList<Integer> colorList = new ArrayList<Integer> ();

        for(Item item : dataList) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date today = new Date();

            String expirationDate = item.getExpDate();

            try{
                Date expDate = dateFormat.parse(expirationDate);

                // Compare date of today with expDate
                long milliDifference = Math.abs(expDate.getTime()-today.getTime());
                long dateDifference = TimeUnit.DAYS.convert(milliDifference, TimeUnit.MILLISECONDS);
                Log.d("bug","" + dateDifference);

                // full expire
                if(dateDifference<=0) {
                    colorList.add(Color.rgb(181, 137, 83));
                }
                // one day
                else if(dateDifference<=1) {
                    colorList.add(Color.rgb(255, 92, 100));
                }
                // 4 days
                else if(dateDifference<=4) {
                    colorList.add(Color.rgb(255,247,156));
                }
                // not soon
                else {
                    colorList.add(Color.rgb(100,202,152));
                }
            } catch (ParseException e) {
                // Do nothing bc there will be no exceptions
            }
        }

        return colorList;
    }

    public static void scheduleRepeatingRTCNotification(Context context, String hour, String min) {
        //get calendar instance to be able to select what time notification should be scheduled
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //Setting time of the day (8am here) when notification will be sent every day (default)
        calendar.set(Calendar.HOUR_OF_DAY,
                Integer.getInteger(hour, 11),
                Integer.getInteger(min, 59));

        //Setting intent to class where Alarm broadcast message will be handled
        Intent intent = new Intent(context, AlarmReceiver.class);
        //Setting alarm pending intent
        alarmIntentRTC = PendingIntent.getBroadcast(context, ALARM_TYPE_RTC, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        //getting instance of AlarmManager service
        alarmManagerRTC = (AlarmManager)context.getSystemService(ALARM_SERVICE);

        //Setting alarm to wake up device every day for clock time.
        //AlarmManager.RTC_WAKEUP is responsible to wake up device for sure, which may not be good practice all the time.
        // Use this when you know what you're doing.
        //Use RTC when you don't need to wake up device, but want to deliver the notification whenever device is woke-up
        //We'll be using RTC.WAKEUP for demo purpose only
        alarmManagerRTC.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntentRTC);
    }

}