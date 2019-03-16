package ru.itmo.se.mapmarks.data.storage

import android.content.Context
import android.util.Log
import ru.itmo.se.mapmarks.data.mark.point.PointMark
import ru.itmo.se.mapmarks.data.mark.polygon.PolygonMark

class SavedMarkInfoContainer: ListMarkInfoContainer() {

    companion object {
        private lateinit var data: String

        fun register(applicationContext: Context, saveFileId: Int) {
            applicationContext.openFileInput(applicationContext.getString(saveFileId)).use {
                data = String(it.readBytes())
            }
        }

        val INSTANCE: MarkInfoContainer by lazy {
            val container = MarkInfoContainerFactory.fromString(data)
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