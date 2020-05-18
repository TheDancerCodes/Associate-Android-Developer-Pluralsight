package com.thedancercodes.notekeeper

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import android.support.test.espresso.Espresso.pressBack


@RunWith(AndroidJUnit4::class)
class CreateNewNoteTest {

    // ActivityTestRule ensures that the NoteListActivity is up & running.
    @Rule @JvmField
    val noteListActivity = ActivityTestRule(NoteListActivity::class.java)

    @Test
    fun createNewNote() {

        val course = DataManager.courses["android_async"]
        val noteTitle = "Test note title"
        val noteText = "This is the body of our test note"

        // Identify the FAB & click on it
        onView(withId(R.id.fab)).perform(click())

        // Tap the spinner to present the selections
        onView(withId(R.id.fab)).perform(click())

        // Make a selection inside the course spinner
        onData(allOf(instanceOf(CourseInfo::class.java), equalTo(course))).perform(click())

        // Add note title & text to MainActivity
        onView(withId(R.id.textNoteTitle)).perform(typeText(noteTitle))
        onView(withId(R.id.textNoteText)).perform(typeText(noteText))

        // Press the back button
        pressBack()
    }
}