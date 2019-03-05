package ru.itmo.se.mapmarks.myElementsActivity

import android.content.Context
import kotlinx.android.synthetic.main.activity_my_elements.*
import ru.itmo.se.mapmarks.AddCategoryClickListener
import ru.itmo.se.mapmarks.adapters.MyCategoriesViewAdapter
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class MyCategoriesActivity :
    MyElementsActivity<MyCategoriesViewAdapter.MyCategoriesViewHolder, Category>("Категория добавлена") {
    override fun addButtonListener() {
        addButton.setOnClickListener(AddCategoryClickListener(this, 1))
    }

    override fun actualAdapter(context: Context) = MyCategoriesViewAdapter(DummyMarkInfoContainer.INSTANCE.allCategories.toList(), context)
}