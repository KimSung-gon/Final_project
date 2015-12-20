package com.example.kimsunggon.everydaylifelogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by KimSunggon on 2015-12-20.
 */
public class NextActivity extends AppCompatActivity {

    LatLng whereAmI;
    GoogleMap map;

    Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        intent1 = this.getIntent();
        whereAmI = intent1.getParcelableExtra("name");
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        Marker seoul = map.addMarker(new MarkerOptions().position(whereAmI).title("where"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(whereAmI, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }
}
