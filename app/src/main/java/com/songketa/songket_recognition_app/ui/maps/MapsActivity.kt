package com.songketa.songket_recognition_app.ui.maps

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var placesClient: PlacesClient
    val keyword = "toko kain"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyAUd7wfyNrYmiwFcPu_oi1bNrEalQg7q98")
        }

        placesClient = Places.createClient(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.show()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (isGooglePlayServicesAvailable()) {
            // Rest of your code
            mMap.uiSettings.run {
                isZoomControlsEnabled = true
                isIndoorLevelPickerEnabled = true
                isCompassEnabled = true
                isMapToolbarEnabled = true
            }
            getMyLocation()
            searchPlaces(keyword)
        } else {
            showToast("lalalalalal")
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googlePlayServices = GoogleApiAvailability.getInstance()
        val resultCode = googlePlayServices.isGooglePlayServicesAvailable(this)
        return resultCode == ConnectionResult.SUCCESS
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }

            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }

            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }

            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun searchPlaces(keyword: String) {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("ID")
            .setTypeFilter(TypeFilter.ESTABLISHMENT)
            .setQuery(keyword)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            for (prediction in response.autocompletePredictions) {
                val placeId = prediction.placeId
                val placeFields = fields

                val fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build()

                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener { placeResponse ->
                    val place = placeResponse.place
                    addMarker(place)
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                    Log.e("Places", "Failed to fetch place details: ${exception.message}")
                }
            }
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            Log.e("Places", "Failed to fetch autocomplete predictions: ${exception.message}")
        }
    }
    private fun addMarker(place: Place) {
        val latLng = place.latLng
        latLng?.let {
            val marker = MarkerOptions().position(it).title(place.name)
            mMap.addMarker(marker)
        }
    }
}