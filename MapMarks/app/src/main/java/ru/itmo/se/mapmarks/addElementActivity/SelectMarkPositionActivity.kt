package ru.itmo.se.mapmarks.addElementActivity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_select_mark_position.*
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.map.MapWithCurrentLocation

class SelectMarkPositionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: MapWithCurrentLocation
    private var selectedPositionForMark: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_mark_position)

        (markLocationMap as SupportMapFragment).getMapAsync(this)
        markLocationSelectedButton.setOnClickListener {
            if (selectedPositionForMark != null) {
                setResult(Activity.RESULT_OK,
                    Intent()
                        .putExtra("name", intent.getStringExtra("name"))
                        .putExtra("description", intent.getStringExtra("description"))
                        .putExtra("lat", selectedPositionForMark!!.latitude)
                        .putExtra("lon", selectedPositionForMark!!.longitude)
                )
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = true

        val listener = {
            selectedPositionForMark = googleMap.cameraPosition.target
        }

        googleMap.setOnCameraIdleListener(listener)
        googleMap.setOnCameraMoveListener(listener)

        map = MapWithCurrentLocation(googleMap, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        map.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
