package com.thedancercodes.notekeeper;

import android.content.Context;
import android.content.Intent;

public class CourseEventBroadcastHelper {

    public static final String ACTION_COURSE_EVENT = "com.thedancercodes.notekeeper.action.COURSE_EVENT";

    public static final String EXTRA_COURSE_ID = "com.thedancercodes.notekeeper.extra.COURSE_ID";
    public static final String EXTRA_COURSE_MESSAGE = "com.thedancercodes.notekeeper.extra.COURSE_MESSAGE";

    // Sends Broadcast about the app's Course event
    public static void sendEventBroadcast(Context context, String courseId, String message) {

        // Create Implicit Intent Instance & provide additional information.
        Intent intent = new Intent(ACTION_COURSE_EVENT);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        intent.putExtra(EXTRA_COURSE_MESSAGE, message);

        // Send the Broadcast
        context.sendBroadcast(intent);
    }

}
