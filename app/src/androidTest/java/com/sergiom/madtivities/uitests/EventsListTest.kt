package com.sergiom.madtivities.uitests

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.sergiom.madtivities.MainApplication
import com.sergiom.madtivities.R
import com.sergiom.madtivities.ui.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EventsListTest {

    lateinit var scenario: ActivityScenario<MainActivity>
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @get:Rule
    val rule = ActivityScenarioRule<MainActivity>(intent)

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun recyclerViewIsLoaded() {
        scenario = rule.scenario

        goToEvents()
        Thread.sleep(3000)
        Espresso.onView(withRecyclerView(R.id.events_rv).atPosition(0)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    /*
    * OTHER FUNCTIONS AND CLASSES NEEDED
    * */
    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private fun goToEvents() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.buttonstart)).perform(ViewActions.click())
    }
}