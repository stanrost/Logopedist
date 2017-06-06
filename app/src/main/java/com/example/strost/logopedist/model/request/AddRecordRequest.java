package com.example.strost.logopedist.model.request;

import android.media.MediaPlayer;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.File;

/**
 * Created by strost on 5-4-2017.
 */

public class AddRecordRequest {

    public void AddRecord(String file, String path) throws Exception {

        File newfile = new File(file);

        Backendless.Files.upload(newfile,
                path,
                new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
            }

            @Override
            public void handleFault(BackendlessFault fault) {
            }
        });
    }

}
