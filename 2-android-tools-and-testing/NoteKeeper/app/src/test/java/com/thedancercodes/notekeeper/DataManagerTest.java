package com.thedancercodes.notekeeper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    // Static member of the class
    static DataManager dataManager;

    @BeforeClass
    public static void classSetUp() {
        dataManager = DataManager.getInstance();
    }

    /**
     * This method runs before each test in this class
     *
     * Makes sure list of notes always starts in a consistent state
     */
    @Before
    public void setUp() { ;
        dataManager.getNotes().clear();
        dataManager.initializeExampleNotes();
    }

    @Test
    public void createNewNote() {

        // Set up values we want in the note
        final CourseInfo course = dataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body text of my test note";

        // Create a new note. Take the index that's returned & assign it to a local variable
        int noteIndex = dataManager.createNewNote();

        // Get the note associated with the index
        NoteInfo newNote = dataManager.getNotes().get(noteIndex);

        // Since the course is empty, set the course, title & text
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        // Go back to the DataManager and get a note at the defined index
        NoteInfo compareNote = dataManager.getNotes().get(noteIndex);

        // GOAL: Make sure compareNote contains the things that we have put into the note
        // at that spot.
        assertEquals(course, compareNote.getCourse());
        assertEquals(noteTitle, compareNote.getTitle());
        assertEquals(noteText, compareNote.getText());

    }

    /**
     * Very that our findNote behaves correctly in an edge case
     */
    @Test
    public void findSimilarNotes() {

        // Initialize values for the 2 notes
        final CourseInfo course = dataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText1 = "This is the body text of my test note";
        final String noteText2 = "This is the body text of my second test note";

        // Create the 1st note
        int noteIndex1 = dataManager.createNewNote();
        NoteInfo newNote1 = dataManager.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        // Create the 2nd note
        int noteIndex2 = dataManager.createNewNote();
        NoteInfo newNote2 = dataManager.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);

        // Find 1st note & confirm we get back the index of where we put the 1st note
        int foundIndex1 = dataManager.findNote(newNote1);
        assertEquals(noteIndex1, foundIndex1);

        // Find 2nd note & confirm we get back the index of where we put the 2nd note
        int foundIndex2 = dataManager.findNote(newNote2);
        assertEquals(noteIndex2, foundIndex2);
    }
}