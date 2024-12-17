package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.EntityIF;

public interface ProductBuilderIF {
    void build();

    EntityIF getResult();
}
