package ru.itmo.se.mapmarks.data.mark

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import ru.itmo.se.mapmarks.data.category.Category

class Mark(
    val name: String,
    val description: String,
    val category: Category,
    val options: MarkerOptions
) {
    fun write(writer: MarkDataWriter) {
        writer.write(this)
    }

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
}

fun GoogleMap.addMarker(marker: Mark) {
    addMarker(marker.options)
}