package ru.itmo.se.mapmarks.addElementActivity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import ru.itmo.se.mapmarks.data.category.Category
import kotlin.random.Random
import kotlinx.android.synthetic.main.activity_add_element.*

class AddCategoryActivity : AddElementActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSelectSpinner.visibility = View.INVISIBLE
        addNextButton.setOnClickListener(
            OnNextButtonClickListener(
                "Название категории не должно быть пустым", "Описание категории не должно быть пустым"
            )
        )
    }

    override fun addElementAction(name: String, description: String) {
        val newCategory = Category(
            name,
            description,
            Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
        )
        markInfoContainer.addCategory(newCategory)
    }
}
