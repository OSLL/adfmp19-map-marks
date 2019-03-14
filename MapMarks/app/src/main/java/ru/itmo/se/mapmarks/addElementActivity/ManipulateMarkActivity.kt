package ru.itmo.se.mapmarks.addElementActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_add_mark.*
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.SelectCategorySpinnerListener
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.data.resources.RequestCodes
import java.io.IOException

abstract class ManipulateMarkActivity: ManipulateElementActivity() {

    abstract fun createAndAddMark(name: String, description: String, position: LatLng?)

    protected fun createMark(name: String, description: String, position: LatLng?) = Mark(
        name,
        description,
        markInfoContainer.getCategoryByName(categoriesList[addSelectCategorySpinner.selectedItemPosition]),
        MarkerOptions().position(position!!)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mark)
        initSpinner()
        addNextButton.setOnClickListener(
            OnNextButtonClickListener(ManipulateMarkInputVerifier())
        )
    }

    override fun propagateToNextActivity(name: String, description: String) {
        val selectMarkPositionIntent = Intent(this, SelectMarkPositionActivity::class.java)
        selectMarkPositionIntent.putExtra("name", name).putExtra("description", description)
        startActivityForResult(selectMarkPositionIntent, RequestCodes.MARK_SELECT_POSITION)
    }

    override fun doNext() {
        val name = addMarkName.text.toString()
        val description = addMarkDescription.text.toString()
        try {
            propagateToNextActivity(name, description)
        } catch (e: IOException) {
            addMarkNameLayout.error = e.message
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    RequestCodes.MARK_ADD_NEW_CATEGORY -> {
                        val newCategoryName = data.getStringExtra("name")
                        categoriesList += newCategoryName
                        addSelectCategorySpinner.adapter = getActualAdapter(categoriesList)

                        // Last item in adapter is actually not a category, but an option to start new activity,
                        // so it is needed to subtract 2 to get an appropriate position
                        addSelectCategorySpinner.setSelection(addSelectCategorySpinner.adapter.count - 2)
                        Toast.makeText(this, "Категория добавлена", Toast.LENGTH_SHORT)
                            .show()
                    }
                    RequestCodes.MARK_SELECT_POSITION -> {
                        val name = data.getStringExtra("name")
                        val description = data.getStringExtra("description")
                        val latitude = data.getDoubleExtra("lat", -1.0)
                        val longitude = data.getDoubleExtra("lon", -1.0)
                        createAndAddMark(name, description, LatLng(latitude, longitude))
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        }
    }

    protected fun getActualAdapter(list: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            list + listOf("Новая категория...")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun initSpinner() {
        // There is the only assignment to categoriesSpinner
        categoriesList += markInfoContainer.allCategories.map { it.name }

        val adapter = getActualAdapter(categoriesList)
        addSelectCategorySpinner.adapter = adapter

        val listener = SelectCategorySpinnerListener(this, addSelectCategorySpinner.adapter)
        addSelectCategorySpinner.setOnTouchListener(listener)
        addSelectCategorySpinner.onItemSelectedListener = listener
    }

    private inner class ManipulateMarkInputVerifier: InputVerifier() {

        override fun verify(): Boolean {
            val isMarkNameNotEmpty =
                verifyInputTextEmptiness(addMarkNameLayout, addMarkName, markNameIsEmptyErrorMessage)
            val isMarkDescriptionNotEmpty = verifyInputTextEmptiness(
                addMarkDescriptionLayout,
                addMarkDescription,
                markDescriptionIsEmptyErrorMessage
            )
            return isMarkNameNotEmpty && isMarkDescriptionNotEmpty
        }
    }

    companion object {
        private const val markNameIsEmptyErrorMessage = "Название метки не должно быть пустым"
        private const val markDescriptionIsEmptyErrorMessage = "Описание метки не должно быть пустым"
    }
}