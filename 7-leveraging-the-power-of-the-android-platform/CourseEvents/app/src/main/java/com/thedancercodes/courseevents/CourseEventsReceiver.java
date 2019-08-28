package com.thedancercodes.courseevents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CourseEventsReceiver extends BroadcastReceiver {

    // Constants. They are exact ones we use when we sent the intent from NoteKeeper app.
    public static final String ACTION_COURSE_EVENT = "com.thedancercodes.notekeeper.action.COURSE_EVENT";

    public static final String EXTRA_COURSE_ID = "com.thedancercodes.notekeeper.extra.COURSE_ID";
    public static final String EXTRA_COURSE_MESSAGE = "com.thedancercodes.notekeeper.extra.COURSE_MESSAGE";

    // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    @Override
    public void onReceive(Context context, Intent intent) {

        // Verify that the received intent conforms to the expected characteristics.
        if (ACTION_COURSE_EVENT.equals(intent.getAction())) {

            String courseId = intent.getStringExtra(EXTRA_COURSE_ID);
            String courseMessage = intent.getStringExtra(EXTRA_COURSE_MESSAGE);
        }
    }
}
