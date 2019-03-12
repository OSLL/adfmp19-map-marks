package ru.itmo.se.mapmarks.data.mark.point

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class GsonPointMarkDataWriter : PointMarkDataWriter {

    override fun write(data: PointMark) {

    }

    private inner class MarkDataSerializer : JsonSerializer<PointMark> {

        override fun serialize(src: PointMark, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            val serializedData = JsonObject()
            serializedData.addProperty("name", src.name)
            serializedData.addProperty("description", src.description)

            val serializedLocation = JsonObject()
            val markPosition = src.getPosition()
            serializedLocation.addProperty("latitude", markPosition.latitude)
            serializedLocation.addProperty("latitude", markPosition.longitude)

            serializedData.add("position", serializedLocation)
            return serializedData
        }
    }
}