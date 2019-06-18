package com.thedancercodes.notekeeper;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    @Test
    public void createNewNote() throws Exception {

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
}