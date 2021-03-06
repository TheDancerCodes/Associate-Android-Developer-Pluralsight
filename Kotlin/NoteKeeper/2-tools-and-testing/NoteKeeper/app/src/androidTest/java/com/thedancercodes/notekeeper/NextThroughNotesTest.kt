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
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.assertion.ViewAssertions.matches

@RunWith(AndroidJUnit4::class)
class NextThroughNotesTest {

    @Rule @JvmField
    val noteListActivity = ActivityTestRule(NoteListActivity::class.java)

    @Test
    fun nextThroughNotes() {

        // Get the first note in the DataManager notes collection.
        // The result launches the MainActivity displaying the first note.
        onData(allOf(instanceOf(NoteInfo::class.java), equalTo(DataManager.notes[0])))
            .perform(click())

        // Walk through all the notes in the DataManager's notes collection
        // For loop that increments the indexes of the DataManager's notes collection
        for (index in 0..DataManager.notes.lastIndex) {

            // Reference to the note at a particular index
            val note = DataManager.notes[index]

            // Verify that the correct values are displayed in the views in MainActivity
            onView(withId(R.id.spinnerCourses)).check(
                matches(withSpinnerText(note.course?.title)))

            onView(withId(R.id.textNoteTitle)).check(
                matches(withText(note.title)))

            onView(withId(R.id.textNoteText)).check(
                matches(withText(note.text)))

            // Select the next menu option as long we are not on the last note & the menu option is enabled
            if (index != DataManager.notes.lastIndex)
                onView(allOf(withId(R.id.action_next), isEnabled())).perform(click())
        }

        // Check whether menu option is still enabled when you reach the end of the list
        onView(withId(R.id.action_next)).check(matches(isEnabled()))
    }
}