package ru.itmo.se.mapmarks.data.mark

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.point.PointMark
import ru.itmo.se.mapmarks.data.mark.polygon.PolygonMark
import java.lang.reflect.Type

class MarkDeserializer: JsonDeserializer<Mark> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext?): Mark {
        val serializedMark = json.asJsonObject
        val name = serializedMark.get("name").asString
        val description = serializedMark.get("description").asString
        val categoryName = serializedMark.get("category").asString
        val isPolygon = serializedMark.get("polygon").asBoolean
        if (isPolygon) {
            val polygonOptions = PolygonOptions()
            val vertices = serializedMark.get("vertices").asJsonArray
            vertices.forEach { vertex ->
                vertex.asJsonObject.let {
                    val latitude = it.get("latitude").asDouble
                    val longitude = it.get("longitude").asDouble
                    polygonOptions.add(LatLng(latitude, longitude))
                }
            }
            return PolygonMark(name, description, dummyCategory(categoryName), polygonOptions)
        } else {
            val position = serializedMark.get("position").asJsonObject
            val latitude = position.get("latitude").asDouble
            val longitude = position.get("longitude").asDouble
            return PointMark(name, description, dummyCategory(categoryName), MarkerOptions().position(LatLng(latitude, longitude)))
        }
    }

    private fun dummyCategory(name: String) = Category(name, "", 0)
}