package com.example.strost.logopedist.model.request;

import com.backendless.Backendless;
import com.example.strost.logopedist.model.entities.Caregiver;

import java.util.ArrayList;

/**
 * Created by strost on 29-3-2017.
 */

public class SendEmailCaregiver {

    public void sendEmail(String password, Caregiver caregiver ){
        final ArrayList<String> patients = new ArrayList<String>();
        patients.add( caregiver.getEmail());

        final String mailBody = "Hi "+caregiver.getFirstName()+ ",<br><br> Je inloggegevens voor de Logopedieapp zijn: <br><br> Email: "+caregiver.getEmail()+"<br> Tijdelijk wachtwoord: "+password+" <br><br> bij de eerste keer inloggen kan je je eigen wachtwoord kiezen </b><br> Met vriendelijke groet, <br> Logopedie";
        Runnable runnable = new Runnable() {
            public void run() {
                Backendless.Messaging.sendHTMLEmail( "Inloggegevens Logopedieapp", mailBody, patients );

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
