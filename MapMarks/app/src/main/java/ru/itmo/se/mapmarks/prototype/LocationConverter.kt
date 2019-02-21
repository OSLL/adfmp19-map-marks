package ru.itmo.se.mapmarks.prototype

import android.location.Location

class LocationConverter {
    companion object {
        fun convert(latitude: Double, longitude: Double): String {
            val builder = StringBuilder()

            if (latitude < 0) {
                builder.append("S ")
            } else {
                builder.append("N ")
            }

            val latitudeDegrees = Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS)
            val latitudeSplit = latitudeDegrees.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            builder.append(latitudeSplit[0])
            builder.append("°")
            builder.append(latitudeSplit[1])
            builder.append("'")
            builder.append(latitudeSplit[2])
            builder.append("\"")

            builder.append(" ")

            if (longitude < 0) {
                builder.append("W ")
            } else {
                builder.append("E ")
            }

            val longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS)
            val longitudeSplit = longitudeDegrees.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            builder.append(longitudeSplit[0])
            builder.append("°")
            builder.append(longitudeSplit[1])
            builder.append("'")
            builder.append(longitudeSplit[2])
            builder.append("\"")

            return builder.toString()
        }
    }
}