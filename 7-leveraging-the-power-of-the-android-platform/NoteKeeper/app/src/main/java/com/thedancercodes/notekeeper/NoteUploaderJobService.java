package com.thedancercodes.notekeeper;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PersistableBundle;

public class NoteUploaderJobService extends JobService {

    public static final String EXTRA_DATA_URI = "com.thedancercodes.notekeeper.extras.DATA_URI";
    private NoteUploader noteUploader;


    public NoteUploaderJobService() {
    }

    /**
     * Called to indicate that the job has begun executing.
     */
    @Override
    public boolean onStartJob(JobParameters params) {

        AsyncTask<JobParameters, Void, Void> task = new AsyncTask<JobParameters, Void, Void>() {
            @Override
            protected Void doInBackground(JobParameters... backgroundParams) {

                JobParameters jobParams = backgroundParams[0];

                String stringDataUri = jobParams.getExtras().getString(EXTRA_DATA_URI);

                Uri dataUri = Uri.parse(stringDataUri);

                noteUploader.doUpload(dataUri);

                // Conditional for when noteUploader isn't cancelled
                if (!noteUploader.isCanceled())
                    jobFinished(jobParams, false);

                return null;
            }
        };

        noteUploader = new NoteUploader( this);

        // Get the background work running
        task.execute(params);

        // This lets our JobScheduler know that the process needs to be allowed to keep running
        // until our background work finishes.
        return true;
    }

    /**
     * This method is called if the system has determined that you must stop execution of your job
     * even before you've had a chance to call {@link #jobFinished(JobParameters, boolean)}.
     */
    @Override
    public boolean onStopJob(JobParameters params) {

        // Stop note uploading work
        noteUploader.cancel();

        // This lets our JobScheduler know that the work needs to be rescheduled.
        return true;
    }

}
