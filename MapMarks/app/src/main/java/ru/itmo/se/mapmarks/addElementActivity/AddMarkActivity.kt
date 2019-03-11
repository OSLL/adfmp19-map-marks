package ru.itmo.se.mapmarks.addElementActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import kotlinx.android.synthetic.main.activity_add_element.*
import ru.itmo.se.mapmarks.SelectCategorySpinnerListener

class AddMarkActivity : AddElementActivity() {
    private val categoriesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // There is the only assignment to categoriesSpinner
        categoriesList += markInfoContainer.allCategories.map { it.name }

        val adapter = getActualAdapter(categoriesList)
        addSelectSpinner.adapter = adapter

        val listener = SelectCategorySpinnerListener(this, addSelectSpinner.adapter)
        addSelectSpinner.setOnTouchListener(listener)
        addSelectSpinner.onItemSelectedListener = listener

        // TODO listener to go to next activity
        addNextButton.setOnClickListener(
            OnNextButtonClickListener(
                "Название метки не должно быть пустым", "Описание метки не должно быть пустым"
            )
        )
    }

    override fun addElementAction(name: String, description: String) {
        val newMark = DummyMarkInfoContainer.newPointMarkWithRandomLocation(
            name,
            description,
            markInfoContainer.getCategoryByName(categoriesList[addSelectSpinner.selectedItemPosition])
        )
        markInfoContainer.addMark(newMark)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val newCategoryName = data.getStringExtra("name")
                categoriesList += newCategoryName
                addSelectSpinner.adapter = getActualAdapter(categoriesList)

//                 Last item in adapter is actually not a category, but an option to start new activity,
//                 so it is needed to subtract 2 to get an appropriate position
                addSelectSpinner.setSelection(addSelectSpinner.adapter.count - 2)
                Toast.makeText(this@AddMarkActivity, "Категория добавлена", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getActualAdapter(list: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            list + listOf("Новая категория...")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}
