package ru.itmo.se.mapmarks

import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.core.AllOf.allOf

import org.junit.runner.RunWith

import android.support.test.espresso.action.ViewActions.typeText
import android.widget.EditText
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.view.KeyEvent
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.View
import org.hamcrest.CoreMatchers.*
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.*


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainScreenActivity::class.java, true, false)

    @Test
    fun testAddMarkAndAutocomplete() {
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPoint("newMark", "newCategory")
        findMark("newMark")
    }

    @Test
    fun testAddPolygon() {
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPolygon("newMark1", "newCategory1")
    }

    @Test
    fun testSaveToStorage() {
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPoint("newMark2", "newCategory2")
        activityRule.activity.finish()
        activityRule.launchActivity(Intent())
        findMark("newMark2")
    }

    @Test
    fun testEmptyMarkInputData() {
        val markName = "mark1"
        val categoryName = "category1"
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPoint(markName, categoryName)
        onView(withId(R.id.addMarkButtonMain)).perform(click())
        onView(withId(R.id.addMarkName)).perform(clearText(), closeSoftKeyboard())

        onView(withId(R.id.addSelectCategorySpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`(categoryName))).perform(click())
        testInputLayoutEmptiness(testMark = true)
    }

    @Test
    fun testDuplicateMarkName() {
        val markName = "mark2"
        val categoryName = "category2"
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPoint(markName, categoryName)
        onView(withId(R.id.addMarkButtonMain)).perform(click())
        onView(withId(R.id.addMarkName)).perform(typeText(markName), closeSoftKeyboard())
        onView(withId(R.id.addMarkDescription)).perform(typeText("something"), closeSoftKeyboard())
        onView(withId(R.id.addNextButton)).perform(click())
        onView(withId(R.id.addMarkNameLayout)).check(matches(hasTextInputLayoutErrorText("Метка с таким названием уже существует")))
    }

    @Test
    fun testEmptyCategoryInputData() {
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        onView(withId(R.id.addMarkButtonMain)).perform(click())
        onView(withId(R.id.addMarkName)).perform(clearText(), closeSoftKeyboard())

        onView(withId(R.id.addSelectCategorySpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Новая категория..."))).perform(click())
        testInputLayoutEmptiness(testMark = false)
    }

    @Test
    fun testDuplicateCategoryName() {
        val markName = "mark3"
        val categoryName = "category3"
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPoint(markName, categoryName)
        onView(withId(R.id.addMarkButtonMain)).perform(click())
        onView(withId(R.id.addSelectCategorySpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Новая категория..."))).perform(click())
        onView(withId(R.id.addCategoryName)).perform(typeText(categoryName), closeSoftKeyboard())
        onView(withId(R.id.addCategoryDescription)).perform(typeText("something..."), closeSoftKeyboard())
        onView(withId(R.id.addCategoryDoneButton)).perform(click())
        onView(withId(R.id.addCategoryNameLayout)).check(matches(hasTextInputLayoutErrorText("Категория с таким названием уже существует")))
    }

    private fun findMark(mark: String) {
        onView(withId(R.id.mainScreenSearch)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText(mark.substring(0, 2)))
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        uiDevice.pressEnter()
        val symb = mark[2]
        val keyCode = if (symb.isDigit()) symb.toInt() - 23 else symb.toInt() - 68
        uiDevice.pressKeyCode(keyCode)
        uiDevice.pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN)
        uiDevice.pressEnter()
        val mMarker1 = uiDevice.findObject(UiSelector().descriptionContains(mark))
        mMarker1.click()
    }

    private fun addAbstractMark(mark: String, category: String) {
        onView(withId(R.id.addMarkButtonMain)).perform(click())
        onView(withId(R.id.addMarkName)).perform(typeText(mark))
        onView(withId(R.id.addMarkDescription)).perform(typeText("MarkDescription"), closeSoftKeyboard())
        onView(withId(R.id.addSelectCategorySpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Новая категория..."))).perform(click())
        onView(withId(R.id.addCategoryName)).perform(typeText(category), closeSoftKeyboard())
        onView(withId(R.id.addCategoryDescription)).perform(typeText("CategoryDescription"), closeSoftKeyboard())
        onView(withId(R.id.addCategoryDoneButton)).perform(click())
        onView(withId(R.id.addNextButton)).perform(click())
    }

    private fun addPoint(mark: String, category: String) {
        addAbstractMark(mark, category)
        onView(withId(R.id.selectMarkLayout)).perform(swipeLeft(), swipeLeft(), swipeDown())
        onView(withId(R.id.markLocationSelectedButton)).perform(click())
    }

    private fun addPolygon(mark: String, category: String) {
        addAbstractMark(mark, category)
        onView(withId(R.id.selectMarkLayout)).perform(swipeLeft())
        onView(withId(R.id.addPointButton)).perform(click())
        onView(withId(R.id.selectMarkLayout)).perform(swipeDown())
        onView(withId(R.id.addPointButton)).perform(click())
        onView(withId(R.id.selectMarkLayout)).perform(swipeLeft())
        onView(withId(R.id.addPointButton)).perform(click())
        onView(withId(R.id.markLocationSelectedButton)).perform(click())
    }

    private fun testInputLayoutEmptiness(testMark: Boolean) {
        val nextButton = if (testMark) R.id.addNextButton else R.id.addCategoryDoneButton
        val addNameLayout = if (testMark) R.id.addMarkNameLayout else R.id.addCategoryNameLayout
        val addName = if (testMark) R.id.addMarkName else R.id.addCategoryName
        val emptyNameError = "Название ${if (testMark) "метки" else "категории"} не должно быть пустым"
        val addDescriptionLayout = if (testMark) R.id.addMarkDescriptionLayout else R.id.addCategoryDescriptionLayout
        val addDescription = if (testMark) R.id.addMarkDescription else R.id.addCategoryDescription
        val emptyDescriptionError = "Описание ${if (testMark) "метки" else "категории"} не должно быть пустым"

        onView(withId(nextButton)).perform(click())
        onView(withId(addNameLayout)).check(matches(hasTextInputLayoutErrorText(emptyNameError)))
        onView(withId(addDescriptionLayout)).check(matches(hasTextInputLayoutErrorText(emptyDescriptionError)))

        onView(withId(addDescription)).perform(typeText("description"), closeSoftKeyboard())
        onView(withId(nextButton)).perform(click())
        onView(withId(addNameLayout)).check(matches(hasTextInputLayoutErrorText(emptyNameError)))
        onView(withId(addDescriptionLayout)).check(matches(not(hasTextInputLayoutErrorText(emptyDescriptionError))))

        onView(withId(addDescription)).perform(clearText(), closeSoftKeyboard())
        onView(withId(addName)).perform(typeText("name"), closeSoftKeyboard())
        onView(withId(nextButton)).perform(click())
        onView(withId(addNameLayout)).check(matches(not(hasTextInputLayoutErrorText(emptyNameError))))
        onView(withId(addDescriptionLayout)).check(matches(hasTextInputLayoutErrorText(emptyDescriptionError)))
    }

    private fun hasTextInputLayoutErrorText(expectedErrorText: String) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {}

        override fun matchesSafely(view: View): Boolean {
            if (view !is TextInputLayout) {
                return false
            }
            val errorMessage = view.error ?: return false
            return expectedErrorText == errorMessage
        }
    }
}
