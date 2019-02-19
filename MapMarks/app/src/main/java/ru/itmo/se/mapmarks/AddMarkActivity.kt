package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class AddMarkActivity : AppCompatActivity() {

    private val markInfoContainer = DummyMarkInfoContainer.INSTANCE
    private lateinit var categoriesSpinner: Spinner
    private val categoriesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mark)

        // There is the only assignment to categoriesSpinner
        categoriesSpinner = findViewById(R.id.add_mark_select_category_spinner)

        categoriesList += markInfoContainer.allCategories.map { it.name }

        val adapter = getActualAdapter(categoriesList)
        categoriesSpinner.adapter = adapter

        val listener = SelectCategorySpinnerListener(this, categoriesSpinner.adapter)
        categoriesSpinner.setOnTouchListener(listener)
        categoriesSpinner.onItemSelectedListener = listener

        val nextButton = findViewById<Button>(R.id.add_mark_next_button)
        // TODO listener to go to next activity
        nextButton.setOnClickListener(OnNextButtonClickListener())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val newCategoryName = data.getStringExtra("cn")
                categoriesList += newCategoryName
                categoriesSpinner.adapter = getActualAdapter(categoriesList)

                // Last item in adapter is actually not a category, but an option to start new activity,
                // so it is needed to subtract 2 to get an appropriate position
                categoriesSpinner.setSelection(categoriesSpinner.adapter.count - 2)
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

    private inner class OnNextButtonClickListener: View.OnClickListener {

        override fun onClick(view: View) {
            val addMarkNameLayout = findViewById<TextInputLayout>(R.id.add_mark_name_layout)
            val addMarkNameEdit = findViewById<TextInputEditText>(R.id.add_mark_name)
            val addMarkDescriptionLayout = findViewById<TextInputLayout>(R.id.add_mark_description_layout)
            val addMarkDescriptionEdit = findViewById<TextInputEditText>(R.id.add_mark_description)

            val isMarkNameNotEmpty = verifyInputTextEmptiness(addMarkNameLayout, addMarkNameEdit, "Название метки не должно быть пустым")
            val isMarkDescriptionNotEmpty = verifyInputTextEmptiness(addMarkDescriptionLayout, addMarkDescriptionEdit, "Описание метки не должно быть пустым")

            // TODO tbd activity to define position
            if (isMarkNameNotEmpty && isMarkDescriptionNotEmpty) {
                val markName = addMarkNameEdit.text.toString()
                val markDescription = addMarkDescriptionEdit.text.toString()

                val newMark = DummyMarkInfoContainer.newMarkWithRandomLocation(
                    markName,
                    markDescription,
                    markInfoContainer.getCategoryByName(categoriesList[categoriesSpinner.selectedItemPosition])
                )

                var addSuccessful = true
                markInfoContainer.addMark(newMark)
                    .ifMarkAlreadyExists { mark ->
                        addMarkNameLayout.error = "Метка с таким именем уже существует"
                        addSuccessful = false
                    }

                if (addSuccessful) {
                    setResult(Activity.RESULT_OK, parentActivityIntent)
                    finish()
                }
            }
        }
    }

    private fun verifyInputTextEmptiness(textInputLayout: TextInputLayout, textInputEdit: TextInputEditText, errorMessage: String): Boolean {
        val isInputEmpty = TextUtils.isEmpty(textInputEdit.text.toString())
        textInputLayout.error = if (isInputEmpty) errorMessage else null
        return !isInputEmpty
    }

}
