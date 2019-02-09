package ru.itmo.se.mapmarks.data.mark

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class GsonMarkDataWriter: MarkDataWriter {

    override fun write(data: Mark) {

    }

    private inner class MarkDataSerializer: JsonSerializer<Mark> {

        override fun serialize(src: Mark, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            val serializedData = JsonObject()
            serializedData.addProperty("name", src.name)
            serializedData.addProperty("description", src.description)

            val serializedLocation = JsonObject()
            val markPosition = src.options.position
            serializedLocation.addProperty("latitude", markPosition.latitude)
            serializedLocation.addProperty("latitude", markPosition.longitude)

            serializedData.add("position", serializedLocation)
            return serializedData
        }
    }
}