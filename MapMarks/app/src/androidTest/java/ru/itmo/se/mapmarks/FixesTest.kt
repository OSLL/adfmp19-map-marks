package ru.itmo.se.mapmarks

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.closeSoftKeyboard
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FixesTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainScreenActivity::class.java)

    @Test
    fun testFix10() {
        val device = UiDevice.getInstance(getInstrumentation())
        onView(withId(R.id.addMarkButtonMain)).perform(click())

        onView(withId(R.id.addName)).perform(typeText("Name"))
        onView(withId(R.id.addDescription)).perform(typeText("Description"))
        closeSoftKeyboard()
        onView(withId(R.id.addNextButton)).perform(click())
        assert(device.findObject(UiSelector().descriptionContains("Google Map")).isEnabled)
    }
}