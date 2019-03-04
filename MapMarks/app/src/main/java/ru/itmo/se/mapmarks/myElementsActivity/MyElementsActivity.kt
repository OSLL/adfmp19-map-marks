package ru.itmo.se.mapmarks.myElementsActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import ru.itmo.se.mapmarks.R
import kotlinx.android.synthetic.main.activity_my_elements.*
import ru.itmo.se.mapmarks.adapters.MyElementsViewAdapter

@SuppressLint("Registered")
abstract class MyElementsActivity<T : RecyclerView.ViewHolder, B>(private val addSuccessMessage: String) :
    AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_elements)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.adapter = actualAdapter(this)
        addButtonListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1 && resultCode == Activity.RESULT_OK) {
            myRecyclerView.adapter = actualAdapter(this)
            Toast.makeText(this@MyElementsActivity, addSuccessMessage, Toast.LENGTH_SHORT).show()
        }
    }

    abstract protected fun addButtonListener()

    abstract protected fun actualAdapter(context: Context): MyElementsViewAdapter<T, B>
}