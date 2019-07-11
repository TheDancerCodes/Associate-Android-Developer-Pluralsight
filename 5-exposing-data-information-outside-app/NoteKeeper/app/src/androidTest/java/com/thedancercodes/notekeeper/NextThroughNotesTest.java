package com.thedancercodes.notekeeper;

import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

public class NextThroughNotesTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule(MainActivity.class);

    /**
     * Verify we can select a note from MainActivity & successfully next through the notes
     * within the NoteActivity.
     */
    @Test
    public void NextThroughNotes() {

        // Reference to DrawerLayout View & open the navigation drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Reference to NavigationView under DrawerLayout & select the navigation option for notes
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes));

        // Reference to RecyclerView & select first note within it.
        // Result of this is to open up the NoteActivity.
        onView(withId(R.id.list_items)).perform(RecyclerViewActions
                .<NoteRecyclerAdapter.ViewHolder>actionOnItemAtPosition(0, click()));

        /* Verify that we have the values we expect in the NoteActivity */

        // Reference to NotesList from our Manager
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        // for loop: walks through the full ranges of indexes for the note list
        for(int index = 0; index < notes.size(); index++) {

            // Get first note from the Note List
            NoteInfo note = notes.get(index);

            /* Walk through each of the UI fields on the NoteActivity & verify that the Spinner
               and TextViews have the right text for this note in them. */
            onView(withId(R.id.spinner_courses)).check(
                    matches(withSpinnerText(note.getCourse().getTitle())));

            onView(withId(R.id.text_note_title)).check(matches(withText(note.getTitle())));

            onView(withId(R.id.text_note_text)).check(matches(withText(note.getText())));

            // Reference to Next menu item
            // NOTE: This test fails on devices that don't contain the next button on the app bar.

            // Verify that button is enabled as lona as we are not at the last index
            if (index < notes.size() -1)
                onView(allOf(withId(R.id.action_next), isEnabled())).perform(click());
        }

        // Verify button is disabled when we get to the last item in the loop
        onView(withId(R.id.action_next)).check(matches(not(isEnabled())));

        // Return back to MainActivity
        pressBack();
    }

}