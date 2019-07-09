package com.thedancercodes.notekeeper;

import android.provider.BaseColumns;

/**
 * Holds information about our Database
 */
public final class NoteKeeperDatabaseContract {

    private NoteKeeperDatabaseContract() {
    } // Make non-creatable (Private Constructor)

    /*
      Nested Classes for each of the tables
     */
    public static final class CourseInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_COURSE_TITLE = "course_title";

        // Takes care of the details of building a Qualified Name
        public static final String getQName(String columnName) {
            return TABLE_NAME + "." + columnName;
        }

        // Constant to create the table
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_COURSE_ID + " TEXT UNIQUE NOT NULL, " +
                        COLUMN_COURSE_TITLE + " TEXT NOT NULL)";
    }

    public static final class NoteInfoEntry implements BaseColumns {

        public static final String TABLE_NAME = "note_info";
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";
        public static final String COLUMN_COURSE_ID = "course_id";

        // Takes care of the details of building a Qualified Name
        public static final String getQName(String columnName) {
            return TABLE_NAME + "." + columnName;
        }

        // Constant to create the table
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
                        COLUMN_NOTE_TEXT + " TEXT, " +
                        COLUMN_COURSE_ID + " TEXT NOT NULL)";

    }
}
