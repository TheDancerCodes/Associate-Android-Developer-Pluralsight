package com.thedancercodes.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName(); // Ensures we always have correct class name.

    /**
     * Constant to be used in Extras by Intents.
     * <p>
     * This Activity is the destination of that Extra.
     * <p>
     * Remember to qualify the constant with your package name to ensure it is unique.
     */
    public static final String NOTE_POSITION = "com.thedancercodes.notekeeper.NOTE_POSITION";

    /**
     * Declare constants for the Activity instance state items we want to preserve.
     */
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.thedancercodes.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.thedancercodes.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.thedancercodes.notekeeper.ORIGINAL_NOTE_TEXT";

    // Value the position will have if the intent Extra is not set
    public static final int POSITION_NOT_SET = -1;

    private NoteInfo mNote;
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int notePosition;
    private boolean isCancelling;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Reference to our spinner
        spinnerCourses = findViewById(R.id.spinner_courses);

        // Get the list of courses
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        // Adapter that associates the list of courses with the Spinner
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        courses); // formats selected item in the spinner.

        // Resource to format dropdown list of courses.
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Associate the Adapter with the Spinner.
        spinnerCourses.setAdapter(adapterCourses);

        // Read contents of the Intent
        readDisplayStateValues();

        /*
            After we call, readDisplayStateValues(),
            that’s a good time for us to preserve the original values of the note.

            Conditional: if savedInstanceState is null, we save the original note values,
             else we restore the original note values.

         */
        if (savedInstanceState == null) {
            saveOriginalNoteValues();
        } else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        // Reference to the Edit Texts in the Activity
        textNoteTitle = findViewById(R.id.text_note_title);
        textNoteText = findViewById(R.id.text_note_text);

        // If we select a note, we display that note BUT if it is a new note, (no note passed),
        // we don't display the note.
        if (!isNewNote)
            displayNote(spinnerCourses, textNoteTitle, textNoteText);

        // Debug Message
        Log.d(TAG, "onCreate");
    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {

        // Get values that we put into the instance state back out.
        originalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        originalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        originalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);

    }

    private void saveOriginalNoteValues() {

        // Check whether it is anew note and do nothing
        if (isNewNote)
            return;

        // Save each of the values of the course
        originalNoteCourseId = mNote.getCourse().getCourseId();
        originalNoteTitle = mNote.getTitle();
        originalNoteText = mNote.getText();
    }

    /**
     * This method is triggered when the user moves away from this screen.
     * <p>
     * Calls saveNote() and takes all the values available on screen and puts them into our Note
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Conditional to check whether or not a user is cancelling out of an activity &
        // whether it is a new note.
        if (isCancelling) {

            // Keep track of when a user cancels
            Log.i(TAG, "Cancelling note at position: " + notePosition);

            if (isNewNote) {
                // Remove note from backing store if user is cancelling out.
                DataManager.getInstance().removeNote(notePosition);
            }
            // If a user cancels, explicitly store original values back
            else {
                storePreviousNoteValues();
            }

        } else {
            saveNote();
        }

        // Debug Message
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Put values we want to store
        outState.putString(ORIGINAL_NOTE_COURSE_ID, originalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, originalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, originalNoteText);
    }

    private void storePreviousNoteValues() {

        // Set the original values back into the note.
        CourseInfo course = DataManager.getInstance().getCourse(originalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(originalNoteTitle);
        mNote.setText(originalNoteText);
    }

    private void saveNote() {

        // Set each of the values of the note currently selected in the spinner.
        mNote.setCourse((CourseInfo) spinnerCourses.getSelectedItem());

        // Get values of text fields
        mNote.setTitle(textNoteTitle.getText().toString());
        mNote.setText(textNoteText.getText().toString());
    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {

        // Get list of courses from DataManager
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        // Get index of selected note course from the list
        int courseIndex = courses.indexOf(mNote.getCourse());

        // Pass in index to spinner to set the selection.
        spinnerCourses.setSelection(courseIndex);

        // Take the Note member variable, mNote, and set each of the values.
        textNoteTitle.setText(mNote.getTitle());
        textNoteText.setText(mNote.getText());
    }

    // Method that reads the contents of the Intent
    private void readDisplayStateValues() {
        Intent intent = getIntent(); // Reference to intent used to start this activity

        // Get the Extra containing the position from it.
        notePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);

        // Add a boolean to determine whether we are creating a new note
        // or passing in an existing note based on the existence or absence of a position.
        isNewNote = notePosition == POSITION_NOT_SET;

        // Create a new note
        if (isNewNote) {
            createNewNote();
        }

        // Write informational message each time the NoteActivity is started,
        // with the position being started for.
        Log.i(TAG, "notePosition: " + notePosition);

        // Get a note with the position whether its a new note or not.
        mNote = DataManager.getInstance().getNotes().get(notePosition);
    }

    private void createNewNote() {

        // Reference to the DataManager
        DataManager dm = DataManager.getInstance();

        // Give the position of newly created note
        notePosition = dm.createNewNote();

        // Get note at that position and assign it to the field mNote.
        // mNote = dm.getNotes().get(notePosition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        }
        // When a user exits activity, don't save changes.
        else if (id == R.id.action_cancel) {
            isCancelling = true;
            finish(); // Closes current Activity & returns to previous Activity.
        }
        // Move to the next note selection within the NoteActivity
        else if (id == R.id.action_next) {
            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Prepare the Screen's standard options menu to be displayed.  This is
     * called right before the menu is shown, every time it is shown.  You can
     * use this method to efficiently enable/disable items or otherwise
     * dynamically modify the contents.
     *
     * <p>The default implementation updates the system menu items based on the
     * activity's state.  Deriving classes should always call through to the
     * base class implementation.
     *
     * @param menu The options menu as last shown or first initialized by
     *             onCreateOptionsMenu().
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // Get reference to the Next MenuItem
        MenuItem item = menu.findItem(R.id.action_next);

        // Determine index of last note in the list
        int lastNoteIndex = DataManager.getInstance().getNotes().size() - 1;

        // Use last note index information to enable/ disable the next menu item
        item.setEnabled(notePosition < lastNoteIndex);

        return super.onPrepareOptionsMenu(menu);
    }

    // Method called when the user selects the menu option Next.
    // The user moves from the current note they are viewing to the next note that comes after it.
    private void moveNext() {

        // Save any changes made in current note before moving to the next note
        saveNote();

        // Increment the note position
        ++notePosition;

        // Get note that corresponds to that position
        mNote = DataManager.getInstance().getNotes().get(notePosition);

        // Save original values of the note we navigated to
        saveOriginalNoteValues();

        // Display note into the Views currently displayed by the NoteActivity
        displayNote(spinnerCourses, textNoteTitle, textNoteText);

        // We use this method to access the onPrepareOptionsMenu at runtime.
        invalidateOptionsMenu();
    }

    // Method to send an email using an implicit intent
    private void sendEmail() {

        // Get the course information
        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();

        // Local variable for subject of email
        String subject = textNoteTitle.getText().toString();

        // Local variable for body of email
        String text = "Check out what I learned in the PluralSight course \"" +
                course.getTitle() + "\"n" + textNoteText.getText().toString();

        // Implicit Intent with Action & Mime Type to send an email
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");

        // Put the title and body of our email in the intent extra
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);

        // Sending the implicit Intent
        startActivity(intent);

    }
}
