package com.thedancercodes.notekeeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NoteReminderReceiver extends BroadcastReceiver {

    // Extras for the values we want to display in the notification.
    public static final String EXTRA_NOTE_TITLE = "com.thedancercodes.notekeeper.extra.NOTE_TITLE";
    public static final String EXTRA_NOTE_TEXT = "com.thedancercodes.notekeeper.extra.NOTE_TEXT";
    public static final String EXTRA_NOTE_ID = "com.thedancercodes.notekeeper.extra.NOTE_ID";

    // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    @Override
    public void onReceive(Context context, Intent intent) {

        // Use the Broadcast intent to access our 3 extras & assign them to local variables.
        String noteTitle = intent.getStringExtra(EXTRA_NOTE_TITLE);
        String noteText = intent.getStringExtra(EXTRA_NOTE_TEXT);
        int noteId = intent.getIntExtra(EXTRA_NOTE_ID, 0);

        // Display the Notification
        NoteReminderNotification.notify(context, noteTitle, noteText, noteId);
    }
}
