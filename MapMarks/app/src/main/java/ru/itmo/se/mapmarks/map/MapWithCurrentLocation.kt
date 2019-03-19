package ru.itmo.se.mapmarks.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import ru.itmo.se.mapmarks.moveToMark

class MapWithCurrentLocation(val backedMap: GoogleMap, val holdingActivity: Activity) : LocationListener {
    private var locationManager: LocationManager? = null
    private var cameraMoved: Boolean = false

    init {
        requestPermissions()
    }

    val currentLocation: LatLng?
        get() = currentLocationMarker?.position

    override fun onLocationChanged(location: Location) {
        currentLocationMarker?.remove()
        val latLng = LatLng(location.latitude, location.longitude)
        currentLocationMarker = backedMap.addMarker(
            MarkerOptions().position(latLng).icon(
                BitmapDescriptorFactory.defaultMarker()
            )
        )
        if (cameraMoved) {
            moveToMark(backedMap, currentLocation!!)
            cameraMoved = false
        }
    }

    fun moveCameraToCurrentPosition() {
        cameraMoved = true
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                }
            }
        }
    }

    fun getLocation() {
        try {
            if (locationManager == null) {
                locationManager = holdingActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            }
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun requestPermissions() {
        holdingActivity.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    companion object {
        private var currentLocationMarker: Marker? = null
    }
}