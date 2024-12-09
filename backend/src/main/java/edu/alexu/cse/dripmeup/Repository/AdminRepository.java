package edu.alexu.cse.dripmeup.Repository;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {

    AdminEntity findByUserName(String userName);

}
