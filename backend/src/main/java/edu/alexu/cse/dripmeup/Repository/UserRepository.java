package edu.alexu.cse.dripmeup.Repository;

import edu.alexu.cse.dripmeup.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserName(String userName);
    UserEntity findByEmail(String email);


}
