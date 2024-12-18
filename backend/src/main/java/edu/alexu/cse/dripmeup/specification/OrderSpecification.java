package edu.alexu.cse.dripmeup.specification;

import edu.alexu.cse.dripmeup.entity.Order;
import edu.alexu.cse.dripmeup.enumeration.Status;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    private OrderSpecification() {}
    public static Specification<Order> status(Status status) {
        return (root, query, builder) -> builder.equal(root.get("status"), status);
    }
    public static Specification<Order> user(Long id) {
        return (root, query, builder) -> builder.equal(root.get("userEntity").get("id"), id);
    }

}
