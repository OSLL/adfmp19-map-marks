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
import android.widget.TextView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.Espresso.onView
import android.view.View
import org.hamcrest.Matcher


@RunWith(AndroidJUnit4::class)
class FixesTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainScreenActivity::class.java)

    @Test
    fun testFix8() {
        val device = UiDevice.getInstance(getInstrumentation())

        val mark = device.findObject(UiSelector()
            .descriptionContains("Google Map")
            .childSelector(UiSelector().instance(1)))

        mark.waitForExists(5000)
        mark.clickAndWaitForNewWindow(5000)

        val markName = getText(withId(R.id.markName))
        val markDescription = getText(withId(R.id.markInfoDescription))

        onView(withId(R.id.editButton)).perform(click())
        onView(withId(R.id.addName)).check(matches(withText(markName)))
        onView(withId(R.id.addDescription)).check(matches(withText(markDescription)))
    }

    private fun getText(matcher: Matcher<View>): String {
        val stringHolder = arrayOf("")
        onView(matcher).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String {
                return "getting text from a TextView"
            }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView //Save, because of check in getConstraints()
                stringHolder[0] = tv.text.toString()
            }
        })
        return stringHolder[0]
    }
}