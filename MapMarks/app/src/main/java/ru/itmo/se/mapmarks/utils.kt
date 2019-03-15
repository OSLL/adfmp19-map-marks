package ru.itmo.se.mapmarks

import android.R
import android.content.res.ColorStateList
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import ru.itmo.se.mapmarks.data.mark.Mark

fun setViewColor(view: View, color: Int) {
    view.backgroundTintList =
        ColorStateList(Array(1) { IntArray(1) { R.attr.state_enabled } }, IntArray(1) { color })
}

fun flyToMark(map: GoogleMap, mark: Mark) {
    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(mark.getBound(), 10)
    map.animateCamera(cameraUpdate)
}