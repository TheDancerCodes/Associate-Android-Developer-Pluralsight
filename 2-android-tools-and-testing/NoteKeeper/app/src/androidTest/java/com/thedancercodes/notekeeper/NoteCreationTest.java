package com.thedancercodes.notekeeper;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    /**
     * Get the activity on which to run the test.
     * <p>
     * Specify the activity where the test starts.
     * <p>
     * With this rule in place, our testing environment will take care of creating that
     * activity instance for us before each test & cleaning it up after each test.
     */
    @Rule
    ActivityTestRule<NoteListActivity> noteListActivityRule =
            new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote() {

    }

}