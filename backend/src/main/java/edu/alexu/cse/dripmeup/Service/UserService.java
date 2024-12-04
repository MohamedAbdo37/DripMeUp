package edu.alexu.cse.dripmeup.Service;

import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Entity.UserEntity;
import edu.alexu.cse.dripmeup.Repository.AdminRepository;
import edu.alexu.cse.dripmeup.Repository.UserRepository;
import edu.alexu.cse.dripmeup.Service.Builder.UserPersonBuilder;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean login(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    public Person signup( UserEntity user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email already exists") ;
        }
        return new PersonDirector().construct(new UserPersonBuilder(user));
    }

    public boolean logInWithoutPassword(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return user != null;
    }

    public boolean changePassword(String email, UserEntity newPassword) {
        UserEntity user = userRepository.findByEmail(email);
        // changing password goes here
        return true;
    }
}
