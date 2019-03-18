package ru.itmo.se.mapmarks

import android.content.Intent
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.core.AllOf.allOf

import org.junit.runner.RunWith

import android.support.test.espresso.action.ViewActions.typeText
import android.widget.EditText
import android.support.test.espresso.Espresso.onView
import android.view.KeyEvent
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.*
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
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
    fun addMrkAndAutocompleteTest() {
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPoint("newMark", "newCategory")
        findMark("newMark")
    }

    @Test
    fun addPolygonTest() {
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPolygon("newMark1", "newCategory1")
    }


    @Test
    fun SaveToStorageTest() {
        activityRule.launchActivity(Intent().putExtra("test", true).putExtra("path", "testData"))
        addPoint("newMark2", "newCategory2")
        activityRule.activity.finish()
        activityRule.launchActivity(Intent())
        findMark("newMark2")

    }

    private fun findMark(mark: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().context)
        onView(withId(R.id.mainScreenSearch)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText(mark.substring(0, 2)))
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        uiDevice.pressEnter()
        val symb = mark[2]
        val keyCode = if (symb.isDigit()) symb.toInt() - 23 else symb.toInt() - 68
        uiDevice.pressKeyCode(keyCode)
        Thread.sleep(500)
        uiDevice.pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN)
        uiDevice.pressEnter()
        val mMarker1 = uiDevice.findObject(UiSelector().descriptionContains(mark))
        mMarker1.click()
    }

    private fun addAbstractMark(mark: String, category: String) {
        onView(withId(R.id.addMarkButtonMain)).perform(click())
        onView(withId(R.id.addMarkName)).perform(typeText(mark))
        onView(withId(R.id.addMarkDescription)).perform(typeText("MarkDescripsion"))
        onView(withId(R.id.addSelectCategorySpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Новая категория..."))).perform(click())
        onView(withId(R.id.addCategoryName)).perform(typeText(category))
        onView(withId(R.id.addCategoryDescription)).perform(typeText("CategoryDescripsion"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.addCategoryDoneButton)).perform(click())
        onView(withId(R.id.addNextButton)).perform(click())
    }

    private fun addPoint(mark: String, category: String) {
        addAbstractMark(mark, category)
        onView(withId(R.id.selectMarkLayout)).perform(swipeLeft()).perform(swipeLeft()).perform(swipeDown())
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
}
