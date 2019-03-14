package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.view.View
import ru.itmo.se.mapmarks.addElementActivity.AddCategoryActivity
import ru.itmo.se.mapmarks.addElementActivity.AddMarkActivity
import ru.itmo.se.mapmarks.addElementActivity.EditMarkActivity

class AddMarkButtonOnClickListener(val activity: Activity, val requestCode: Int): View.OnClickListener {

    override fun onClick(view: View) {
        val newMarkIntent = Intent(activity, AddMarkActivity::class.java)
        activity.startActivityForResult(newMarkIntent, requestCode)
    }
}

class EditMarkButtonOnClickListener(val activity: Activity, val requestCode: Int, val markNameToEdit: String): View.OnClickListener {

    override fun onClick(view: View) {
        val newMarkIntent = Intent(activity, EditMarkActivity::class.java)
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