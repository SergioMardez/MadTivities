package com.sergiom.madtivities.uitests

import android.content.Intent
import android.os.SystemClock
import android.view.View
import android.view.ViewConfiguration
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sergiom.madtivities.R
import com.sergiom.madtivities.ui.MainActivity
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class EventDetailTest {

    lateinit var scenario: ActivityScenario<MainActivity>
    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @get:Rule
    val rule = ActivityScenarioRule<MainActivity>(intent)

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun eventDetailViewIsLoaded() {
        scenario = rule.scenario

        goToEventDetail()
        Thread.sleep(1000)
        onView(withId(R.id.backButton)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
        onView(withId(R.id.map)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun clickOnBackArrowGoesToEventList() {
        scenario = rule.scenario

        goToEventDetail()
        Thread.sleep(1000)
        onView(withId(R.id.backButton)).perform(click())
        Thread.sleep(1000)
        onView(withRecyclerView(R.id.events_rv).atPosition(0)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun swipeUpOnBottomSheetShowsIt() {
        scenario = rule.scenario

        goToEventDetail()
        Thread.sleep(1000)
        onView(withId(R.id.coordinator)).perform(
            DragAction(GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER)
        )
        onView(withId(R.id.buttonSeeOnInternet)).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun swipeDownOnBottomSheetCloseIt() {
        scenario = rule.scenario

        goToEventDetail()
        Thread.sleep(1000)
        onView(withId(R.id.coordinator)).perform(
            DragAction(GeneralLocation.BOTTOM_CENTER, GeneralLocation.TOP_CENTER, Press.FINGER)
        )
        Thread.sleep(1000)
        onView(withId(R.id.coordinator)).perform(
            DragAction(GeneralLocation.CENTER, GeneralLocation.BOTTOM_CENTER, Press.FINGER)
        )
        onView(withId(R.id.buttonSeeOnInternet)).check(
            ViewAssertions.matches(not(ViewMatchers.isDisplayed()))
        )
    }

    /*
    * OTHER FUNCTIONS AND CLASSES NEEDED
    * */
    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private fun goToEventDetail() {
        Thread.sleep(1000)
        onView(withId(R.id.buttonstart)).perform(click())
        Thread.sleep(3000)
        onView(withRecyclerView(R.id.events_rv).atPosition(0)).perform(click())
    }

    /** This is like [GeneralSwipeAction], but it does not send ACTION_UP at the end.  */
    private class DragAction(
        private val mStart: CoordinatesProvider,
        private val mEnd: CoordinatesProvider,
        private val mPrecisionDescriber: PrecisionDescriber
    ) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.any(View::class.java)
        }

        override fun getDescription(): String {
            return "drag"
        }

        override fun perform(uiController: UiController, view: View?) {
            val precision = mPrecisionDescriber.describePrecision()
            val start = mStart.calculateCoordinates(view)
            val end = mEnd.calculateCoordinates(view)
            val steps = interpolate(start, end, STEPS)
            val delayBetweenMovements = DURATION / steps.size
            // Down
            val downEvent = MotionEvents.sendDown(uiController, start, precision).down
            try {
                for (i in steps.indices) {
                    // Wait
                    val desiredTime = downEvent.downTime + (delayBetweenMovements * i).toLong()
                    val timeUntilDesired = desiredTime - SystemClock.uptimeMillis()
                    if (timeUntilDesired > 10L) {
                        uiController.loopMainThreadForAtLeast(timeUntilDesired)
                    }
                    // Move
                    if (!MotionEvents.sendMovement(uiController, downEvent, steps[i])) {
                        MotionEvents.sendCancel(uiController, downEvent)
                        throw RuntimeException("Cannot drag: failed to send a move event.")
                    }
                }
                val duration = ViewConfiguration.getPressedStateDuration()
                if (duration > 0) {
                    uiController.loopMainThreadForAtLeast(duration.toLong())
                }
            } finally {
                downEvent.recycle()
            }
        }

        companion object {
            private const val STEPS = 10
            private const val DURATION = 100
            private fun interpolate(
                start: FloatArray,
                end: FloatArray,
                steps: Int
            ): Array<FloatArray> {
                if (1 >= start.size) {
                    throw IndexOutOfBoundsException(
                        "1 is outside of start's bounds [" + start.size + "]"
                    )
                }
                if (1 >= end.size) {
                    throw IndexOutOfBoundsException("1 is outside of end's bounds [" + start.size + "]")
                }
                val res = Array(steps) {
                    FloatArray(
                        2
                    )
                }
                for (i in 1 until steps + 1) {
                    res[i - 1][0] =
                        start[0] + (end[0] - start[0]) * i.toFloat() / (steps.toFloat() + 2.0f)
                    res[i - 1][1] =
                        start[1] + (end[1] - start[1]) * i.toFloat() / (steps.toFloat() + 2.0f)
                }
                return res
            }
        }
    }
}