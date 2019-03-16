package ru.itmo.se.mapmarks

import android.R
import android.content.res.ColorStateList
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.lang.Math.rint

fun setViewColor(view: View, color: Int) {
    view.backgroundTintList =
        ColorStateList(Array(1) { IntArray(1) { R.attr.state_enabled } }, IntArray(1) { color })
}

fun flyToMark(map: GoogleMap, bound: LatLngBounds) {
    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bound, 10)
    map.animateCamera(cameraUpdate)
}

fun moveToMark(map: GoogleMap, latLng: LatLng) {
    val position = CameraPosition.Builder().target(latLng).build()
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position.target, 17.0f))
}

fun getDistance(latLng1: LatLng, latLng2: LatLng): Double {
    val lat1 = latLng1.latitude
    val lon1 = latLng1.longitude
    val lat2 = latLng2.latitude
    val lon2 = latLng2.longitude
    val radius = 6371// radius of earth in Km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + (Math.cos(Math.toRadians(lat1))
            * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
            * Math.sin(dLon / 2))
    val c = 2 * Math.asin(Math.sqrt(a))
    return rint(100.0 * radius * c) / 100.0
}