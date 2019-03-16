package ru.itmo.se.mapmarks.data.mark.polygon

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark

class PolygonMark(name: String, description: String, category: Category, internal var options: PolygonOptions) :
    Mark(name, description, category) {

    private var polygon: Polygon? = null

    init {
        options.clickable(true)
    }

    override fun getBound(): LatLngBounds {
        val builder = LatLngBounds.Builder()
        options.points.forEach { builder.include(it) }
        return builder.build()
    }

    override fun addToMap(map: GoogleMap) {
        polygon = map.addPolygon(options.strokeColor(Color.BLACK).fillColor(category.color))
        polygon!!.tag = this
    }

    override fun remove() {
        polygon?.remove()
        polygon = null
    }

    override fun getPosition() = getBound().center
}