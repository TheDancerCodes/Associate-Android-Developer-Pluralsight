package com.thedancercodes.notekeeper;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
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
import static org.hamcrest.Matchers.*;
import static android.support.test.espresso.Espresso.pressBack;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    // Static member variable that points to the DataManager
    static DataManager dataManager;

    // Initialize DataManager
    @BeforeClass
    public static void classSetUp() {
        dataManager = DataManager.getInstance();
    }

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

        // Initialize variable that points to the course we want to use in our test
        final CourseInfo course = dataManager.getCourse("java_lang");

        // Variable for note title & text
        final String noteTitle = "Test Note Title";
        final String noteText = "This is the body of our test note";

        // Local Variable to get a specific View
        // ViewInteraction fabNewNote = onView(withId(R.id.fab));

        // Perform an action on the View
        // fabNewNote.perform(click());

        // Simplified Version of the 2 steps above 👆: Launches NoteActivity
        onView(withId(R.id.fab)).perform(click());

        // Click on the spinner
        onView(withId(R.id.spinner_courses)).perform(click());

        // Make a selection from the spinner.
        // Look through the AdapterViews on this Activity, find the one that holds
        // CourseInfo instance which is equal to the course variable & select it.
        onData(allOf(instanceOf(CourseInfo.class), equalTo(course))).perform(click());

        // Get to our note title field of the NoteActivity & input text
        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle));

        // Get to our note text field of the NoteActivity & input text
        onView(withId(R.id.text_note_text))
                .perform(typeText(noteText),
                        closeSoftKeyboard());

        // Leave NoteActivity at the end of the test
        pressBack();
    }
}