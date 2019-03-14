package ru.itmo.se.mapmarks.addElementActivity

import android.annotation.SuppressLint
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_add_mark.*
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import java.io.IOException

@SuppressLint("Registered")
abstract class ManipulateElementActivity: AppCompatActivity() {

    protected val markInfoContainer = DummyMarkInfoContainer.INSTANCE
    protected val categoriesList = mutableListOf<String>()

    abstract fun propagateToNextActivity(name: String, description: String)
    abstract fun doNext()

    protected inner class OnNextButtonClickListener(
        private val inputVerifier: InputVerifier
    ) : View.OnClickListener {

        override fun onClick(view: View) {

            if (inputVerifier.verify()) {
                doNext()
            }
        }
    }

    abstract class InputVerifier {
        abstract fun verify(): Boolean

        fun verifyInputTextEmptiness(
            textInputLayout: TextInputLayout,
            textInputEdit: TextInputEditText,
            errorMessage: String
        ): Boolean {
            val isInputEmpty = TextUtils.isEmpty(textInputEdit.text.toString())
            textInputLayout.error = if (isInputEmpty) errorMessage else null
            return !isInputEmpty
        }
    }
}