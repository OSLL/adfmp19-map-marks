package ru.itmo.se.mapmarks.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView

abstract class MyElementsAdapter<T : RecyclerView.ViewHolder, R>(
    protected val allElements: List<R>,
    protected val context: Context
) : RecyclerView.Adapter<T>()