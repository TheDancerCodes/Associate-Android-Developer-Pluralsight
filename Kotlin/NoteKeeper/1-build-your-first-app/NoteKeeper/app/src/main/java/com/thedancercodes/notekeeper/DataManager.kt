package com.thedancercodes.notekeeper

/**
 * Class that serves as central point of management for instances of the CourseInfo & NoteInfo classes.
 */

class DataManager {

    //
    /**
     * Property to hold our collection of courses.
     *
     * Use HasMap -> This collection makes it easy to look up courses by the courseId (String).
     *
     * Constructs new instance of our HashMap.
     *
     * The HashMap maps String values to instances of our CourseInfo class.
     *
     * A reference of that HashMap is assigned to our courses property.
     */
    val courses = HashMap<String, CourseInfo>()


    /**
     * Property to hold our collection of notes.
     *
     * We don't need any kind of value-based look ups. We will use ArrayList Collection.
     *
     * ArrayList is a simple collection that can grow dynamically & provides index-based access to its members.
     *
     * ArrayList is a generic type; the type we want to store is NoteInfo.
     *
     * ArrayList that holds NoteInfo references. We assign the ArrayList reference to our notes property.
     */
    val notes = ArrayList<Noteinfo>()


    /**
     * Initializer Block: Run the initializeCourses() function whenever an instance of
     * the DataManager class is created.
     *
     * Automatically populate our collection of courses.
     */

    //
    init {
        initializeCourses()
    }

    // Function that will create instances of our CourseInfo class and place them into the HashMap.
    // This function initializes our courses collection with four courses.
    private fun initializeCourses() {

        // Create a Course instance
        var course = CourseInfo("android_intents", "Android Programming with Intents")

        // Add the course to our courses collection using the HashMap set() method.
        courses.set(course.courseId, course)

        // NOTE: We can explicitly identify which parameter we want to associate with a value
        // by using named parameters.
        course = CourseInfo(courseId = "android_async", title = "Android Async Programming and Services")
        courses.set(course.courseId, course)

        // Demonstrating that with name parameters, the order we list the parameters doesn't matter
        course = CourseInfo(title = "Java Fundamentals: The Java Language", courseId = "java_lang")
        courses.set(course.courseId, course)

        // Going back to using positional parameters
        course = CourseInfo("java_core", "Java Fundamentals: The Core Platform")
        courses.set(course.courseId, course)
    }
}