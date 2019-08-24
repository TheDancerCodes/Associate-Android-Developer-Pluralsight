package com.thedancercodes.notekeeper;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteKeeperProviderContract {

    // Private constructor to ensure no-one can create an instance of this class
    private NoteKeeperProviderContract(){}

    // Content Provider Authority Constant
    public static final String AUTHORITY = "com.thedancercodes.notekeeper.provider";

    // Content Provider Base URI Constant
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    // Interface for constant for the column that identifies a Course
    protected interface CoursesIdColumns {
        public static final String COLUMN_COURSE_ID = "course_id";
    }

    // Interface for constant for the column that describes a Course
    protected interface CoursesColumns {

        // String constants of columns available from the Courses table
        public static final String COLUMN_COURSE_TITLE = "course_title";
    }

    // Interface for constant for the column that describes a Note
    protected interface NotesColumns {

        // String constants of columns available from the Notes table
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";
    }

    /* Nested classes for the each of the tables that we expose with this content provider. */

    // Courses Table Class
    public static final class Courses implements BaseColumns, CoursesColumns, CoursesIdColumns {

        public static final String PATH = "courses";

        /**
         * CONTENT_URI - used to access our content providers Courses table.
         *
         * content://com.thedancercodes.notekeeper.provider/courses
         */

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }

    // Notes Table Class
    public static final class Notes implements BaseColumns, NotesColumns, CoursesIdColumns, CoursesColumns {
        public static final String PATH = "notes";

        /**
         * CONTENT_URI - used to access our content providers Notes table.
         *
         * content://com.thedancercodes.notekeeper.provider/notes
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);

        // New content provider table that provides the list of notes along with each notes
        // corresponding course information by joining the note_info table to the course_info table.
        public static final String PATH_EXPANDED = "notes_expanded";

        /**
         * CONTENT_URI - used to access our content providers Courses + Notes table.
         *
         * content://com.thedancercodes.notekeeper.provider/notes_expanded
         */
        public static final Uri CONTENT_EXPANDED_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH_EXPANDED);
    }
}
