package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.view.View

class StartActivityForResultListener(val currentActivity: Activity, val nextActivity: Class<*>, val requestCode: Int) :
    View.OnClickListener {

    override fun onClick(view: View) {
        val newMarkIntent = Intent(currentActivity, nextActivity)
        currentActivity.startActivityForResult(newMarkIntent, requestCode)
    }
}