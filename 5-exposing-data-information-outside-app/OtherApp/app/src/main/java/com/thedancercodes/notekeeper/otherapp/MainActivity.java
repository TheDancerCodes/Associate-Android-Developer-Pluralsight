package com.thedancercodes.notekeeper.otherapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Cursor> {

    // Constant that identifies issuing the query for courses from our NoteKeeper app
    private static final int LOADER_NOTEKEEPER_COURSES = 0;
    private SimpleCursorAdapter mCoursesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Instance of the SimpleCursorAdapter
        mCoursesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null,

                // Info necessary to populate the ListView
                new String[]{"course_title", "course_id"},
                new int[] {android.R.id.text1, android.R.id.text2}, 0);

        // Get reference to the ListView & associate an adapter to it.
        ListView listCourses = (ListView) findViewById(R.id.list_courses);
        listCourses.setAdapter(mCoursesAdapter);

        /* Initialize the process of issuing the query
         *
         * When we initialize the Loader that corresponds to the LOADER_NOTEKEEPER_COURSES constant,
         * we want the callbacks to come back to our current activity.
         * */
        getLoaderManager().initLoader(LOADER_NOTEKEEPER_COURSES, null, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {



        // Since we are getting data from a Content Provider, we need the URI of the
        // NoteKeeper app Content Provider.
        Uri uri = Uri.parse("content://com.thedancercodes.notekeeper.provider");

        // List of columns
        String[] columns = {"_id", "course_title", "course_id"};

        // Return back a new CursorLoader instance
        return new CursorLoader(this, uri, columns, null, null, "course_title");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Associate Cursor with the Adapter that populates our ListView.
        mCoursesAdapter.changeCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Closes up the Cursor
        mCoursesAdapter.changeCursor(null);

    }
}
