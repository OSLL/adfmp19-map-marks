package ru.itmo.se.mapmarks.data.mark.polygon

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolygonOptions
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark

class PolygonMark(name: String, description: String, category: Category, private val options: PolygonOptions) :
    Mark(name, description, category) {

    init {
        options.clickable(true)
    }

    override fun getBound(): LatLngBounds {
        val builder = LatLngBounds.Builder()
        options.points.forEach { builder.include(it) }
        return builder.build()
    }

    override fun addToMap(map: GoogleMap) {
        map.addPolygon(options.strokeColor(Color.BLACK).fillColor(category.color)).tag = this
    }

    override fun getPosition() = getBound().center
}