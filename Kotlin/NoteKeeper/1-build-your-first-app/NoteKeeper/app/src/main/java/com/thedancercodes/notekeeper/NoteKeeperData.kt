package com.thedancercodes.notekeeper

// Class that represents a PluralSight Course
class CourseInfo (val courseId: String, val title: String)

// Class that represents a Note
class Noteinfo (var course: CourseInfo, var title: String, var text: String)