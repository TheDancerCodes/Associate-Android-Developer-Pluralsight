package com.thedancercodes.notekeeper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
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

    private static final String MIME_VENDOR_TYPE = "vnd." + NoteKeeperProviderContract.AUTHORITY + ".";
    private NoteKeeperOpenHelper dbOpenHelper;

    // URIMatcher field
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int COURSES = 0;
    public static final int NOTES = 1;
    public static final int NOTES_EXPANDED = 2;
    public static final int NOTES_ROW = 3;
    private static final int COURSES_ROW = 4;
    private static final int NOTES_EXPANDED_ROW = 5;

    /*
       * Static Initializer: allows to run some code when a type is initially loaded.

       * We often use them to initialise a static field.
    */
    static {

        // Add Courses & Notes URI's to our sUriMatcher
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Courses.PATH, COURSES);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH, NOTES);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Courses.PATH + "/#", COURSES_ROW);
        // Row URI support that references specific rows in our notes table
        // Notes.PATH + "/#" -> This is how we indicate that we want to handle a Row URI that
        // references a rowId within our notes table.
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH + "/#", NOTES_ROW);

        // Add new Content Provider Table for Courses + Notes
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH_EXPANDED, NOTES_EXPANDED);
        sUriMatcher.addURI(NoteKeeperProviderContract.AUTHORITY, Notes.PATH_EXPANDED + "/#", NOTES_EXPANDED_ROW);
    }

    public NoteKeeperProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);
        switch(uriMatch) {
            case COURSES:
                nRows = db.delete(CourseInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES:
                nRows = db.delete(NoteInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES_EXPANDED:
                // throw exception saying that this is a read-only table
            case COURSES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = CourseInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.delete(CourseInfoEntry.TABLE_NAME, rowSelection, rowSelectionArgs);
                break;
            case NOTES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = NoteInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.delete(NoteInfoEntry.TABLE_NAME, rowSelection, rowSelectionArgs);
                break;
            case NOTES_EXPANDED_ROW:
                // throw exception saying that this is a read-only table
                break;
        }
        return nRows;
    }

    @Override
    public String getType(Uri uri) {
        // Handle requests for the MIME type of the data of each of the URIs supported by the
        // Content Provider.
        String mimeType = null;

        // Identify the URI received
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case COURSES:
                // vnd.android.cursor.dir/vnd.com.thedancercodes.notekeeper.provider.courses
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        MIME_VENDOR_TYPE + Courses.PATH;
                break;

            case NOTES:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        MIME_VENDOR_TYPE + Notes.PATH;
                break;

            case NOTES_EXPANDED:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        MIME_VENDOR_TYPE + Notes.PATH_EXPANDED;
                break;

            case NOTES_ROW:
                // vnd.android.cursor.dir/vnd.com.thedancercodes.notekeeper.provider.courses
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                        MIME_VENDOR_TYPE + Notes.PATH;
                break;
        }


        return mimeType;
    }

    // Content Provider insert() method
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /* Implement this to handle requests to insert a new row. */

        // DB reference
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        // DB insert method returns back a rowId of the new row, so we initialize a rowId with -1.
        long rowId = -1;

        // The ContentProvider insert method returns back a URI that identifies the new inserted row
        Uri rowUri = null;

        // Constant for the table that corresponds to the URI & our local variable uriMatch
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {

            /* Case for each of the tables */

            case NOTES:
                // DB insert
                rowId = db.insert(NoteInfoEntry.TABLE_NAME, null, values);

                // URI for the new row: (TABLE_URI + rowId)
                // content://com.thedancercodes.notekeeper.provider/notes/1
                rowUri = ContentUris.withAppendedId(Notes.CONTENT_URI, rowId);
                break;

            case COURSES:
                rowId = db.insert(CourseInfoEntry.TABLE_NAME, null, values);

                // URI for the new row: (TABLE_URI + rowId)
                // content://com.thedancercodes.notekeeper.provider/courses/1
                rowUri = ContentUris.withAppendedId(Courses.CONTENT_URI, rowId);
                break;

            case NOTES_EXPANDED:
                // Throw exception saying that this is a read-only table.
                break;
        }

        return rowUri;

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
                break;

            case NOTES_ROW:

                // Query DB NoteInfo table for a specific row by specifying a selection criteria
                // that includes the rowId value contained in our URI.
                long rowId = ContentUris.parseId(uri);

                // String value holding the selection clause
                String rowSelection = NoteInfoEntry._ID + " = ?";

                String[] rowSelectionArgs = new String[]{Long.toString(rowId)};

                // Query: Assign cursor variable teh result of a call db.query() method
                cursor = db.query(NoteInfoEntry.TABLE_NAME, projection, rowSelection,
                        rowSelectionArgs, null, null, null);

                break;
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
        // Implement this to handle requests to update one or more rows.
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);
        switch(uriMatch) {
            case COURSES:
                nRows = db.update(CourseInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES:
                nRows = db.update(NoteInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES_EXPANDED:
                // throw exception saying that this is a read-only table
            case COURSES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = CourseInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.update(CourseInfoEntry.TABLE_NAME, values, rowSelection, rowSelectionArgs);
                break;
            case NOTES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = NoteInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.update(NoteInfoEntry.TABLE_NAME, values, rowSelection, rowSelectionArgs);
                break;
            case NOTES_EXPANDED_ROW:
                // throw exception saying that this is a read-only table
                break;
        }

        return nRows;
    }
}
