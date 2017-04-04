package com.example.strost.logopedist.model.request;


import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by strost on 3-3-2017.
 */

public class AddPictureRequest {


    public void AddPicture(Bitmap file, String path, String title, final NotificationManager mNotifyMgr) throws Exception {

        //Log.e("foto", file.getPath());


        Log.e("waar moet je naartoe", path);


        Backendless.Files.Android.upload( file,
                Bitmap.CompressFormat.PNG,
                100,
                title + ".png",
                "media",
                new AsyncCallback<BackendlessFile>() {
            @Override
            public void handleResponse(BackendlessFile response) {
               Log.e("hij doet het", "JJJJJJAAAAAAAAAAAAAa") ;
                mNotifyMgr.cancel(001);
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("hij doet het niet", fault.getDetail() + " " + fault.getCode() + " " + fault.getMessage()) ;
            }
        });
    }

}
