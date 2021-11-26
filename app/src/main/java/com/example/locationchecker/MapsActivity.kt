package com.example.locationchecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.locationchecker.databinding.ActivityMapsBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var dbReference: DatabaseReference = database.getReference("test")
    private lateinit var findButton: Button
    private lateinit var binding: ActivityMapsBinding

    companion object {
        private const val TAG = "MapsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findButton = findViewById(R.id.btn_find_location)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        dbReference = Firebase.database.reference
        dbReference.addValueEventListener(locListener)
    }

    val locListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                val location = snapshot.child("test").getValue(LocInfo::class.java)
                val locationLat = location?.latitude
                val locationLong = location?.longitude

                findButton.setOnClickListener {
                    if (locationLat != null && locationLong != null) {
                        val latLng = LatLng(locationLat, locationLong)

                        map.addMarker(MarkerOptions().position(latLng).title("A felhasználó jelenleg itt van!"))
                        val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)

                        map.moveCamera(update)
                    }
                    else {
                        Log.e(TAG, "A felhasználó nem található!")
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}