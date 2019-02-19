package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class CategoryMarksActivity : AppCompatActivity() {

    private val markInfoContainer = DummyMarkInfoContainer.INSTANCE
    private lateinit var cardsView: RecyclerView
    private lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_marks)

        cardsView = findViewById(R.id.category_marks_recycler_view)
        cardsView.layoutManager = LinearLayoutManager(this)

        category = markInfoContainer.getCategoryByName(intent.getStringExtra("cn"))

        cardsView.adapter = actualAdapter(this)

        val addMarkButton = findViewById<FloatingActionButton>(R.id.add_mark_button_category_marks)
        addMarkButton.setOnClickListener(AddMarkButtonOnClickListener(this, requestCode = 1))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1 && resultCode == Activity.RESULT_OK) {
            cardsView.adapter = actualAdapter(this)
            Toast.makeText(this@CategoryMarksActivity, "Метка добавлена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualAdapter(context: Context) = CategoryMarksViewAdapter(markInfoContainer.getMarksForCategory(category).toList(), context)
}
