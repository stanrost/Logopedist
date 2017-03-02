package com.example.strost.logopedist.model.request;

import com.backendless.Backendless;
import com.example.strost.logopedist.model.entities.Exercise;

public class RemoveExerciseRequest {

    public void removeExercise(Exercise op){
        Long result = Backendless.Persistence.of( Exercise.class ).remove( op );
    }
}
