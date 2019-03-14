package ru.itmo.se.mapmarks.addElementActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.activity_add_mark.*
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.data.mark.Mark
import java.io.IOException
import kotlin.random.Random

@SuppressLint("Registered")
abstract class ManipulateCategoryActivity: ManipulateElementActivity() {

    abstract fun createAndAddCategory(name: String, description: String)

    protected fun createCategory(name: String, description: String) = Category(
        name,
        description,
        Color.argb(255, Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)
        addCategoryDoneButton.setOnClickListener(
            OnNextButtonClickListener(ManipulateCategoryInputVerifier())
        )
    }

    override fun propagateToNextActivity(name: String, description: String) {
        createAndAddCategory(name, description)
        setResult(Activity.RESULT_OK, Intent().putExtra("name", name))
        finish()
    }

    override fun doNext() {
        val name = addCategoryName.text.toString()
        val description = addCategoryDescription.text.toString()
        try {
            propagateToNextActivity(name, description)
        } catch (e: IOException) {
            addCategoryNameLayout.error = e.message
        }
    }

    private inner class ManipulateCategoryInputVerifier: InputVerifier() {

        override fun verify(): Boolean {
            val isCategoryNameNotEmpty =
                verifyInputTextEmptiness(addCategoryNameLayout, addCategoryName, categoryNameIsEmptyErrorMessage)
            val isCategoryDescriptionNotEmpty = verifyInputTextEmptiness(
                addCategoryDescriptionLayout,
                addCategoryDescription,
                categoryDescriptionIsEmptyErrorMessage
            )
            return isCategoryNameNotEmpty && isCategoryDescriptionNotEmpty
        }
    }

    companion object {
        private const val categoryNameIsEmptyErrorMessage = "Название метки не должно быть пустым"
        private const val categoryDescriptionIsEmptyErrorMessage = "Описание метки не должно быть пустым"
    }
}