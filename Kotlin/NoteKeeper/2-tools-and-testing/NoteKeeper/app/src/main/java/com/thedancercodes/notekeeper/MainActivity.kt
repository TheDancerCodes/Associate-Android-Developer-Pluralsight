package com.thedancercodes.notekeeper

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    // Private mutable note to get the note position.
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val adapterCourses = ArrayAdapter<CourseInfo>(this,
            android.R.layout.simple_spinner_item,

            // Access DataManager instance
            DataManager.courses.values.toList())

        // Specify layout resource to format the selections that are
        // displayed within our dropdown list
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Associate ArrayAdapter with Spinner.
        // Assign the adapter we created to spinnerCourses adapter property.
        spinnerCourses.adapter = adapterCourses

        // If the savedInstanceState is non-null, get the saved notePosition from the instance state.
        // If savedInstanceState is null, it will result in a null value & the elvis operator causes
        // us to get the notePosition from the extra.
        // Assign value to notePosition property.
        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
            intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        // When we receive a value for notePosition, display the note at this position.
        if (notePosition != POSITION_NOT_SET) {
            displayNote()
        }

        // Create a new mote
        else {
            createNewNote()
        }
    }

    private fun createNewNote() {
        // Edit-in-place: Create an empty note & add it to DataManager
        // NOTE: The last member of our notes collection is this newly created empty note.
        DataManager.notes.add(NoteInfo())

        // Set notePosition to last index in the DataManager notes collection
        // (position of our new note)
        notePosition = DataManager.notes.lastIndex
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        // Save notePosition state into bundle
        outState?.putInt(NOTE_POSITION, notePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_next -> {
                if (notePosition < DataManager.notes.lastIndex) {
                    moveNext()
                } else {
                    showMessage("No more notes")
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayNote() {

        if (notePosition > DataManager.notes.lastIndex) {
            val message = "Note not found"
            showMessage(message)
            return
        }

        // Get the note that corresponds to the notePosition.
        val note = DataManager.notes[notePosition]

        // Display values within the views on our activity
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        // Display appropriate course for the note in the spinner
        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)
    }

    private fun showMessage(message: String) {
        Snackbar.make(textNoteTitle, message, Snackbar.LENGTH_LONG).show()
    }

    private fun moveNext() {

        // Increase the value of notePosition & display the not
        ++notePosition
        displayNote()

        /**
         * NOTE:
         * All of our code to affect the appearance of the menu is placed within onPrepareOptionsMenu(),
         * and we call InvalidateOptionsMenu() anytime we want the code to run.
         */
        invalidateOptionsMenu()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

       // Check for the last note in the list
        if (notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)

            // Modify item state only if menuItem is not null
            if (menuItem != null) {

                // Change icon
                menuItem.icon = getDrawable(R.drawable.ic_block_white_24dp)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }

    // Automatically save changes when you leave an activity.
    override fun onPause() {
        super.onPause()
        saveNote()
    }

    // Save content from screen into the note in our DataManager.
    private fun saveNote() {

        // Get the note at the specific position
        val note = DataManager.notes[notePosition]

        // Set the values
        note.title = textNoteTitle.text.toString()
        note.text = textNoteText.text.toString()
        note.course = spinnerCourses.selectedItem as CourseInfo
    }
}
