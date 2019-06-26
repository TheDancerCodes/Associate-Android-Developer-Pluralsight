package com.thedancercodes.notekeeper;

import org.junit.Rule;
import org.junit.Test;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;

import java.util.List;


import static org.junit.Assert.*;

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

        // Get DrawerLayout View & open the navigation drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

    }

}