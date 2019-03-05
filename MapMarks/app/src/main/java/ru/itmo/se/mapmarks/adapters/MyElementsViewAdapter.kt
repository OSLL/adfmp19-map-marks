package ru.itmo.se.mapmarks.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView

abstract class MyElementsViewAdapter<T : RecyclerView.ViewHolder, R>(
    protected val allElements: List<R>,
    protected val context: Context
) : RecyclerView.Adapter<T>() {
    override fun getItemCount() = allElements.size
}