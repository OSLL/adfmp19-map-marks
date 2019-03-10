package ru.itmo.se.mapmarks.myElementsActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_my_elements.*
import ru.itmo.se.mapmarks.MainScreenActivity
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.StartActivityForResultListener
import ru.itmo.se.mapmarks.adapters.MyMarksViewAdapter
import ru.itmo.se.mapmarks.addElementActivity.AddMarkActivity
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MyMarksActivity : MyElementsActivity<MyMarksViewAdapter.MyMarksViewHolder, Mark>("Метка добавлена") {
    private var marksList = DummyMarkInfoContainer.INSTANCE.allMarks.toList()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_my_elements)
        val intent = intent
        val categoryName = intent.getStringExtra("name")
        if (categoryName != null) {
            marksList = marksList.filter { it.category.name == categoryName }.toList()
        }
        super.onCreate(savedInstanceState)
        if (categoryName != null) {
            categoriesMarksViewButton.visibility = View.VISIBLE
            categoriesMarksViewButton.setOnClickListener { viewCategoryMarks(categoryName) }
        }
    }

    override fun addButtonListener() {
        addButton.setOnClickListener {
            StartActivityForResultListener(this, AddMarkActivity::class.java, 1)
        }
    }

    override fun actualAdapter(context: Context) = MyMarksViewAdapter(marksList, context)

    private fun viewCategoryMarks(categoryName: String) {
        val newMarkIntent = Intent(this, MainScreenActivity::class.java)
        newMarkIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        newMarkIntent.putExtra("categoryName", categoryName)
        finish()
        startActivity(newMarkIntent)
    }
}

