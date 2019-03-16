package ru.itmo.se.mapmarks.data.mark

import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface GsonMarkWriter {

    fun LatLng.write(): JsonElement {
        val element = JsonObject()
        element.addProperty("latitude", latitude)
        element.addProperty("longitude", longitude)
        return element
    }

    fun Mark.writeCommonDataTo(jsonObject: JsonObject) {
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("description", description)
        jsonObject.addProperty("category", category.name)
    }
}