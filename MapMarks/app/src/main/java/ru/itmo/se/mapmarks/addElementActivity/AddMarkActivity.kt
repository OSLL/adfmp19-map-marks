package ru.itmo.se.mapmarks.addElementActivity

import com.google.android.gms.maps.model.LatLng

class AddMarkActivity : ManipulateMarkActivity() {
    override fun createAndAddMark(name: String, description: String, coordinates: ArrayList<LatLng>) {
        val newMark = if (coordinates.size == 1) {
            createMark(name, description, coordinates[0])
        } else {
            createPolygon(name, description, coordinates)
        }
        markInfoContainer.addMark(newMark)
    }
}
