package com.example.strost.logopedist.model.request;
import com.backendless.Backendless;
import com.example.strost.logopedist.model.entities.Caregiver;

public class UpdateCaregiverRequest {

    public void updateCaregiver(final Caregiver oldCaregiver, final Caregiver newCaregiver) {
        Caregiver caregiver = Backendless.Persistence.of( Caregiver.class ).findById( oldCaregiver.getObjectId() );
        caregiver = newCaregiver;
        Backendless.Persistence.save(caregiver);

    }
}


