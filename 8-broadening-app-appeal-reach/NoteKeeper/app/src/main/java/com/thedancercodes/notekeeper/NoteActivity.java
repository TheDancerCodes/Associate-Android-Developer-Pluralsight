package com.thedancercodes.notekeeper;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

import static com.thedancercodes.notekeeper.NoteKeeperProviderContract.Courses;
import static com.thedancercodes.notekeeper.NoteKeeperProviderContract.Notes;

public class NoteActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_NOTES = 0;
    public static final int LOADER_COURSES = 1;
    public final String TAG = getClass().getSimpleName(); // Ensures we always have correct class name.

    /**
     * Constant to be used in Extras by Intents.
     * <p>
     * This Activity is the destination of that Extra.
     * <p>
     * Remember to qualify the constant with your package name to ensure it is unique.
     */
    public static final String NOTE_ID = "com.thedancercodes.notekeeper.NOTE_ID";

    /**
     * Declare constants for the Activity instance state items we want to preserve.
     */
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.thedancercodes.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.thedancercodes.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.thedancercodes.notekeeper.ORIGINAL_NOTE_TEXT";

    // Value the position will have if the intent Extra is not set
    public static final int ID_NOT_SET = -1;

    private NoteInfo mNote = new NoteInfo(DataManager.getInstance().getCourses().get(0), "", "");
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int noteId;
    private boolean isCancelling;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;
    private NoteKeeperOpenHelper dbOpenHelper;
    private Cursor noteCursor;
    private int mCourseIdPos;
    private int noteTextPos;
    private int noteTitlePos;
    private SimpleCursorAdapter adapterCourses;
    private boolean coursesQueryFinished;
    private boolean notesQueryFinished;
    private Uri mNoteUri;

    @Override
    protected void onDestroy() {

        // Close OpenHelper
        dbOpenHelper.close();

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "************** onCreate **************");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instance of OpenHelper class
        dbOpenHelper = new NoteKeeperOpenHelper(this);

        // Reference to our spinner
        spinnerCourses = findViewById(R.id.spinner_courses);


        // SimpleCursorAdapter that displays the current item using the Android layout resources
        // (simple_spinner_item).
        // Take the values from Cursor's course_title column & display them in the view who's id
        // is android.R.id.text1
        adapterCourses = new SimpleCursorAdapter(this,
                android.R.layout.simple_spinner_item,
                null,
                new String[] {CourseInfoEntry.COLUMN_COURSE_TITLE},
                new int[] {android.R.id.text1}, 0); // formats selected item in the spinner.

        // Resource to format dropdown list of courses.
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Associate the Adapter with the Spinner.
        spinnerCourses.setAdapter(adapterCourses);

        // Use LoadManager to start process of loading Course data from DB
        getLoaderManager().initLoader(LOADER_COURSES, null, this);

        // Read contents of the Intent & does associated initialization
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

            // Use LoadManager to start process of loading note data from DB
            getLoaderManager().initLoader(LOADER_NOTES, null, this);

        // Debug Message
        Log.d(TAG, "onCreate");
    }

    // Call to load courses from the DB.
    private void loadCourseData() {

        // Reference to SQLite DB
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        // Array of the tables' columns
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_ID,
                CourseInfoEntry._ID
        };

        // Perform DB Query to return all rows back from the table
        Cursor cursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                null, null, null, null, CourseInfoEntry.COLUMN_COURSE_TITLE);

        // Associate Cursor with Adapter.
        adapterCourses.changeCursor(cursor);
    }

    // Call to load a note from the DB.
    // Handles all the details of locating a note that matches the specified selection criteria.
    private void loadNoteData() {

        // Reference to SQLite DB
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        // Selection Criteria: Assume we want to always find the exact same note
        // that start with the name "dynamic"
        String courseId = "android_intents";
        String titleStart = "dynamic";

        // Selection Clause that uses _ID column
        String selection = NoteInfoEntry._ID + " = ? ";

        // Selection Values
        String[] selectionArgs = {Integer.toString(noteId)};

        // Array of the tables' columns
        String [] noteColumns = {
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_NOTE_TEXT
        };

        // Perform the DB Query
        noteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns,
                selection, selectionArgs, null, null, null);

        // To access the column values in the Cursor, we need the position of each of columns.
        mCourseIdPos = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        noteTitlePos = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        noteTextPos = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);

        // Position Cursor to first row in the result
        noteCursor.moveToNext();

        // Display the Note
        displayNote();

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
            Log.i(TAG, "Cancelling note at position: " + noteId);

            if (isNewNote) {
                // Remove note from DB if user is cancelling out.
                deleteNoteFromDatabase();
            }
            // If a user cancels, explicitly store original values back
            else {
                storePreviousNoteValues();
            }

        } else {
            saveNote();
        }

        // Debug Message
        Log.d(TAG, "************** onPause **************");

    }

    // Remove note with a specific ID
    private void deleteNoteFromDatabase() {

        // Selection Clause that uses _ID column
        final String selection = NoteInfoEntry._ID + " = ? ";

        // Selection Values
        final String[] selectionArgs = {Integer.toString(noteId)};

        // Initialize Async Task
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

                // Connection to DB
                SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

                // Call Delete Method: Delete the row from the note_info table that has the ID value
                // contained in our noteId field
                db.delete(NoteInfoEntry.TABLE_NAME, selection, selectionArgs);

                return null;
            }
        };

        // Execute AsyncTask
        task.execute();
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

        // Get user's current selection in the spinner & get value from the Cursor.
        String courseId = selectedCourseId();

        // Get values of text fields
        String noteTitle = textNoteTitle.getText().toString();
        String noteText = textNoteText.getText().toString();

        // Call method to save note to DB & pass in the courseId, noteTitle & noteText
        // This updates the DB with any changes the user makes to a note.
        saveNoteToDatabase(courseId, noteTitle, noteText);
    }

    // Determine which course ID corresponds to the currently selected course on the Activity
    private String selectedCourseId() {

        // Currently selected position on the spinner.
        int selectedPosition = spinnerCourses.getSelectedItemPosition();

        // Get the Cursor associated with Adapter to populate the Spinner.
        Cursor cursor = adapterCourses.getCursor();

        // Move Cursor to get the correct cursor position.
        cursor.moveToPosition(selectedPosition);

        // Index of the column that contains the course ID.
        int courseIdPos = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);

        // Get value of the course_id that corresponds to users current selection in the spinner
        String courseId = cursor.getString(courseIdPos);

        return courseId;
    }

    // Handles details of updating the note within the note_info table
    private void saveNoteToDatabase(String courseId, String noteTitle, String noteText) {

        /* Selection Criteria */
        // Selection Clause
        String selection = NoteInfoEntry._ID + " = ? ";

        // Selection Argument
        String[] selectionArgs = {Integer.toString(noteId)};

        // Column Values: Instance of ContentValues class
        ContentValues values = new ContentValues();
        values.put(NoteInfoEntry.COLUMN_COURSE_ID, courseId);
        values.put(NoteInfoEntry.COLUMN_NOTE_TITLE, noteTitle);
        values.put(NoteInfoEntry.COLUMN_NOTE_TEXT, noteText);

        // Connection to the DB
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        // Update DB
        db.update(NoteInfoEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    // Displays values for the currently selected note.
    private void displayNote() {

        // Get column values from the Cursor
        String courseId = noteCursor.getString(mCourseIdPos);
        String noteTitle = noteCursor.getString(noteTitlePos);
        String noteText = noteCursor.getString(noteTextPos);


        // Get index of selected note course from our Cursor.
        // Pass in ID of the course that was read from the DB.
        int courseIndex = getIndexOfCourseId(courseId);

        // Pass in index to spinner to set the selection.
        spinnerCourses.setSelection(courseIndex);

        // Take the Note member variable, mNote, and set each of the values.
        textNoteTitle.setText(noteTitle);
        textNoteText.setText(noteText);

        // Trigger the sending of a Broadcast
        CourseEventBroadcastHelper.sendEventBroadcast(this, courseId, "Editing Note");
    }

    private int getIndexOfCourseId(String courseId) {

        // Reference of Cursor being used to populate the Spinner.
        Cursor cursor = adapterCourses.getCursor();

        // Use Cursor to figure out correct row for the current course.
        // courseIdPos - index of the column that holds the value of the courseId
        int courseIdPos = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);

        // Initialize courseRowIndex
        int courseRowIndex = 0;

        // Walk through cursor row by row, finding the row index of thw course that corresponds to
        // the course in the note.
        boolean more = cursor.moveToFirst();

        while(more) {

            // Get courseId value for the current row.
            String cursorCourseId = cursor.getString(courseIdPos);

            // Condition that checks whether the cursorCourseId value is equal to our courseId parameter.
            if (courseId.equals(cursorCourseId))
                break;

            // Increase courseRowIndex value by 1
            courseRowIndex++;

            // Move to the next row
            more = cursor.moveToNext();
        }

        // Return the index value of the row in the course Cursor that corresponds to the course
        // for the current note.
        return courseRowIndex;
    }

    // Method that reads the contents of the Intent
    private void readDisplayStateValues() {
        Intent intent = getIntent(); // Reference to intent used to start this activity

        // Get the Extra containing the id from it.
        noteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET);

        // Add a boolean to determine whether we are creating a new note
        // or passing in an existing note based on the existence or absence of an id.
        isNewNote = noteId == ID_NOT_SET;

        // Create a new note
        if (isNewNote) {
            createNewNote();
        }

        // Write informational message each time the NoteActivity is started,
        // with the id being started for.
        Log.i(TAG, "noteId: " + noteId);

        // Get a note with the id whether its a new note or not.
        // *** NOTE: Don't need this anymore as we are reading the note from the DB. ***
        // mNote = DataManager.getInstance().getNotes().get(noteId);
    }

    private void createNewNote() {

        //* Perform the insert of our new note row using a background thread.
        //* Actual insert on a background thread, then assign the resulting URI to our mNoteUri field on the main thread.
        AsyncTask<ContentValues, Integer, Uri> task = new AsyncTask<ContentValues, Integer, Uri>() {

            // ProgressBar Field
            private ProgressBar progressBar;

            /**
             * Runs on the UI thread before {@link #doInBackground}.
             *
             * @see #onPostExecute
             * @see #doInBackground
             */
            @Override
            protected void onPreExecute() {
                progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(1);
            }

            @Override
            protected Uri doInBackground(ContentValues... contentValues) {

                Log.d(TAG, "doInBackground - thread: " + Thread.currentThread().getId());

                // To access the single content values reference we pass to the execute method,
                // we access the initial element of contentValues
                ContentValues insertValues = contentValues[0];

                // Reference to a ContentResolver that inserts into the Notes table.
                // The insert method returns back a URI for our new row.
                Uri rowUri = getContentResolver().insert(Notes.CONTENT_URI, insertValues);

                simulateLongRunningWork(); // simulate slow database work
                publishProgress(2);

                simulateLongRunningWork(); // simulate slow database work
                publishProgress(3);

                return rowUri;
            }

            /**
             * Runs on the UI thread after {@link #publishProgress} is invoked.
             * The specified values are the values passed to {@link #publishProgress}.
             *
             * @param values The values indicating progress.
             * @see #publishProgress
             * @see #doInBackground
             */
            @Override
            protected void onProgressUpdate(Integer... values) {

                // Take the value onProgressUpdate receives & pass it to our ProgressBar’s setProgress method.
                int progressValue = values[0];
                progressBar.setProgress(progressValue);
            }

            @Override
            protected void onPostExecute(Uri uri) {

                Log.d(TAG, "onPostExecute - thread: " + Thread.currentThread().getId());

                // Assign the rowUri returned from doInBackground to mNoteUri field.
                mNoteUri = uri;

                displaySnackBar(mNoteUri.toString());

                progressBar.setVisibility(View.GONE);
            }
        };

        // New instance of ContentValues
        ContentValues values = new ContentValues();

        // Specify values for each individual column we want to put into that row.
        // Put placeholders in each column, as an empty string
        values.put(Notes.COLUMN_COURSE_ID, "");
        values.put(Notes.COLUMN_NOTE_TITLE, "");
        values.put(Notes.COLUMN_NOTE_TEXT, "");

        Log.d(TAG, "Call to execute - thread: " + Thread.currentThread().getId());
        task.execute(values);
    }

    private void simulateLongRunningWork() {
        try {
            Thread.sleep(2000);
        } catch(Exception ex) {}
    }

    private void displaySnackBar(String message) {
        View view = findViewById(R.id.spinner_courses);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
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
        // Show Reminder Notification
        else if (id == R.id.action_set_reminder) {
            showReminderNotification();
        }

        return super.onOptionsItemSelected(item);
    }

    // Code that displays a Notification
    private void showReminderNotification() {

        // Current Note Title
        String noteTitle = textNoteTitle.getText().toString();

        // Current Note Text
        String noteText = textNoteText.getText().toString();

        // Extract current noteId from URI
        int noteId = (int) ContentUris.parseId(mNoteUri);

        /* Use the Alarm Manager to schedule a call to our broadcast receiver */

        // Create the Explicit Intent
        Intent intent = new Intent(this, NoteReminderReceiver.class);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_TITLE, noteTitle);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_TEXT, noteText);
        intent.putExtra(NoteReminderReceiver.EXTRA_NOTE_ID, noteId);

        // Create the Pending Intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Reference to Alarm Manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /* Calculate time we want to set the Alarm to. Setting a relative time. */

        // Get the time in milliseconds from when the device was last rebooted.
        long currentTimeInMilliseconds = SystemClock.elapsedRealtime();

        // One hour in milliseconds
        long ONE_HOUR = 60 * 60 * 1000;

        // 10 seconds in milliseconds
        long TEN_SECONDS = 10 * 1000;

        long alarmTime = currentTimeInMilliseconds + TEN_SECONDS;

        alarmManager.set(AlarmManager.ELAPSED_REALTIME, alarmTime,pendingIntent);
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }



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
        item.setEnabled(noteId < lastNoteIndex);

        return super.onPrepareOptionsMenu(menu);
    }

    // Method called when the user selects the menu option Next.
    // The user moves from the current note they are viewing to the next note that comes after it.
    private void moveNext() {

        // Save any changes made in current note before moving to the next note
        saveNote();

        // Increment the note position
        ++noteId;

        // Get note that corresponds to that position
        mNote = DataManager.getInstance().getNotes().get(noteId);

        // Save original values of the note we navigated to
        saveOriginalNoteValues();

        // Display note into the Views currently displayed by the NoteActivity
        displayNote();

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

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * <p>This will always be called from the process's main thread.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        // Initialize Cursor Loader
        CursorLoader loader = null;

        // Check whether ID value passed as a parameter to onCreateLoader has the value LOADER_NOTES
        if (id == LOADER_NOTES)
            loader = createLoaderNotes();
        else if (id == LOADER_COURSES)
            loader = createLoaderCourses();
        return loader;
    }

    // Return a CursorLoader instance that knows how to load up our course data.
    // This where our app request data from courses data from our content provider.
    private CursorLoader createLoaderCourses() {

        // This field is false before we start our query for courses
        coursesQueryFinished = false;

        /* Query to Content Provider */

        // Define Content Provider URI variable
        Uri uri = Courses.CONTENT_URI;

        // Identify columns we want back from the Content Provider.
        String[] courseColumns = {
                Courses.COLUMN_COURSE_TITLE,
                Courses.COLUMN_COURSE_ID,
                Courses._ID
        };

        // Create new instance of the CursorLoader
        return new CursorLoader(this, uri, courseColumns,
                null, null, Courses.COLUMN_COURSE_TITLE);
    }

    // Return a CursorLoader instance that knows how to load up our note data.
    private CursorLoader createLoaderNotes() {

        // This field is false before we start our query for notes
        notesQueryFinished = false;

        // Array of the tables' columns
        String [] noteColumns = {
                Notes.COLUMN_COURSE_ID,
                Notes.COLUMN_NOTE_TITLE,
                Notes.COLUMN_NOTE_TEXT
        };


        // URI that references the rowId of specified note in our notes table
        // (TABLE_URI + rowId)
        mNoteUri = ContentUris.withAppendedId(Notes.CONTENT_URI, noteId);

        // return a new CursorLoader Instance
        return new CursorLoader(this, mNoteUri, noteColumns, null, null, null);
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     *
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     *
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * <p>This will always be called from the process's main thread.
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        // Check whether loader value passed as parameter to onLoadFinished has the value LOADER_NOTES
        if (loader.getId() == LOADER_NOTES)

            // Display note returned to that Cursor
            loadFinishedNotes(data);

        // Check whether loader value passed as parameter to onLoadFinished has the value LOADER_COURSES
        else if (loader.getId() == LOADER_COURSES)

            // Associate Cursor passed to onLoadFinished() with CursorAdapter
            adapterCourses.changeCursor(data);

            // The field is true when we finish our query of courses
            coursesQueryFinished = true;

        // Display the note when both notes & courses queries are finished
            displayNoteWhenQueriesFinished();
    }

    private void loadFinishedNotes(Cursor data) {

        // Take Cursor & assign it to member field noteCursor
        noteCursor = data;

        // To access the column values in the Cursor, we need the position of each of the columns.
        mCourseIdPos = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        noteTitlePos = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        noteTextPos = noteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);

        // Position Cursor to first row in the result
        noteCursor.moveToFirst();

        // The field is true when we finish our query of notes
        notesQueryFinished = true;

        // Display the note when both notes & courses queries are finished
        displayNoteWhenQueriesFinished();
    }

    private void displayNoteWhenQueriesFinished() {

        // Conditional
        if (notesQueryFinished && coursesQueryFinished)

            // Display the Note
            displayNote();
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * <p>This will always be called from the process's main thread.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        // Check that loader corresponds to LOADER_NOTES
        if (loader.getId() == LOADER_NOTES) {

            // Defensive Coding: Check that NoteCursor is not null
            if (noteCursor != null)

                // Close the Cursor
                noteCursor.close();
        }
        // Check that loader corresponds to LOADER_NOTES
        else if (loader.getId() == LOADER_COURSES) {

            // Close the Cursor
            adapterCourses.changeCursor(null);
        }
    }
}
