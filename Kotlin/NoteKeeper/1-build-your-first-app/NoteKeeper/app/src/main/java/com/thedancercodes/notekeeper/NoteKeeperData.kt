package com.thedancercodes.notekeeper

// Class that represents a PluralSight Course
class CourseInfo (val courseId: String, val title: String) {

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
class Noteinfo (var course: CourseInfo, var title: String, var text: String)