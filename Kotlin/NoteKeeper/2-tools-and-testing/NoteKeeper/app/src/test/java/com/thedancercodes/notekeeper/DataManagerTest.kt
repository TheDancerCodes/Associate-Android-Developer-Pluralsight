package com.thedancercodes.notekeeper

import org.junit.Test

import org.junit.Assert.*

class DataManagerTest {

    @Test
    fun addNote() {

        // Variables to create a new note
        val course = DataManager.courses.get("android_async")!!
        val noteTitle = "This is test note"
        val noteText = "This is the body of my text note"

        // Index value returned when we create a new note
        val index = DataManager.addNote(course, noteTitle, noteText)

        // Get the note at that index
        val note = DataManager.notes[index]

        // Check that the note we get back has the values in it that we expect
        assertEquals(course, note.course)
        assertEquals(noteTitle, note.title)
        assertEquals(noteText, note.text)

    }
}