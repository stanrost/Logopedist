package com.example.strost.logopedist.model.request;

import com.backendless.Backendless;
import com.example.strost.logopedist.model.entities.Patient;

public class RemovePatientRequest {

    public void removePatient(Patient p){
        Long result = Backendless.Persistence.of( Patient.class ).remove( p );
    }
}
