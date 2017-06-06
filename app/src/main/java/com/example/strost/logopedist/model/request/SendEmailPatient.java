package com.example.strost.logopedist.model.request;

import com.backendless.Backendless;
import com.example.strost.logopedist.model.entities.Patient;

import java.util.ArrayList;

/**
 * Created by strost on 29-3-2017.
 */

public class SendEmailPatient {

    public void sendEmail(String password, Patient patient ){
        final ArrayList<String> patients = new ArrayList<String>();
        patients.add( patient.getEmail());

        final String mailBody = "Hi "+patient.getFirstName()+" " +patient.getLastName()+ ",<br><br> Je inloggegevens voor de Logopedieapp zijn: <br><br> Email: "+patient.getEmail()+"<br> Tijdelijk wachtwoord: "+password+" <br><br> bij de eerste keer inloggen kan je je eigen wachtwoord kiezen </b><br> Met vriendelijke groet, <br> Logopedie";
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
