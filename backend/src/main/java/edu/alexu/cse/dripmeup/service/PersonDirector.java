package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.service.builder.Builder;

public class PersonDirector {
    public Person construct(Builder builder) {
        builder.build();
        return builder.getResult();
    }
}
