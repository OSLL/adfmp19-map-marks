package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.view.View
import ru.itmo.se.mapmarks.addElementActivity.AddCategoryActivity
import ru.itmo.se.mapmarks.addElementActivity.AddMarkActivity

class AddMarkButtonOnClickListener(val activity: Activity, val requestCode: Int, val edit: Boolean = false, val markNameToEdit: String? = null): View.OnClickListener {

    override fun onClick(view: View) {
        val newMarkIntent = Intent(activity, AddMarkActivity::class.java)
        newMarkIntent.putExtra("edit", edit)
        newMarkIntent.putExtra("name", markNameToEdit)
        activity.startActivityForResult(newMarkIntent, requestCode)
    }
}

class AddCategoryClickListener(val activity: Activity, val requestCode: Int): View.OnClickListener {

    override fun onClick(view: View) {
        val newMarkIntent = Intent(activity, AddCategoryActivity::class.java)
        activity.startActivityForResult(newMarkIntent, requestCode)
    }
}