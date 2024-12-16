package edu.alexu.cse.dripmeup.repository;

import edu.alexu.cse.dripmeup.entity.OrderMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMetaRepository extends JpaRepository<OrderMeta, Long> {
}
