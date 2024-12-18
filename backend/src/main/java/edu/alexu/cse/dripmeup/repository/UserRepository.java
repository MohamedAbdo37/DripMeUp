package edu.alexu.cse.dripmeup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.alexu.cse.dripmeup.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserName(String userName);
    UserEntity findByEmail(String email);

    UserEntity findByUserID(long userID) ;

}
