package com.example.kimsunggon.everydaylifelogger;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ResourceType")
public class MainActivity extends AppCompatActivity {

    TextView logView;
    TextView text_date;
    TextView text_time;
    EditText text_content;
    Button bt_googlemap;
    Button bt_save;

    private GoogleMap map;

    ListView mList;
    ArrayList<String> nameList;
    ArrayAdapter<String> baseAdapter;

    Intent intent;
    SQLiteDatabase db;
    String dbName = "dbList.db";
    String tableName = "dbListTable";
    int dbMode = Context.MODE_PRIVATE;

    double lat;
    double lng;

    static LatLng whereAmI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Main", "onCreate");

        // Acquire a reference to the system Location Manager
        logView = (TextView) findViewById(R.id.log);
        logView.setText("GPS가 잡혀야 좌표가 구해짐");
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("Main", "isGPSEnabled=" + isGPSEnabled);
        Log.d("Main", "isNetworkEnabled=" + isNetworkEnabled);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                logView.setText("longtitude=" + lng + ", latitude=" + lat);
            }


            public void onStatusChanged(String provider, int status, Bundle extras) {
                logView.setText("longtitude=" + lng + ", latitude=" + lat);
            }

            public void onProviderEnabled(String provider) {
                logView.setText("longtitude=" + lng + ", latitude=" + lat);
            }

            public void onProviderDisabled(String provider) {
                logView.setText("longtitude=" + lng + ", latitude=" + lat);
            }
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
            logView.setText("longtitude=" + lng + ", latitude=" + lat);
        }

        text_content = (EditText) findViewById(R.id.text_content);
        text_time = (TextView) findViewById(R.id.text_time);
        text_date = (TextView) findViewById(R.id.text_date);
        bt_googlemap = (Button) findViewById(R.id.bt_googlemap);
        bt_save = (Button) findViewById(R.id.bt_save);

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmpDate = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
                String tmpTime = new java.text.SimpleDateFormat("HHmmss").format(new java.util.Date());
                String tmpContent = text_content.getText().toString();
                double tmpLongitude = lng;
                double tmpLatitude = lat;

                insertData(tmpContent, tmpDate, tmpTime, lat, lng);
            }
        });

        whereAmI = new LatLng(lat,lng);

        intent = new Intent(this, NextActivity.class);
        intent.putExtra("place",whereAmI);

        bt_googlemap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        mList = (ListView) findViewById(R.id.listView);
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);
    }

    public int checkSelfPermission(String accessFineLocation) {
        return 0xa;
    }

    public void createTable() {
        try {
            String sql = "create table " + tableName + "(content, " +
                    "date," + "time," + "lat," + "lng)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }

    public void insertData(String content, String date, String time, double lat, double lng) {
        String sql = "insert into " + tableName + " values(NULL, '" + content +
                date + time + lat + lng + "');";
        db.execSQL(sql);
    }
}