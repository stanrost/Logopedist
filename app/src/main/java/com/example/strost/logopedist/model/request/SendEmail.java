package com.example.strost.logopedist.model.request;

import com.backendless.Backendless;

import java.util.ArrayList;

/**
 * Created by strost on 30-3-2017.
 */

public class SendEmail {

    public void sendEmail(final String title, String email, final String mailBody){
        final ArrayList<String> patients = new ArrayList<String>();
        patients.add(email);
        Runnable runnable = new Runnable() {
            public void run() {
                Backendless.Messaging.sendHTMLEmail(title, mailBody, patients );

            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
