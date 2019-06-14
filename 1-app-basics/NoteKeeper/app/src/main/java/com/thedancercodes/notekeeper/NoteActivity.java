package com.thedancercodes.notekeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    /**
     * Constant to be used in Extras by Intents.
     *
     * This Activity is the destination of that Extra.
     *
     * Remember to qualify the constant with your package name to ensure it is unique.
     *
     */
    public static final String NOTE_INFO = "com.thedancercodes.notekeeper.NOTE_INFO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Reference to our spinner
        Spinner spinnerCourses = findViewById(R.id.spinner_courses);

        // Get the list of courses
        List<CourseInfo> courses = DataManager.getInstance().getCourses();

        // Adapter that associates the list of courses with the Spinner
        ArrayAdapter<CourseInfo> adapterCourses =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        courses); // formats selected item in the spinner.

        // Resource to format dropdown list of courses.
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Associate the Adapter with the Spinner.
        spinnerCourses.setAdapter(adapterCourses);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
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
}
