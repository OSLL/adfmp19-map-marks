package ru.itmo.se.mapmarks.data.mark.point

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark

class PointMark(name: String, description: String, category: Category, private var options: MarkerOptions) :
    Mark(name, description, category) {

    private var marker: Marker? = null

    override fun getBound() = LatLngBounds(options.position, options.position)

    override fun addToMap(map: GoogleMap) {
        if (marker == null) {
            marker = map.addMarker(options.icon(getMarkerIcon(category.color)))
        }
        marker!!.tag = this
        marker!!.title = name
    }

    override fun remove() {
        if (marker != null) {
            marker!!.remove()
            marker = null
        }
    }

    override fun getPosition() = options.position

    fun write(writer: PointMarkDataWriter) {
        writer.write(this)
    }

    private fun getMarkerIcon(color: Int): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }
}