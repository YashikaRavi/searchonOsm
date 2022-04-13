package com.firstapp.maps

import android.Manifest
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.location.LocationListener
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.OverlayItem
import android.os.Bundle
import android.preference.PreferenceManager
import com.firstapp.maps.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.util.GeoPoint
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.firstapp.maps.databinding.ActivityMainBinding
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import retrofit2.HttpException
import java.io.IOException
import java.util.ArrayList

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private var map: MapView? = null
    var myLocationOverlay: MyLocationNewOverlay? = null
    var anotherOverlayItemArray: ArrayList<OverlayItem>? = null

    private var places: ArrayList<MapDataformat> = arrayListOf()
    private var matchedPlaces: ArrayList<MapDataformat> = arrayListOf()
    private var mapAdapter: MapAdapt = MapAdapt(places)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        map = findViewById(R.id.map)
        map?.setTileSource(TileSourceFactory.MAPNIK)
        map?.getZoomController()?.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
        map?.setMultiTouchControls(true)
        map?.getController()?.setZoom(18.0)
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        myLocationOverlay?.enableMyLocation()
        map?.getOverlays()?.add(myLocationOverlay)
        val center = GeoPoint(13.024105994863957, 80.20838161059535)
        map?.getController()?.animateTo(center)
        addMarker(center)
        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )


        setupRecyclerView()
        performSearch()


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



    private fun setupRecyclerView()=binding.rvTodos.apply {
        mapAdapter=MapAdapt(places).also {
            binding.rvTodos.adapter = it
            binding.rvTodos.adapter!!.notifyDataSetChanged()
        }
        binding.searchView.isSubmitButtonEnabled = true
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
    private fun performSearch() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText)
                return true

            }
        })
    }
    private fun search(text: String?) {
        matchedPlaces = arrayListOf()

        text?.let {
            places.forEach { place ->
                if (place.Country.contains(text, true) || place.Street.contains(text, true) ) {
                    matchedPlaces.add(place)
                }
            }
            updateRecyclerView()
            if (matchedPlaces.isEmpty()) {
                Toast.makeText(this, "No match found!", Toast.LENGTH_SHORT).show()
            }
            updateRecyclerView()
        }
    }
    private fun updateRecyclerView() {
        binding.rvTodos.apply {
            mapAdapter.map = matchedPlaces
            mapAdapter.notifyDataSetChanged()
        }
    }
    fun addMarker(center: GeoPoint?) {
        val marker = Marker(map)
        marker.position = center
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.icon = resources.getDrawable(R.drawable.pic)
        map?.overlays?.clear()
        map?.overlays?.add(marker)
        map?.invalidate()
    }

    public override fun onResume() {
        super.onResume()
        map?.onResume() //needed for compass, my location overlays, v6.0.0 and up
    }

    public override fun onPause() {
        super.onPause()
        map?.onPause() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val permissionsToRequest = ArrayList<String>()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        // ToDo
    }
}