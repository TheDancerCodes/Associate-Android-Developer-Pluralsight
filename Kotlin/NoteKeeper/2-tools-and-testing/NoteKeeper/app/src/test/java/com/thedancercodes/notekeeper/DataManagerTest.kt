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

    @Test
    fun findSimilarNotes() {
        val course = DataManager.courses.get("android_async")!!
        val noteTitle = "This is test note"
        val noteText1 = "This is the body of my test note"
        val noteText2 = "This is the body of my second test note"

        // Add 2 notes
        val index1 = DataManager.addNote(course, noteTitle, noteText1)
        val index2 = DataManager.addNote(course, noteTitle, noteText2)

        val note1 = DataManager.findNote(course, noteTitle, noteText1)
        val foundIndex1 = DataManager.notes.indexOf(note1)
        assertEquals(index1, foundIndex1)

        val note2 = DataManager.findNote(course, noteTitle, noteText2)
        val foundIndex2 = DataManager.notes.indexOf(note2)
        assertEquals(index2, foundIndex2)




    }
}