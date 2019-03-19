package ru.itmo.se.mapmarks.addElementActivity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_select_mark_position.*
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.map.MapWithCurrentLocation
import ru.itmo.se.mapmarks.moveToMark

class SelectMarkPositionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: MapWithCurrentLocation
    private var selectedPositionForMark: LatLng? = null
    private val points = ArrayList<LatLng>()
    private val polygon = PolygonOptions().strokeColor(Color.BLACK).fillColor(Color.YELLOW)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_mark_position)

        (markLocationMap as SupportMapFragment).getMapAsync(this)

        addPointButton.setOnClickListener {
            if (selectedPositionForMark != null) {
                points.add(selectedPositionForMark!!)
                polygon.add(selectedPositionForMark)
                map.backedMap.clear()
                if (points.size == 1) {
                    map.backedMap.addMarker(MarkerOptions().position(selectedPositionForMark!!))
                }
                map.backedMap.addPolygon(polygon)
            }
        }

        markLocationSelectedButton.setOnClickListener {
            if (selectedPositionForMark != null) {
                if (points.size == 0) {
                    points.add(selectedPositionForMark!!)
                }
                setResult(Activity.RESULT_OK,
                    Intent()
                        .putExtra("name", intent.getStringExtra("name"))
                        .putExtra("description", intent.getStringExtra("description"))
                        .putExtra("coordinates", points)
                )
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val listener = {
            selectedPositionForMark = googleMap.cameraPosition.target
        }

        googleMap.setOnCameraIdleListener(listener)
        googleMap.setOnCameraMoveListener(listener)

        map = MapWithCurrentLocation(googleMap, this)
        if (intent.hasExtra("coordinates")) {
            val coordinates = intent.getDoubleArrayExtra("coordinates")
            moveToMark(map.backedMap, LatLng(coordinates[0], coordinates[1]))
        } else {
            map.moveCameraToCurrentPosition()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        map.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
