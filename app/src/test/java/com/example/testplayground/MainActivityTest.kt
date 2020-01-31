package com.example.testplayground

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityTest {

    private lateinit var app: MyApp

    @Before
    fun init() {
        app = getApplicationContext()
    }

    @Test
    fun useAppContext() {
        assertEquals("com.example.testplayground", app.packageName)
    }

    @Test
    fun intentTest() {
        val controller = launchActivity<MainActivity>()
        controller.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
        controller.onActivity {
            it.fab.performClick()
            val expectedIntent = Intent(it, AnotherActivity::class.java)
            val actual: Intent = shadowOf(app).nextStartedActivity
            assertEquals(expectedIntent.component, actual.component)
        }
    }

    @Test
    fun typeTest() {
        val controller = launchActivity<MainActivity>()
        onView(withId(R.id.et_name)).perform(typeText("Myo Lwin Oo"))
        controller.recreate()
        onView(withId(R.id.et_name)).check(matches(withText("Myo Lwin Oo")))
    }
}