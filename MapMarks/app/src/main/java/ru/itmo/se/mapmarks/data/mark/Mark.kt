package ru.itmo.se.mapmarks.data.mark

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ru.itmo.se.mapmarks.data.category.Category

abstract class Mark(
    var name: String,
    var description: String,
    var category: Category
) {
    var attach: Any? = null

    abstract fun getPosition(): LatLng

    abstract fun addToMap(map: GoogleMap)

    abstract fun getBound(): LatLngBounds

    abstract fun remove()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mark

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return name
    }
}