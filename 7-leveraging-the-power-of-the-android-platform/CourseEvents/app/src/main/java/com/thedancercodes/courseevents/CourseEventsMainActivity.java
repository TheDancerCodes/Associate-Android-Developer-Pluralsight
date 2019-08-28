package com.thedancercodes.courseevents;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This Activity is designed to display information from the Broadcast we receive.
 */
public class CourseEventsMainActivity extends AppCompatActivity
        implements CourseEventsDisplayCallbacks {

    ArrayList<String> mCourseEvents;
    ArrayAdapter<String> mCourseEventsAdapter;
    private CourseEventsReceiver mCourseEventsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_events_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupListView();

        // Code to create the Receiver.
        setupCourseEventReceiver();
    }

    private void setupCourseEventReceiver() {

        // Create Broadcast Receiver
        mCourseEventsReceiver = new CourseEventsReceiver();

        // Associate Activity as the callback when a broadcast is received.
        mCourseEventsReceiver.setCourseEventsDisplayCallbacks(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    protected void setupListView() {
        mCourseEvents = new ArrayList<String>();
        mCourseEventsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCourseEvents);

        final ListView listView = (ListView) findViewById(R.id.list_course_events);
        listView.setAdapter(mCourseEventsAdapter);
    }

    // Calling this method adds an item to the ListView that displays a courseId and message that
    // was received from the Broadcast.
    @Override
    public void onEventReceived(String courseId, String courseMessage) {

        String displayText = courseId + ": " + courseMessage;

        mCourseEvents.add(displayText);
        mCourseEventsAdapter.notifyDataSetChanged();
    }
}
