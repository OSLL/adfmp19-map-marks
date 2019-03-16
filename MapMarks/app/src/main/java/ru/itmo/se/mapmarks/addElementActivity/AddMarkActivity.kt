package ru.itmo.se.mapmarks.addElementActivity

import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_add_mark.*

class AddMarkActivity : ManipulateMarkActivity() {
    override fun createAndAddMark(name: String, description: String, coordinates: ArrayList<LatLng>) {
        val newMark = if (coordinates.size == 1) {
            createMark(name, description, coordinates[0])
        } else {
            createPolygon(name, description, coordinates)
        }
        markInfoContainer.addMark(newMark)
    }

    override fun doNext() {
        val name = addMarkName.text.toString()
        val description = addMarkDescription.text.toString()
        if (!markInfoContainer.containsMark(name)) {
            propagateToNextActivity(name, description)
        } else {
            addMarkNameLayout.error = "Метка с таким именем уже существует"
        }
    }
}
