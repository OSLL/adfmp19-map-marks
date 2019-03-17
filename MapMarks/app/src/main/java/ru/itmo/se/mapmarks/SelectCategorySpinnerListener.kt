package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.AdapterView
import android.view.MotionEvent
import android.view.View
import android.widget.Adapter
import android.widget.Spinner
import ru.itmo.se.mapmarks.addElementActivity.AddCategoryActivity
import ru.itmo.se.mapmarks.data.resources.RequestCodes


class SelectCategorySpinnerListener(val activity: Activity, val variantsCount: Int) : AdapterView.OnItemSelectedListener, View.OnTouchListener {

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.performClick()
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        // First item in adapter is non-active
        if (position == variantsCount + 1) {
            activity.startActivityForResult(
                Intent(activity, AddCategoryActivity::class.java),
                RequestCodes.MARK_ADD_NEW_CATEGORY
            )
        }
    }
}