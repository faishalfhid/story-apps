package com.dicoding.mystoryapp.ui.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.ViewModelFactory
import com.dicoding.mystoryapp.api.response.ListStoryItem
import com.dicoding.mystoryapp.data.ResultState
import com.dicoding.mystoryapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Observe the data
        mapsViewModel.getStoriesWithLocation().observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    addManyMarker(result.data)
                }

                is ResultState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getMyLocation()
        setMapStyle()


        val indonesiaLocation = LatLng(-2.5489, 118.0149)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaLocation, 4f))

        val indonesiaBorder = listOf(
            LatLng(5.9041, 95.2057),
            LatLng(5.9041, 141.0195),
            LatLng(-10.9417, 141.0195),
            LatLng(-10.9417, 95.2057),
            LatLng(5.9041, 95.2057)
        )

        val polygonOptions = PolygonOptions()
        polygonOptions.addAll(indonesiaBorder)
        polygonOptions.strokeColor(ContextCompat.getColor(this@MapsActivity, R.color.navy))
        polygonOptions.strokeWidth(2f) // Lebar garis batas
        polygonOptions.fillColor(ContextCompat.getColor(this@MapsActivity, android.R.color.transparent))

        mMap.addPolygon(polygonOptions)
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
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

    private fun addManyMarker(data: List<ListStoryItem>) {
        data.forEach { story ->
            val latLng = story.lat?.let { story.lon?.let { it1 -> LatLng(it, it1) } }
            latLng?.let {
                MarkerOptions()
                    .position(it)
                    .title(story.name)
                    .snippet(story.description)
            }?.let {
                mMap.addMarker(
                    it
                )
            }
            latLng?.let { boundsBuilder.include(it) }
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}
