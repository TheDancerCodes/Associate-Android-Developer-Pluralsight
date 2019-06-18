package com.thedancercodes.notekeeper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    /**
     * This method runs before each test in this class
     *
     * Makes sure list of notes always starts in a consistent state
     */
    @Before
    public void setUp() {
        DataManager dm = DataManager.getInstance();
        dm.getNotes().clear();
        dm.initializeExampleNotes();
    }

    @Test
    public void createNewNote() {

        // Local variable of DataManager instance
        DataManager dm = DataManager.getInstance();

        // Set up values we want in the note
        final CourseInfo course = dm.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "This is the body text of my test note";

        // Create a new note. Take the index that's returned & assign it to a local variable
        int noteIndex = dm.createNewNote();

        // Get the note associated with the index
        NoteInfo newNote = dm.getNotes().get(noteIndex);

        // Since the course is empty, set the course, title & text
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        // Go back to the DataManager and get a note at the defined index
        NoteInfo compareNote = dm.getNotes().get(noteIndex);

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

        // Reference to DataManager
        final DataManager dm = DataManager.getInstance();

        // Initialize values for the 2 notes
        final CourseInfo course = dm.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText1 = "This is the body text of my test note";
        final String noteText2 = "This is the body text of my second test note";

        // Create the 1st note
        int noteIndex1 = dm.createNewNote();
        NoteInfo newNote1 = dm.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        // Create the 2nd note
        int noteIndex2 = dm.createNewNote();
        NoteInfo newNote2 = dm.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);

        // Find 1st note & confirm we get back the index of where we put the 1st note
        int foundIndex1 = dm.findNote(newNote1);
        assertEquals(noteIndex1, foundIndex1);

        // Find 2nd note & confirm we get back the index of where we put the 2nd note
        int foundIndex2 = dm.findNote(newNote2);
        assertEquals(noteIndex2, foundIndex2);
    }
}