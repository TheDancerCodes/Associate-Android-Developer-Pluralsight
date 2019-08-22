package com.thedancercodes.notekeeper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;
import com.thedancercodes.notekeeper.NoteKeeperProviderContract.Courses;
import static com.thedancercodes.notekeeper.NoteKeeperProviderContract.CoursesIdColumns;
import com.thedancercodes.notekeeper.NoteKeeperProviderContract.Notes;


public class NoteKeeperProvider extends ContentProvider {

    private NoteKeeperOpenHelper dbOpenHelper;

    // URIMatcher field
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int COURSES = 0;

    public static final int NOTES = 1;

    public static final int NOTES_EXPANDED = 2;

    /*
       * Static Initializer: allows to run some code when a type is initially loaded.

       * We often use them to initialise a static field.
    */
    static {

        // Add Courses & Notes URI's to our sUriMatcher
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Courses.PATH, COURSES);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH, NOTES);

        // Add new Content Provider Table for Courses + Notes
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH_EXPANDED, NOTES_EXPANDED);
    }

    public NoteKeeperProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Create ContentProvider
    @Override
    public boolean onCreate() {

        // Instance of NoteKeeperHelper
        dbOpenHelper = new NoteKeeperOpenHelper(getContext());

        // Indicate that ContentProvider is successfully created
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Initialize Cursor
        Cursor cursor = null;

        // SQLiteDatabase reference
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        /* Determine whether that URI passed in is for the courses table or the notes table
           using the match method of our UriMatcher field.

           * Match method returns back an appropriate integer value for that URI.
        */
        int uriMatch = sUriMatcher.match(uri);

        // Switch statement to check it
        switch (uriMatch) {
            case COURSES:
                // Issue CourseInfo Table query & assign its result back to the cursor variable.
                cursor = db.query(CourseInfoEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case NOTES:
                // Issue NoteInfo Table query & assign its result back to the cursor variable.
                cursor = db.query(NoteInfoEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case NOTES_EXPANDED:
                cursor = notesExpandedQuery(db, projection, selection, selectionArgs, sortOrder);
        }

        // Return back Cursor variable
        return cursor;
    }

    private Cursor notesExpandedQuery(SQLiteDatabase db, String[] projection, String selection,
                                      String[] selectionArgs, String sortOrder) {

        /* Code to Table qualify any columns that are requested that are part of both the
           note_info & course_info database tables. */

        // local variable columns assigned a string array that is the same size as the projection
        // length.
        // projection is a list of desired columns
        // projection is a list of desired columns
        String[] columns = new String[projection.length];

        // Loop that loops once for each member of the projection array
        for (int idx=0; idx < projection.length; idx++) {

            // Assign a value to the current element of the columns array.
            // The specific value we assign depends on the corresponding element of the projection array.
            // Check whether the current value if the projection array equals the value of the
            // BaseColumns._ID or whether the columns is the course_id column,
            // If it does, we need to table qualify the column name using NoteInfoEntry.getQName(),
            // otherwise we can simply assign the value from the projection array.
            columns[idx] = projection[idx].equals(BaseColumns._ID) ||
                    projection[idx].equals(CoursesIdColumns.COLUMN_COURSE_ID) ?
                    NoteInfoEntry.getQName(projection[idx]) : projection[idx];
        }


        // Join the note_info table to the course_info table based on the courseID values being
        // equal in the two tables.
        String tablesWithJoin = NoteInfoEntry.TABLE_NAME + " JOIN " +
                CourseInfoEntry.TABLE_NAME + " ON " +
                NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID) + " = " +
                CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID);

        // Return back the Cursor that's returned back to the called query
        return db.query(tablesWithJoin, columns, selection, selectionArgs,
                null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
