package ru.itmo.se.mapmarks.data.mark.point

import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark

class PointMark(name: String, description: String, category: Category, private val options: MarkerOptions) :
    Mark(name, description, category) {
    override fun getBound() = LatLngBounds(options.position, options.position)

    override fun addToMap(map: GoogleMap) {
        map.addMarker(options.icon(getMarkerIcon(category.color))).tag = this
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