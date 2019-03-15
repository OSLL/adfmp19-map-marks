package ru.itmo.se.mapmarks.addElementActivity

import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_add_mark.*
import ru.itmo.se.mapmarks.data.mark.Mark

class EditMarkActivity: ManipulateMarkActivity() {

    private lateinit var markToEdit: Mark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        markToEdit = markInfoContainer.getMarkByName(intent.getStringExtra("name"))

        title = "Редактирование метки"
        addMarkName.setText(markToEdit.name)
        addMarkDescription.setText(markToEdit.description)
    }

    override fun createAndAddMark(name: String, description: String, coordinates: ArrayList<LatLng>) {
        val newMark = if (coordinates.size == 1) {
            createMark(name, description, coordinates[0])
        } else {
            createPolygon(name, description, coordinates)
        }
        val markToEdit = markInfoContainer.getMarkByName(intent.getStringExtra("name"))
        markInfoContainer.updateMark(markToEdit, newMark)
    }
}