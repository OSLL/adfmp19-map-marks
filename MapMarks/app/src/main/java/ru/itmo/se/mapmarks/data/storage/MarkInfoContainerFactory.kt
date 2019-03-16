package ru.itmo.se.mapmarks.data.storage

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.data.mark.MarkDeserializer

object MarkInfoContainerFactory {

    fun fromString(src: String): MarkInfoContainer {
        return GsonBuilder()
            .registerTypeAdapter(Mark::class.java, MarkDeserializer())
            .create()
            .fromJson(src, ListMarkInfoContainer::class.java)
    }
}