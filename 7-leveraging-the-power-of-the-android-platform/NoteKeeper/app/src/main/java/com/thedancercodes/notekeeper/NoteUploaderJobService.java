package com.thedancercodes.notekeeper;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.os.PersistableBundle;

public class NoteUploaderJobService extends JobService {

    public static final String EXTRA_DATA_URI = "com.thedancercodes.notekeeper.extras.DATA_URI";

    public NoteUploaderJobService() {
    }

    /**
     * Called to indicate that the job has begun executing.
     */
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    /**
     * This method is called if the system has determined that you must stop execution of your job
     * even before you've had a chance to call {@link #jobFinished(JobParameters, boolean)}.
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

}
