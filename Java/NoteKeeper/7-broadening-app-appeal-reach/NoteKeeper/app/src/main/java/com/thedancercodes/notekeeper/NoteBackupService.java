package com.thedancercodes.notekeeper;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class NoteBackupService extends IntentService {

    public static final String EXTRA_COURSE_ID = "com.thedancercodes.notekeeper.extra.COURSE_ID";

    public NoteBackupService() {
        super("NoteBackupService");
    }

    // This is run on a background thread
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            // Store the value of the COURSE_ID
            String backupCourseId = intent.getStringExtra(EXTRA_COURSE_ID);

            // Perform the Backup
            NoteBackup.doBackup(this, backupCourseId);

        }
    }
}
