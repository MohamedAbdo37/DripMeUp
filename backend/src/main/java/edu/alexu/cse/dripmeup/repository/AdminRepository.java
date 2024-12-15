package edu.alexu.cse.dripmeup.repository;

import edu.alexu.cse.dripmeup.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    AdminEntity findByUserName(String userName);

}
