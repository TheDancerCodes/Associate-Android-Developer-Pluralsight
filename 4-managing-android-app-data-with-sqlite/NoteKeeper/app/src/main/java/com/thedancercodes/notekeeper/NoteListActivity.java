package com.thedancercodes.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.List;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter noteRecyclerAdapter;

    // Comment out the ListView Adapter
    // private ArrayAdapter<NoteInfo> adapterNotes;

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
         *
         *  This refreshes our data set.
         */
        // adapterNotes.notifyDataSetChanged();
        noteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {

        // Reference to RecyclerView
        final RecyclerView recyclerNotes = findViewById(R.id.list_notes);

        // Create instance of LayoutManager
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);

        // Set the LayoutManager to RecyclerView
        recyclerNotes.setLayoutManager(notesLayoutManager);

        // Get notes to display within RecyclerView
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        // Create instance of NoteRecyclerAdapter
        noteRecyclerAdapter = new NoteRecyclerAdapter(this, null);

        // Associate NoteRecyclerAdapter with the RecyclerView
        recyclerNotes.setAdapter(noteRecyclerAdapter);

    }

}
