package ru.itmo.se.mapmarks.addElementActivity

import com.google.android.gms.maps.model.LatLng

class AddMarkActivity : ManipulateMarkActivity() {
    override fun createAndAddMark(name: String, description: String, position: LatLng?) {
        val newMark = createMark(name, description, position)
        markInfoContainer.addMark(newMark)
    }
}
