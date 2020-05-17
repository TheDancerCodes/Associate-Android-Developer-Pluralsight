package com.thedancercodes.notekeeper

/**
 * Class that serves as central point of management for instances of the CourseInfo & NoteInfo classes.
 *
 * Making this an object converts it to a singleton & we can interact with a single instance of
 * DataManager throughout the whole application.
 */

object DataManager {

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
    val notes = ArrayList<NoteInfo>()


    /**
     * Initializer Block: Run the initializeCourses() function whenever an instance of
     * the DataManager class is created.
     *
     * Automatically populate our collection of courses.
     */

    //
    init {
        initializeCourses()
        initializeNotes()
    }

    // Function to make it easy to add a new note to the DataManager's note collection & get back
    // the index of that note.
    fun addNote(courseInfo: CourseInfo, noteTitle: String, noteText: String): Int {
        return -1
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

    // Function to initialize our Notes collection
    private fun initializeNotes() {
        var course = courses["android_intents"]!!
        var note = NoteInfo(course, "Dynamic intent resolution",
            "Wow, intents allow components to be resolved at runtime")
        notes.add(note)

        note = NoteInfo(
            course, "Delegating intents",
            "PendingIntents are powerful; they delegate much more than just a component invocation")
        notes.add(note)

        course = courses["android_async"]!!
        note = NoteInfo(course, "Service default threads",
            "Did you know that by default an Android Service will tie up the UI thread?")
        notes.add(note)

        note = NoteInfo(course, "Long running operations",
            "Foreground Services can be tied to a notification icon")
        notes.add(note)

        course = courses["java_lang"]!!
        note = NoteInfo(course, "Parameters",
            "Leverage variable-length parameter lists")
        notes.add(note)

        note = NoteInfo(course, "Anonymous classes",
            "Anonymous classes simplify implementing one-use types")
        notes.add(note)

        course = courses["java_core"]!!
        note = NoteInfo(course, "Compiler options",
            "The -jar option isn't compatible with with the -cp option")
        notes.add(note)

        note = NoteInfo(course, "Serialization",
            "Remember to include SerialVersionUID to assure version compatibility")
        notes.add(note)
    }
}