package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.view.View

// TODO tbd merge that classes
class AddMarkButtonOnClickListener(val activity: Activity, val requestCode: Int): View.OnClickListener {

    override fun onClick(view: View) {
        val newMarkIntent = Intent(activity, AddMarkActivity::class.java)
        activity.startActivityForResult(newMarkIntent, requestCode)
    }
}

class AddCategoryClickListener(val activity: Activity, val requestCode: Int): View.OnClickListener {

    override fun onClick(view: View) {
        val newMarkIntent = Intent(activity, AddCategoryActivity::class.java)
        activity.startActivityForResult(newMarkIntent, requestCode)
    }
}