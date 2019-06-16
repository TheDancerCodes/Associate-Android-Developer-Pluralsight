package com.thedancercodes.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private ArrayAdapter<NoteInfo> adapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Use this FAB to create a new note
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create an intent and start activity
                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));
            }
        });

        // Populate the ListView
        initializeDisplayContent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
         *  Let the ArrayAdapter know that the data has changed.
         *
         *  Each time our NoteListActivity moves into the foreground, we're telling it to go ahead
         *
         *  & get prepared for the latest list of notes that we have.
         */
        adapterNotes.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {

        // Reference to ListView
        final ListView listNotes = findViewById(R.id.list_notes);

        // Get list of notes to add into the ListView
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        // Add the notes into ListView using an ArrayAdapter
        adapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        // Associate our Adapter with the ListView
        listNotes.setAdapter(adapterNotes);

        // Handle ListView Selection
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * This method is called each time a user makes a selection from the ListView
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Create an intent that identifies the activity we want to start.
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);

                // Get NoteInfo that corresponds to user selection
                // NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);

                // Put Note Position as an Extra in the Intent
                intent.putExtra(NoteActivity.NOTE_POSITION, position);


                // Launch the Activity
                startActivity(intent);

            }
        });

    }

}
