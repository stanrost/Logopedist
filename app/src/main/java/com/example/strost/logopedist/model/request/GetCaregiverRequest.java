package com.example.strost.logopedist.model.request;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.example.strost.logopedist.model.entities.Caregiver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GetCaregiverRequest implements Serializable {

    List<Caregiver> caregivers = new ArrayList<Caregiver>();


    public List<Caregiver> getCaregiver() {
        BackendlessCollection<Caregiver> result = Backendless.Persistence.of( Caregiver.class ).find();
        caregivers = result.getCurrentPage();
        return caregivers;
    }
}
