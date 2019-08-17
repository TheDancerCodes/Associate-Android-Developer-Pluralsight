package com.thedancercodes.notekeeper;

import android.net.Uri;

public final class NoteKeeperProviderContract {

    // Private constructor to ensure no-one can create an instance of this class

    // Content Provider Authority Constant
    public static final String AUTHORITY = "com.thedancercodes.notekeeper.provider";

    // Content Provider Base URI Constant
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    /* Nested classes for the each of the tables that we expose with this content provider. */

    // Courses Table Class
    public static final class Courses {

        public static final String PATH = "courses";

        /**
         * CONTENT_URI - used to access our content providers Courses table.
         *
         * content://com.thedancercodes.notekeeper.provider/courses
         */

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }

    // Notes Table Class
    public static final class Notes {
        public static final String PATH = "notes";

        /**
         * CONTENT_URI - used to access our content providers Courses table.
         *
         * content://com.thedancercodes.notekeeper.provider/courses
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }
}
