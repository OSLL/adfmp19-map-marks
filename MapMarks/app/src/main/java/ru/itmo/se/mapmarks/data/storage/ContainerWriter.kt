package ru.itmo.se.mapmarks.data.storage

interface ContainerWriter {
    fun write(container: MarkInfoContainer): String
}