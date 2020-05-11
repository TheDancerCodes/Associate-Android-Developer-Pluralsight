package com.thedancercodes.notekeeper

/**
 * The NoteKeeperData file contains the classes we use for modelling the data in our application.
 */


// Class that represents a PluralSight Course
data class CourseInfo (val courseId: String, val title: String) {

    /**
     * Returns a string representation of the CourseInfo object.
     *
     * Return the value we would like displayed for each course (i.e.) the title.
     *
     * For each course loaded into the spinner, the course title will appear.
     */
    override fun toString(): String {
        return title
    }
}

// Class that represents a Note
// By making the NoteInfo class a data class, it will generate our toString method
// and also other methods we might need. (equals and copy)
data class Noteinfo (var course: CourseInfo? = null, var title: String? = null, var text: String? = null)