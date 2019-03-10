package ru.itmo.se.mapmarks

import android.R
import android.content.res.ColorStateList
import android.view.View

fun setViewColor(view: View, color: Int) {
    view.backgroundTintList =
        ColorStateList(Array(1) { IntArray(1) { R.attr.state_enabled } }, IntArray(1) { color })
}