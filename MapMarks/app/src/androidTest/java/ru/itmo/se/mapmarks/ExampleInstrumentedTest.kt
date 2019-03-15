package ru.itmo.se.mapmarks

import android.app.Instrumentation
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.test.InstrumentationRegistry
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.core.AllOf.allOf

import org.junit.runner.RunWith

import android.view.KeyEvent.KEYCODE_ENTER
import android.support.test.espresso.action.ViewActions.typeText
import android.widget.EditText
import android.support.test.espresso.Espresso.onView
import android.view.KeyEvent
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.*
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.rule.ServiceTestRule
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.view.Display
import android.view.Display.STATE_ON
import android.view.View
import ru.itmo.se.mapmarks.data.category.Category
import ru.itmo.se.mapmarks.prototype.DummyMarkInfoContainer
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.matcher.ViewMatchers
import android.util.Log
import android.view.KeyEvent.KEYCODE_K
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import junit.framework.Assert.assertEquals
import org.hamcrest.Matchers
import org.junit.*
import ru.itmo.se.mapmarks.data.mark.Mark
import ru.itmo.se.mapmarks.data.mark.point.PointMark


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainScreenActivity::class.java)

    @Test
    fun showMarkOnMapAfterAddingAndAutocomplete() {
        addMark("autocompete", "category1")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().context)
        onView(withId(R.id.mainScreenSearch)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(typeText("au"))
        val uiDevice = UiDevice.getInstance(getInstrumentation())
        uiDevice.pressEnter()
        uiDevice.pressKeyCode(KeyEvent.KEYCODE_T)
        Thread.sleep(500)
        uiDevice.pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN)
        uiDevice.pressEnter()
        val mMarker1 = uiDevice.findObject(UiSelector().descriptionContains("autocompete"))
        mMarker1.click()
    }

    private fun addMark(mark: String, category: String) {
        onView(withId(R.id.addMarkButtonMain)).perform(click())
        onView(withId(R.id.addName)).perform(typeText(mark))
        onView(withId(R.id.addDescription)).perform(typeText("MarkDescripsion"))
        onView(withId(R.id.addSelectSpinner)).perform(click())
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Новая категория..."))).perform(click())
        onView(withId(R.id.addName)).perform(typeText(category))
        onView(withId(R.id.addDescription)).perform(typeText("CategoryDescripsion"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.addNextButton)).perform(click())
        onView(withId(R.id.addNextButton)).perform(click())
    }
}
