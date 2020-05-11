package com.thedancercodes.notekeeper

import android.os.Bundle
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

        // Get note position from intent that started MainActivity &
        // assign value to notePosition property
        notePosition = intent.getIntExtra(EXTRA_NOTE_POSITION, POSITION_NOT_SET)

        // When we receive a value for notePosition, display the note at this position.
        if (notePosition != POSITION_NOT_SET) {
            displayNote()
        }
    }

    private fun displayNote() {

        // Get the note that corresponds to the notePosition.
        val note = DataManager.notes[notePosition]

        // Display values within the views on our activity
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        // Display appropriate course for the note in the spinner
        val coursePosition = DataManager.courses.values.indexOf(note.course)
        spinnerCourses.setSelection(coursePosition)
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}
