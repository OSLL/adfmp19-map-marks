package ru.itmo.se.mapmarks.data.storage

import com.google.gson.*
import ru.itmo.se.mapmarks.data.mark.point.PointMark
import ru.itmo.se.mapmarks.data.mark.point.PointMarkDataSerializer
import ru.itmo.se.mapmarks.data.mark.polygon.PolygonMark
import ru.itmo.se.mapmarks.data.mark.polygon.PolygonMarkDataSerializer

class GsonContainerWriter: ContainerWriter {

    override fun write(container: MarkInfoContainer): String {
        return GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(PointMark::class.java, PointMarkDataSerializer())
            .registerTypeAdapter(PolygonMark::class.java, PolygonMarkDataSerializer())
            .create()
            .toJson(container)
    }
}