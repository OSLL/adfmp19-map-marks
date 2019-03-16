package ru.itmo.se.mapmarks.data.mark.share

import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.prototype.LocationConverter
import java.lang.StringBuilder

class ShareableMark(val mark: Mark) {

    fun makrShareBody(): String {
        val shareBodyBuilder = StringBuilder("\n")
        shareBodyBuilder
            .append("Check that mark on your map!")
            .append("Name: ${mark.name}")
            .append("Description: ${mark.description}")
            .append(mark.getPosition().let { "Location: (${LocationConverter.convert(it.latitude, it.longitude)})" })
        return shareBodyBuilder.toString()
    }
}