package com.thedancercodes.notekeeper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*

class NoteListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(toolbar)

        // Launch Activity for editing notes
        fab.setOnClickListener { view ->
            val activityIntent = Intent(this, MainActivity::class.java)
            startActivity(activityIntent)
        }

        // Populate the ListView using the Adapter
        listNotes.adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            DataManager.notes)

        // Listen for note selection on ListView.
        listNotes.setOnItemClickListener{ parent, view, position, id ->

            // Create Intent
            val activityIntent = Intent(this, MainActivity::class.java)

            // Use putExtra() to pass the note position data between activities.
            activityIntent.putExtra(NOTE_POSITION, position)

            // Start Activity
            startActivity(activityIntent)

        }
    }

    // Tell our ArrayAdapter that the data has changed when our NoteListActivity becomes our
    // foreground activity.
    override fun onResume() {
        super.onResume()

        // Access adapter that populated our ListView
        (listNotes.adapter as ArrayAdapter<NoteInfo>).notifyDataSetChanged()

    }

}
