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
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MyCategoriesActivity : AppCompatActivity() {

    private lateinit var cardsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_categories)

        cardsView = findViewById(R.id.my_categories_recycler_view)
        cardsView.layoutManager = LinearLayoutManager(this)

        cardsView.adapter = actualAdapter(this)

        val addCategoryButton = findViewById<FloatingActionButton>(R.id.add_category_button_my_categories)
        addCategoryButton.setOnClickListener(AddCategoryClickListener(this, requestCode = 1))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1 && resultCode == Activity.RESULT_OK) {
            cardsView.adapter = actualAdapter(this)
            Toast.makeText(this@MyCategoriesActivity, "Категория добавлена", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private fun actualAdapter(context: Context) = MyCategoriesViewAdapter(DummyMarkInfoContainer.INSTANCE.allCategories.toList(), context)
    }
}
