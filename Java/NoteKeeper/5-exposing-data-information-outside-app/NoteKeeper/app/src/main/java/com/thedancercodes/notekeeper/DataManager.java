package com.thedancercodes.notekeeper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * DataManager is a Singleton: There's only one instance of DataManager in the entire app.
 */
public class DataManager {
    private static DataManager ourInstance = null;

    private List<CourseInfo> mCourses = new ArrayList<>();
    private List<NoteInfo> mNotes = new ArrayList<>();

    public static DataManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataManager();

            // loads up list of courses & notes from transient data in-memory
            // ourInstance.initializeCourses();
            // ourInstance.initializeExampleNotes();
        }
        return ourInstance;
    }

    // Method that explicitly loads list of courses & notes from DB
    public static void loadFromDatabase(NoteKeeperOpenHelper dbHelper) {

        // Read DB and assign to a local variable
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Array of Course table columns
        String[] courseColumns = {
                CourseInfoEntry.COLUMN_COURSE_ID,
                CourseInfoEntry.COLUMN_COURSE_TITLE};

        // Querying CourseInfo Table
        Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns,
                null, null, null, null, CourseInfoEntry.COLUMN_COURSE_TITLE + " DESC");

        loadCoursesFromDatabase(courseCursor);

        // Array of Note table columns
        String[] noteColumns = {
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_NOTE_TEXT,
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry._ID};

        // Local string variable to enable sorting by 2 columns
        String noteOrderBy = NoteInfoEntry.COLUMN_COURSE_ID + "," + NoteInfoEntry.COLUMN_NOTE_TITLE;

        // Querying NoteInfo Table
        Cursor noteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns,
                null, null, null, null, noteOrderBy);

        loadNotesFromDatabase(noteCursor);
    }

    // Get values of the columns within the notes_info table
    private static void loadNotesFromDatabase(Cursor cursor) {

        // Get column positions
        int noteTitlePos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        int noteTextPos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        int courseIdPos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        int idPos = cursor.getColumnIndex(NoteInfoEntry._ID);

        // Get reference to Data Manager singleton & clearing out the notes list
        DataManager dm = getInstance();
        dm.mNotes.clear();

        // Use while loop to move through results row by row by calling moveToNext()
        while (cursor.moveToNext()) {

            // Get values for each of the columns
            String noteTitle = cursor.getString(noteTitlePos);
            String noteText = cursor.getString(noteTextPos);
            String courseId = cursor.getString(courseIdPos);
            int id = cursor.getInt(idPos);

            // Get Course the corresponds to a particular Note
            CourseInfo noteCourse = dm.getCourse(courseId);

            // Instance of NoteInfo class & add it to Notes List
            NoteInfo note = new NoteInfo(id, noteCourse, noteTitle, noteText);
            dm.mNotes.add(note);
        }

        // Close cursor when done
        cursor.close();
    }

    // Get values of the columns within the course_info table
    private static void loadCoursesFromDatabase(Cursor cursor) {

        /* Get column values based on column position. */

        // Get position of course_id & course_title columns
        int courseIdPos = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
        int courseTitlePos = cursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);

        // Load courses into the lists contained in the DataManager Singleton
        DataManager dm = getInstance(); // Reference to the Singleton

        // Clear out any courses that might have been loaded
        dm.mCourses.clear();

        // Use while loop, to loop though the rows to access the column values.
        while (cursor.moveToNext()) {

            // Get values for course_id & course_title columns
            String courseId = cursor.getString(courseIdPos);
            String courseTitle = cursor.getString(courseTitlePos);

            // new CourseInfo instance containing id & title form current row
            CourseInfo course = new CourseInfo(courseId, courseTitle, null);

            // Add to Courses list
            dm.mCourses.add(course);
        }

        // Close the Cursor when done
        cursor.close();
    }

    public String getCurrentUserName() {
        return "Roger Taracha";
    }

    public String getCurrentUserEmail() {
        return "thedancercodes@gmail.com";
    }

    public List<NoteInfo> getNotes() {
        return mNotes;
    }

    public int createNewNote() {
        NoteInfo note = new NoteInfo(null, null, null);
        mNotes.add(note);
        return mNotes.size() - 1;
    }

    public int findNote(NoteInfo note) {
        for (int index = 0; index < mNotes.size(); index++) {
            if (note.equals(mNotes.get(index)))
                return index;
        }

        return -1;
    }

    public void removeNote(int index) {
        mNotes.remove(index);
    }

    /**
     * The result of this call is what we associate with our spinner.
     *
     * @return A list of courses
     */
    public List<CourseInfo> getCourses() {
        return mCourses;
    }

    public CourseInfo getCourse(String id) {
        for (CourseInfo course : mCourses) {
            if (id.equals(course.getCourseId()))
                return course;
        }
        return null;
    }

    public List<NoteInfo> getNotes(CourseInfo course) {
        ArrayList<NoteInfo> notes = new ArrayList<>();
        for (NoteInfo note : mNotes) {
            if (course.equals(note.getCourse()))
                notes.add(note);
        }
        return notes;
    }

    public int getNoteCount(CourseInfo course) {
        int count = 0;
        for (NoteInfo note : mNotes) {
            if (course.equals(note.getCourse()))
                count++;
        }
        return count;
    }

    private DataManager() {
    }

    //region Initialization code

    private void initializeCourses() {
        mCourses.add(initializeCourse1());
        mCourses.add(initializeCourse2());
        mCourses.add(initializeCourse3());
        mCourses.add(initializeCourse4());
    }

    public void initializeExampleNotes() {
        final DataManager dm = getInstance();

        CourseInfo course = dm.getCourse("android_intents");
        course.getModule("android_intents_m01").setComplete(true);
        course.getModule("android_intents_m02").setComplete(true);
        course.getModule("android_intents_m03").setComplete(true);
        mNotes.add(new NoteInfo(course, "Dynamic intent resolution",
                "Wow, intents allow components to be resolved at runtime"));
        mNotes.add(new NoteInfo(course, "Delegating intents",
                "PendingIntents are powerful; they delegate much more than just a component invocation"));

        course = dm.getCourse("android_async");
        course.getModule("android_async_m01").setComplete(true);
        course.getModule("android_async_m02").setComplete(true);
        mNotes.add(new NoteInfo(course, "Service default threads",
                "Did you know that by default an Android Service will tie up the UI thread?"));
        mNotes.add(new NoteInfo(course, "Long running operations",
                "Foreground Services can be tied to a notification icon"));

        course = dm.getCourse("java_lang");
        course.getModule("java_lang_m01").setComplete(true);
        course.getModule("java_lang_m02").setComplete(true);
        course.getModule("java_lang_m03").setComplete(true);
        course.getModule("java_lang_m04").setComplete(true);
        course.getModule("java_lang_m05").setComplete(true);
        course.getModule("java_lang_m06").setComplete(true);
        course.getModule("java_lang_m07").setComplete(true);
        mNotes.add(new NoteInfo(course, "Parameters",
                "Leverage variable-length parameter lists"));
        mNotes.add(new NoteInfo(course, "Anonymous classes",
                "Anonymous classes simplify implementing one-use types"));

        course = dm.getCourse("java_core");
        course.getModule("java_core_m01").setComplete(true);
        course.getModule("java_core_m02").setComplete(true);
        course.getModule("java_core_m03").setComplete(true);
        mNotes.add(new NoteInfo(course, "Compiler options",
                "The -jar option isn't compatible with with the -cp option"));
        mNotes.add(new NoteInfo(course, "Serialization",
                "Remember to include SerialVersionUID to assure version compatibility"));
    }

    private CourseInfo initializeCourse1() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("android_intents_m01", "Android Late Binding and Intents"));
        modules.add(new ModuleInfo("android_intents_m02", "Component activation with intents"));
        modules.add(new ModuleInfo("android_intents_m03", "Delegation and Callbacks through PendingIntents"));
        modules.add(new ModuleInfo("android_intents_m04", "IntentFilter data tests"));
        modules.add(new ModuleInfo("android_intents_m05", "Working with Platform Features Through Intents"));

        return new CourseInfo("android_intents", "Android Programming with Intents", modules);
    }

    private CourseInfo initializeCourse2() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("android_async_m01", "Challenges to a responsive user experience"));
        modules.add(new ModuleInfo("android_async_m02", "Implementing long-running operations as a service"));
        modules.add(new ModuleInfo("android_async_m03", "Service lifecycle management"));
        modules.add(new ModuleInfo("android_async_m04", "Interacting with services"));

        return new CourseInfo("android_async", "Android Async Programming and Services", modules);
    }

    private CourseInfo initializeCourse3() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("java_lang_m01", "Introduction and Setting up Your Environment"));
        modules.add(new ModuleInfo("java_lang_m02", "Creating a Simple App"));
        modules.add(new ModuleInfo("java_lang_m03", "Variables, Data Types, and Math Operators"));
        modules.add(new ModuleInfo("java_lang_m04", "Conditional Logic, Looping, and Arrays"));
        modules.add(new ModuleInfo("java_lang_m05", "Representing Complex Types with Classes"));
        modules.add(new ModuleInfo("java_lang_m06", "Class Initializers and Constructors"));
        modules.add(new ModuleInfo("java_lang_m07", "A Closer Look at Parameters"));
        modules.add(new ModuleInfo("java_lang_m08", "Class Inheritance"));
        modules.add(new ModuleInfo("java_lang_m09", "More About Data Types"));
        modules.add(new ModuleInfo("java_lang_m10", "Exceptions and Error Handling"));
        modules.add(new ModuleInfo("java_lang_m11", "Working with Packages"));
        modules.add(new ModuleInfo("java_lang_m12", "Creating Abstract Relationships with Interfaces"));
        modules.add(new ModuleInfo("java_lang_m13", "Static Members, Nested Types, and Anonymous Classes"));

        return new CourseInfo("java_lang", "Java Fundamentals: The Java Language", modules);
    }

    private CourseInfo initializeCourse4() {
        List<ModuleInfo> modules = new ArrayList<>();
        modules.add(new ModuleInfo("java_core_m01", "Introduction"));
        modules.add(new ModuleInfo("java_core_m02", "Input and Output with Streams and Files"));
        modules.add(new ModuleInfo("java_core_m03", "String Formatting and Regular Expressions"));
        modules.add(new ModuleInfo("java_core_m04", "Working with Collections"));
        modules.add(new ModuleInfo("java_core_m05", "Controlling App Execution and Environment"));
        modules.add(new ModuleInfo("java_core_m06", "Capturing Application Activity with the Java Log System"));
        modules.add(new ModuleInfo("java_core_m07", "Multithreading and Concurrency"));
        modules.add(new ModuleInfo("java_core_m08", "Runtime Type Information and Reflection"));
        modules.add(new ModuleInfo("java_core_m09", "Adding Type Metadata with Annotations"));
        modules.add(new ModuleInfo("java_core_m10", "Persisting Objects with Serialization"));

        return new CourseInfo("java_core", "Java Fundamentals: The Core Platform", modules);
    }

    public int createNewNote(CourseInfo course, String noteTitle, String noteText) {

        // Create the spot for the note
        int index = createNewNote();

        // Get the note back
        NoteInfo note = getNotes().get(index);

        // Set all the values in that note
        note.setCourse(course);
        note.setTitle(noteTitle);
        note.setText(noteText);

        return index;
    }
    //endregion

}
