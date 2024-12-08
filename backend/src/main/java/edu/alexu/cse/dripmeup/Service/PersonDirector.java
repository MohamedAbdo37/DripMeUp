package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Service.builder.Builder;

public class PersonDirector {
    public Person construct(Builder builder) {
        builder.build();
        return builder.getResult();
    }
}
