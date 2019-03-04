package ru.itmo.se.mapmarks.location

import android.app.Activity
import com.google.android.gms.maps.model.LatLng

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