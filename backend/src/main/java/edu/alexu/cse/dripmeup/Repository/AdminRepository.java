package edu.alexu.cse.dripmeup.Repository;

import edu.alexu.cse.dripmeup.Entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<AdminEntity, String> {

    AdminEntity findByUserName(String userName);

}
