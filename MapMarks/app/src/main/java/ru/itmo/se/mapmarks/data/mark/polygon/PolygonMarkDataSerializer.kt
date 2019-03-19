package ru.itmo.se.mapmarks.data.mark.polygon

import com.google.gson.*
import ru.itmo.se.mapmarks.data.mark.GsonMarkWriter
import java.lang.reflect.Type

class PolygonMarkDataSerializer : JsonSerializer<PolygonMark>, GsonMarkWriter {

    override fun serialize(src: PolygonMark, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val serializedData = JsonObject()
        src.writeCommonDataTo(serializedData)

        serializedData.addProperty("polygon", true)

        val vertices = JsonArray()
        src.options.points.forEach {
            vertices.add(it.write())
        }

        serializedData.add("vertices", vertices)
        return serializedData
    }
}