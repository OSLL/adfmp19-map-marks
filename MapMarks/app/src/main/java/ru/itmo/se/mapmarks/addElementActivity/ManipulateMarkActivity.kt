package ru.itmo.se.mapmarks.addElementActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import kotlinx.android.synthetic.main.activity_add_mark.*
import ru.itmo.se.mapmarks.R
import ru.itmo.se.mapmarks.data.mark.point.PointMark
import ru.itmo.se.mapmarks.data.mark.polygon.PolygonMark
import ru.itmo.se.mapmarks.data.resources.RequestCodes

abstract class ManipulateMarkActivity : ManipulateElementActivity() {

    abstract fun createAndAddMark(name: String, description: String, coordinates: ArrayList<LatLng>)

    protected fun createMark(name: String, description: String, position: LatLng?) = PointMark(
        name,
        description,
        markInfoContainer.getCategoryByName(categoriesList[addSelectCategorySpinner.selectedItemPosition - 1]),
        MarkerOptions().position(position!!)
    )

    protected fun createPolygon(name: String, description: String, coordinates: ArrayList<LatLng>) = PolygonMark(
        name,
        description,
        markInfoContainer.getCategoryByName(categoriesList[addSelectCategorySpinner.selectedItemPosition - 1]),
        PolygonOptions().addAll(coordinates)
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
        if (addSelectCategorySpinner.selectedItemPosition > 0) {
            val selectMarkPositionIntent = Intent(this, SelectMarkPositionActivity::class.java)
            selectMarkPositionIntent
                .putExtra("name", name)
                .putExtra("description", description)
            if (intent.hasExtra("coordinates")) {
                selectMarkPositionIntent.putExtra("coordinates", intent.getDoubleArrayExtra("coordinates"))
            }
            startActivityForResult(selectMarkPositionIntent, RequestCodes.MARK_SELECT_POSITION)
        } else {
            Toast.makeText(this, "Выберите категорию или создайте новую", Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    when (requestCode) {
                        RequestCodes.MARK_ADD_NEW_CATEGORY -> {
                            val newCategoryName = data.getStringExtra("name")
                            categoriesList += newCategoryName
                            updateSpinnerAdapterAndListener()

                            // Last item in adapter is actually not a category, but an option to start new activity,
                            // so it is needed to subtract 2 to get an appropriate position
                            addSelectCategorySpinner.setSelection(addSelectCategorySpinner.adapter.count - 2)
                            Toast.makeText(this, "Категория добавлена", Toast.LENGTH_SHORT)
                                .show()
                        }
                        RequestCodes.MARK_SELECT_POSITION -> {
                            val name = data.getStringExtra("name")
                            val description = data.getStringExtra("description")
                            val coordinates = data.getSerializableExtra("coordinates") as? ArrayList<LatLng>
                            if (coordinates != null) {
                                createAndAddMark(name, description, coordinates)
                            }
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }
                }
            }
        }
    }

    protected fun getActualAdapter(list: List<String>): ArrayAdapter<String> {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("- Категория -") + list + listOf("Новая категория...")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun initSpinner() {
        // There is the only assignment to categoriesSpinner
        categoriesList += markInfoContainer.allCategories.map { it.name }

        updateSpinnerAdapterAndListener()
    }

    private fun updateSpinnerAdapterAndListener() {
        val listener = SelectCategorySpinnerListener(this, categoriesList.size)
        addSelectCategorySpinner.setOnTouchListener(listener)
        addSelectCategorySpinner.onItemSelectedListener = listener

        val adapter = getActualAdapter(categoriesList)
        addSelectCategorySpinner.adapter = adapter
    }

    private inner class ManipulateMarkInputVerifier : InputVerifier() {

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

    private inner class SelectCategorySpinnerListener(val activity: Activity, val variantsCount: Int) : AdapterView.OnItemSelectedListener, View.OnTouchListener {

        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            v.performClick()
            return false
        }

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            // First item in adapter is non-active
            if (position == variantsCount + 1) {
                activity.startActivityForResult(
                    Intent(activity, AddCategoryActivity::class.java),
                    RequestCodes.MARK_ADD_NEW_CATEGORY
                )
            }
        }
    }

    companion object {
        private const val markNameIsEmptyErrorMessage = "Название метки не должно быть пустым"
        private const val markDescriptionIsEmptyErrorMessage = "Описание метки не должно быть пустым"
    }
}