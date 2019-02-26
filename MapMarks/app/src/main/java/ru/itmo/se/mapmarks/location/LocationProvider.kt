package ru.itmo.se.mapmarks.location

import android.app.Activity
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import android.widget.Toast



class LocationProvider {
    companion object {
        fun from(activity: Activity): LatLng? {
            val locationTrack = LocationTrack(activity)

            if (locationTrack.canGetLocation()) {
                val longitude = locationTrack.getLongitude()
                val latitude = locationTrack.getLatitude()
                return LatLng(latitude, longitude)
            } else {
                locationTrack.showSettingsAlert()
                return null
            }
        }
    }
}