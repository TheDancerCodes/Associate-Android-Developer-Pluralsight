package com.thedancercodes.notekeeper;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/* TIP: Add static imports for the Espresso, ViewMatchers & ViewActions class.

   Allow us to use any static methods from these classes as just method names.

   Makes our test code more readable.

 */
import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    /**
     * Get the activity on which to run the test/launch.
     * <p>
     * Specify the activity where the test starts.
     * <p>
     * With this rule in place, our testing environment will take care of creating that
     * activity instance for us before each test & cleaning it up after each test.
     */
    @Rule
    public ActivityTestRule<NoteListActivity> noteListActivityRule =
            new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote() {

        // Local Variable to get a specific View
        // ViewInteraction fabNewNote = onView(withId(R.id.fab));

        // Perform an action on the View
        // fabNewNote.perform(click());

        // Simplified Version of the 2 steps above 👆
        onView(withId(R.id.fab)).perform(click());

        // Get to our note title field of the NoteActivity & input text
        onView(withId(R.id.text_note_title)).perform(typeText("Test Note Title"));

        // Get to our note text field of the NoteActivity & input text
        onView(withId(R.id.text_note_text))
                .perform(typeText("This is the body of our test note"),
                        closeSoftKeyboard());
    }

}