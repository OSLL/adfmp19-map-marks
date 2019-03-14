package ru.itmo.se.mapmarks

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
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
    fun testFix17() {
        val markInfoSheetView = onView(withId(R.id.mark_info_sheet))
        val editButtonView = onView(withId(R.id.editButton))

        markInfoSheetView.check(matches(not(isDisplayed())))
        editButtonView.check(matches(isEnabled()))
        val device = UiDevice.getInstance(getInstrumentation())

        val mapSelector = UiSelector()
            .descriptionContains("Google Map")

        val mark = device.findObject(mapSelector
            .childSelector(UiSelector().instance(1)))

        mark.waitForExists(5000)
        mark.clickAndWaitForNewWindow(5000)
        markInfoSheetView.check(matches(isDisplayed()))
        editButtonView.check(matches(isEnabled()))

        device.findObject(mapSelector).clickAndWaitForNewWindow(5000)
        markInfoSheetView.check(matches(not(isDisplayed())))
        editButtonView.check(matches(not(isEnabled())))
    }
}