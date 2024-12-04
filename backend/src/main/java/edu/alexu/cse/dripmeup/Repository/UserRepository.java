package edu.alexu.cse.dripmeup.Repository;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByEmail(String email);
}
