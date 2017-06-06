package com.example.strost.logopedist;

import android.content.Context;


import com.example.strost.logopedist.model.entities.Caregiver;
import com.example.strost.logopedist.model.request.SendEmail;
import com.example.strost.logopedist.model.request.UpdateCaregiverRequest;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * Created by strost on 30-3-2017.
 */

public class ForgotPassword {

    private Context mContext;

    public void forgotPassword(String email, List<Caregiver> caregivers, Context context){
        Caregiver mCaregiver = null;
        mContext = context;
        for (int i = 0; i < caregivers.size(); i++) {
            if (caregivers.get(i).getEmail().equals(email)){
                mCaregiver = caregivers.get(i);
            }
        }
        String password = randomString();

            sendEmail(mCaregiver, password);
        updateCaregiver(mCaregiver, password);

    }

    private void sendEmail(Caregiver mCaregiver, String password) {
        SendEmail sE = new SendEmail();
        String title = mContext.getString(R.string.email_title);
        String email = mCaregiver.getEmail();
        String body = mContext.getString(R.string.email_greeting) + " " + mCaregiver.getFirstName()+ " " + mCaregiver.getLastName()+ ", <br><br>"  +mContext.getString(R.string.email_body_part_1)+" <br>" + mContext.getString(R.string.email_body_part_2)+ " " + mCaregiver.getEmail() + "<br>" + mContext.getString(R.string.email_body_part_3) + " " +password  + "<br>"+ mContext.getString(R.string.email_body_part_4) + "<br><br>"+ mContext.getString(R.string.email_body_part_5) +"<br>" + mContext.getString(R.string.email_body_part_6);
        sE.sendEmail(title, email, body);
    }

    private void updateCaregiver(final Caregiver mCaregiver, String password) {
        PasswordEncryption pE = new PasswordEncryption();
        final UpdateCaregiverRequest uCR = new UpdateCaregiverRequest();
        mCaregiver.setChangedGenaratedPassword(false);
        mCaregiver.setPassword(pE.encryptPassword(password));
        uCR.updateCaregiver(mCaregiver);
    }

    public static String randomString() {
        char[] characterSet = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        Random random = new SecureRandom();
        char[] result = new char[8];
        for (int i = 0; i < result.length; i++) {
            int randomCharIndex = random.nextInt(characterSet.length);
            result[i] = characterSet[randomCharIndex];
        }
        return new String(result);
    }

}

