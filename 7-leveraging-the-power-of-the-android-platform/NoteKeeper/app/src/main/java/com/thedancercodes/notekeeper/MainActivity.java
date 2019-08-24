package com.thedancercodes.notekeeper;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.thedancercodes.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

import static com.thedancercodes.notekeeper.NoteKeeperProviderContract.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_NOTES = 0;
    // Fields
    private NoteRecyclerAdapter noteRecyclerAdapter;
    private RecyclerView recyclerItems;
    private LinearLayoutManager notesLayoutManager;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    private GridLayoutManager coursesLayoutManager;
    private NoteKeeperOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enableStrictMode();

        // Create instance of NoteKeeperOpenHelper & assign it to member field
        dbOpenHelper = new NoteKeeperOpenHelper(this);

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

        updateNavHeader();
    }

    private void enableStrictMode() {

        // Enable StrictMode Policy for DEBUG builds.
        if (BuildConfig.DEBUG) {

            // Create the Policy
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();

            // Set the Policy
            StrictMode.setThreadPolicy(policy);


        }
    }


    @Override
    protected void onDestroy() {

        // Close NoteKeeperOpenHelper when activity is destroyed
        dbOpenHelper.close();
        super.onDestroy();
    }

    // Called each time we return to an Activity
    @Override
    protected void onResume() {
        super.onResume();

        // Using restartLoader instead of initLoader, assures that we re-query the DB each time
        // our onResume() method get called.
        getLoaderManager().restartLoader(LOADER_NOTES, null, this);

        /*
           Update values in Nav Drawer every time onResume gets called.
         */
        updateNavHeader();
    }

    private void loadNotes() {

        /* Query the note_info table in the DB to get back a list of notes. */

        // Read DB and assign to a local variable
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        // Array of Note table columns
        String[] noteColumns = {
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry._ID};

        // Local string variable to enable sorting by 2 columns
        String noteOrderBy = NoteInfoEntry.COLUMN_COURSE_ID + "," + NoteInfoEntry.COLUMN_NOTE_TITLE;

        // Querying NoteInfo Table
        final Cursor noteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns,
                null, null, null, null, noteOrderBy);

        // Associate the Cursor with our NoteRecyclerAdapter
        noteRecyclerAdapter.changeCursor(noteCursor);
    }

    private void updateNavHeader() {

        // Reference to our Navigation View
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Reference to the Navigation View Header
        View headerView = navigationView.getHeaderView(0);

        // Reference to TextViews
        TextView textUserName = headerView.findViewById(R.id.text_user_name);
        TextView textEmailAddress = headerView.findViewById(R.id.text_email_address);

        /*
           Interact with Preference System to get the values
         */

        // Reference to SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get the values
        String userName = preferences.getString("user_display_name", "");
        String emailAddress = preferences.getString("user_email_address", "");

        // Set values to the TextViews
        textUserName.setText(userName);
        textEmailAddress.setText(emailAddress);
    }

    private void initializeDisplayContent() {

        // Call loadFromDatabase method
        DataManager.loadFromDatabase(dbOpenHelper);

        // Reference to RecyclerView
        recyclerItems = findViewById(R.id.list_items);

        // Instance of NotesLayoutManager
        notesLayoutManager = new LinearLayoutManager(this);

        // Instance of CoursesLayoutManager
        coursesLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.course_grid_span)); // Get column number from a resource file

        // Create instance of NoteRecyclerAdapter
        noteRecyclerAdapter = new NoteRecyclerAdapter(this, null);

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

            //Launch Settings Activity
            startActivity(new Intent(this, SettingsActivity.class));
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
            // handleSelection(R.string.nav_share_message);
            handleShare();
        } else if (id == R.id.nav_send) {
            handleSelection(R.string.nav_send_message);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //
    private void handleShare() {

        // Get reference to RecyclerView to display the SnackBar
        View view = findViewById(R.id.list_items);
        Snackbar.make(view, "Share to - " +
                PreferenceManager.getDefaultSharedPreferences(this)
                        .getString("user_favorite_social", ""),
                Snackbar.LENGTH_LONG).show();

    }

    // Helper Function
    private void handleSelection(int message_id) {

        // Get reference to RecyclerView to display the SnackBar
        View view = findViewById(R.id.list_items);
        Snackbar.make(view, message_id, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader  = null;

        if (id == LOADER_NOTES) {
            // List of return columns; includes values that come from note_info table
            // along with the course title from the course_info table that corresponds
            // to each note
            final String[] noteColumns = {
                    Notes._ID,
                    Notes.COLUMN_NOTE_TITLE,
                    Notes.COLUMN_COURSE_TITLE
            };

            // Sort primarily by Course title and then within it the Note Title
            final String noteOrderBy = Notes.COLUMN_COURSE_TITLE +
                    "," + Notes.COLUMN_NOTE_TITLE;

            // Create CursorLoader that accesses our  NOTES_EXPANDED_URI
            loader = new CursorLoader(this, Notes.CONTENT_EXPANDED_URI, noteColumns,
                    null, null, noteOrderBy);

        }
        return loader;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See {@link FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     *
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     *
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link Cursor}
     * and you place it in a {@link CursorAdapter}, use
     * the {@link CursorAdapter#CursorAdapter(Context,
     * Cursor, int)} constructor <em>without</em> passing
     * in either {@link CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link Cursor} from a {@link CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link CursorAdapter}, you should use the
     * {@link CursorAdapter#swapCursor(Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader loader, Cursor data) {

        if (loader.getId() == LOADER_NOTES) {
            noteRecyclerAdapter.changeCursor(data);
        }
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader loader) {

        if (loader.getId() == LOADER_NOTES) {
            noteRecyclerAdapter.changeCursor(null);
        }

    }
}
