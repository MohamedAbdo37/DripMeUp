package edu.alexu.cse.dripmeup.service;

import edu.alexu.cse.dripmeup.entity.Person;
import edu.alexu.cse.dripmeup.service.builder.PersonBuilder;

public class PersonDirector {
    public Person construct(PersonBuilder personBuilder) {
        personBuilder.build();
        return personBuilder.getResult();
    }
}
