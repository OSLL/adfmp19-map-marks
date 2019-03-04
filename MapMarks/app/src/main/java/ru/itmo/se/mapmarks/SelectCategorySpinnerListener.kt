package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.widget.AdapterView
import android.view.MotionEvent
import android.view.View
import android.widget.Adapter
import ru.itmo.se.mapmarks.addElementActivity.AddCategoryActivity


class SelectCategorySpinnerListener(val activity: Activity, val adapter: Adapter) : AdapterView.OnItemSelectedListener, View.OnTouchListener {

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var userSelect = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        userSelect = true
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        if (userSelect) {
            if (position == adapter.count - 1) {
                val requestCode = 1
                activity.startActivityForResult(Intent(activity, AddCategoryActivity::class.java), requestCode)
            }
            userSelect = false
        }
    }

}