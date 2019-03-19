package ru.itmo.se.mapmarks.data.storage

import android.content.Context
import android.util.Log
import ru.itmo.se.mapmarks.data.mark.point.PointMark
import ru.itmo.se.mapmarks.data.mark.polygon.PolygonMark
import java.io.FileNotFoundException

class SavedMarkInfoContainer: ListMarkInfoContainer() {

    companion object {
        private var data: String? = null

        fun register(applicationContext: Context, path: String) {
            try {
                applicationContext.openFileInput(path).use {
                    data = String(it.readBytes())
                }
            } catch (_: FileNotFoundException) {
                Log.i("Load from internal storage", "File not found, using empty container")
            }
        }

        val INSTANCE: MarkInfoContainer by lazy {
            val container = data?.let { MarkInfoContainerFactory.fromString(it) } ?: SavedMarkInfoContainer()
            container.allMarks.forEach {
                val newMark = when (it) {
                    is PointMark -> PointMark(it.name, it.description, container.getCategoryByName(it.category.name), it.options)
                    is PolygonMark -> PolygonMark(it.name, it.description, container.getCategoryByName(it.category.name), it.options)
                    else -> throw IllegalStateException("Found unknown subclass of class Point: ${it::class.java}")
                }
                container.updateMark(it, newMark)
            }
            container
        }
    }
}