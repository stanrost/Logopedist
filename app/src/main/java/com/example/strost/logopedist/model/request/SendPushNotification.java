package com.example.strost.logopedist.model.request;

import com.backendless.Backendless;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.PublishOptions;
import com.backendless.services.messaging.MessageStatus;

/**
 * Created by strost on 16-5-2017.
 */

public class SendPushNotification {

    public void sendPushNotificaation(final DeliveryOptions deliveryOptions, final PublishOptions publishOptions){
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    MessageStatus status = Backendless.Messaging.publish("this is a private message!", publishOptions, deliveryOptions);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
