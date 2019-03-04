package ru.itmo.se.mapmarks.addElementActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import kotlinx.android.synthetic.main.activity_add_element.*
import ru.itmo.se.mapmarks.R
import java.io.IOException
import android.content.Intent


@SuppressLint("Registered")
abstract class AddElementActivity : AppCompatActivity() {
    protected val markInfoContainer = DummyMarkInfoContainer.INSTANCE

    abstract fun addElementAction(name: String, description: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_element)
    }

    protected inner class OnNextButtonClickListener(
        private val nameErrorMessage: String,
        private val descriptionErrorMessage: String
    ) : View.OnClickListener {

        override fun onClick(view: View) {
            val isMarkNameNotEmpty =
                verifyInputTextEmptiness(addNameLayout, addName, nameErrorMessage)
            val isMarkDescriptionNotEmpty = verifyInputTextEmptiness(
                addDescriptionLayout,
                addDescription,
                descriptionErrorMessage
            )

            // TODO tbd activity to define position
            if (isMarkNameNotEmpty && isMarkDescriptionNotEmpty) {
                val name = addName.text.toString()
                val description = addDescription.text.toString()
                try {
                    addElementAction(name, description)
                    setResult(Activity.RESULT_OK, Intent().putExtra("name", name))
                    finish()
                } catch (e: IOException) {
                    addNameLayout.error = e.message
                }
            }
        }
    }

    protected fun verifyInputTextEmptiness(
        textInputLayout: TextInputLayout,
        textInputEdit: TextInputEditText,
        errorMessage: String
    ): Boolean {
        val isInputEmpty = TextUtils.isEmpty(textInputEdit.text.toString())
        textInputLayout.error = if (isInputEmpty) errorMessage else null
        return !isInputEmpty
    }
}