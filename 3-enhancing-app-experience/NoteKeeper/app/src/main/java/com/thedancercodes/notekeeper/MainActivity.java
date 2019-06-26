package com.thedancercodes.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Fields
    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView recyclerItems;
    private LinearLayoutManager notesLayoutManager;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    private GridLayoutManager coursesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Use this FAB to create a new note
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent and start activity
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Populate the RecyclerView
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
        noteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {

        // Reference to RecyclerView
        recyclerItems = findViewById(R.id.list_items);

        // Instance of NotesLayoutManager
        notesLayoutManager = new LinearLayoutManager(this);

        // Instance of CoursesLayoutManager
        coursesLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.course_grid_span)); // Get column number from a resource file

        // Get notes to display within RecyclerView
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        // Create instance of NoteRecyclerAdapter
        noteRecyclerAdapter = new NoteRecyclerAdapter(this, notes);

        // Get courses to display within RecyclerView
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        // Create instance of CourseRecyclerAdapter
        courseRecyclerAdapter = new CourseRecyclerAdapter(this, courses);

        displayNotes();

    }

    private void displayNotes() {

        // Set the LayoutManager to RecyclerView
        recyclerItems.setLayoutManager(notesLayoutManager);

        // Associate NoteRecyclerAdapter with the RecyclerView
        recyclerItems.setAdapter(noteRecyclerAdapter);

        /* Set selected menu item as checked, in the navigation drawer. */
        selectNavigationMenuItem(R.id.nav_notes);

    }

    private void selectNavigationMenuItem(int id) {
        // Reference to NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Reference to Menu within the NavigationView
        Menu menu = navigationView.getMenu();

        // Use menu to find the menu item we want to select and check
        menu.findItem(id).setChecked(true);
    }

    private void displayCourses() {

        // Set the LayoutManager to RecyclerView
        recyclerItems.setLayoutManager(coursesLayoutManager);

        // Associate CourseRecyclerAdapter with the RecyclerView
        recyclerItems.setAdapter(courseRecyclerAdapter);

        /* Set selected menu item as checked, in the navigation drawer. */
        selectNavigationMenuItem(R.id.nav_courses);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            displayNotes();
        } else if (id == R.id.nav_courses) {
            displayCourses();
        } else if (id == R.id.nav_share) {
            handleSelection(R.string.nav_share_message);
        } else if (id == R.id.nav_send) {
            handleSelection(R.string.nav_send_message);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Helper Function
    private void handleSelection(int message_id) {

        // Get reference to RecyclerView to display the SnackBar
        View view = findViewById(R.id.list_items);
        Snackbar.make(view, message_id, Snackbar.LENGTH_LONG).show();
    }
}
