package ru.itmo.se.mapmarks

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer

class AddCategoryActivity : AppCompatActivity() {

    private val markInfoContainer = DummyMarkInfoContainer.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        val addCategoryDoneButton = findViewById<Button>(R.id.add_category_done_button)
        val addCategoryNameLayout = findViewById<TextInputLayout>(R.id.add_category_name_layout)
        val addCategoryNameEdit = findViewById<TextInputEditText>(R.id.add_category_name)
        val addCategoryDescriptionLayout = findViewById<TextInputLayout>(R.id.add_category_description_layout)
        val addCategoryDescriptionEdit = findViewById<TextInputEditText>(R.id.add_category_description)

        addCategoryDoneButton.setOnClickListener {
            val isCategoryNameNotEmpty = verifyInputTextEmptiness(addCategoryNameLayout, addCategoryNameEdit, "Имя категории не должно быть пустым")
            val isCategoryDescriptionNotEmpty = verifyInputTextEmptiness(addCategoryDescriptionLayout, addCategoryDescriptionEdit, "Описание категории не должно быть пустым")

            if (isCategoryNameNotEmpty && isCategoryDescriptionNotEmpty) {
                val parentActivityIntent = Intent()
                val categoryName = addCategoryNameEdit.text.toString()
                val categoryDescription = addCategoryDescriptionEdit.text.toString()

                // TODO tbd more smart notification about successful operation (maybe another callback)
                var addSuccessful = true
                markInfoContainer.addCategory(Category(categoryName, categoryDescription)).ifAlreadyExists {
                    addCategoryNameLayout.error = "Категория с таким именем уже существует"
                    addSuccessful = false
                }

                if (addSuccessful) {
                    parentActivityIntent.putExtra("cn", categoryName)
                    parentActivityIntent.putExtra("cd", categoryDescription)
                    setResult(Activity.RESULT_OK, parentActivityIntent)
                    finish()
                }
            }
        }
    }

    // TODO tbd better verifiers
    private fun verifyInputTextEmptiness(textInputLayout: TextInputLayout, textInputEdit: TextInputEditText, errorMessage: String): Boolean {
        val isInputEmpty = TextUtils.isEmpty(textInputEdit.text.toString())
        textInputLayout.error = if (isInputEmpty) errorMessage else null
        return !isInputEmpty
    }
}
