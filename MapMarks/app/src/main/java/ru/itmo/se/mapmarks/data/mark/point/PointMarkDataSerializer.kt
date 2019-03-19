package ru.itmo.se.mapmarks.data.mark.point

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import ru.itmo.se.mapmarks.data.mark.GsonMarkWriter
import java.lang.reflect.Type

class PointMarkDataSerializer : JsonSerializer<PointMark>, GsonMarkWriter {

    override fun serialize(src: PointMark, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val serializedData = JsonObject()
        src.writeCommonDataTo(serializedData)

        serializedData.addProperty("polygon", false)

        serializedData.add("position", src.getPosition().write())
        return serializedData
    }
}