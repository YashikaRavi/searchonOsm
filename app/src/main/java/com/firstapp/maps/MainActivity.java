package com.firstapp.maps;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.firstapp.maps.databinding.ActivityMainBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocationListener {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    MyLocationNewOverlay myLocationOverlay;
    ArrayList<OverlayItem> anotherOverlayItemArray;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        map.getController().setZoom(18D);


        this.myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),map);
        this.myLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.myLocationOverlay);

        GeoPoint center = new GeoPoint(13.024105994863957, 80.20838161059535);
        map.getController().animateTo(center);
        addMarker(center);

        requestPermissionsIfNecessary(new String[]{

                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
//        anotherOverlayItemArray = new ArrayList<OverlayItem>();
//        anotherOverlayItemArray.add(new OverlayItem(
//                "0, 0", "0, 0", new GeoPoint(0, 0)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Chennai", "Chennai", new GeoPoint(13.08268, 80.27072)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Coimbatore", "Coimbatore", new GeoPoint(11.004556, 76.961632)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Chengalpatu", "Chengalpatu", new GeoPoint(12.693933, 79.975662)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Salem", "Salem", new GeoPoint(11.664325, 78.146011)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Tamilnadu", "Tamilnadu", new GeoPoint(11.059821, 78.387451)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Kaniyakumari", "Kaniyakumari", new GeoPoint(8.088306, 77.538452)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Trichy", "Trichy", new GeoPoint(10.790483, 78.704674)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Tripura", "Tripura", new GeoPoint(9.939093, 78.121719)));
//        anotherOverlayItemArray.add(new OverlayItem(
//                "Nagapattinam", "Nagapattinam ", new GeoPoint(10.76561, 79.84239)));
//
//        ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay
//                = new ItemizedIconOverlay<OverlayItem>(
//                this, anotherOverlayItemArray, null);
//        map.getOverlays().add(anotherItemizedIconOverlay);
    }
    public void addMarker (GeoPoint center){
        Marker marker = new Marker(map);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.pic));
        map.getOverlays().clear();
        map.getOverlays().add(marker);
        map.invalidate();
        marker.setTitle("Sua Localização");
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // ToDo
    }

}