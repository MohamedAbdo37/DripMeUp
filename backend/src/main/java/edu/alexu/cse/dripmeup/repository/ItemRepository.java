package edu.alexu.cse.dripmeup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.alexu.cse.dripmeup.entity.OrderItem;

public interface ItemRepository extends JpaRepository<OrderItem, Long> {

}
